package twilightforest.entity.boss;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.loot.TFLootTables;
import twilightforest.network.UpdateDeathTimePacket;
import twilightforest.util.EntityUtil;
import twilightforest.util.LandmarkUtil;

import java.util.Optional;

public abstract class BaseTFBoss extends Monster implements IBossLootBuffer, EnforcedHomePoint {
	private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(BaseTFBoss.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);

	private final ServerBossEvent bossEvent;
	private final NonNullList<ItemStack> dyingInventory = NonNullList.withSize(27, ItemStack.EMPTY);

	@SuppressWarnings("this-escape")
	protected BaseTFBoss(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.bossEvent = this.createBossEvent();
	}

	public abstract ResourceKey<Structure> getHomeStructure();

	public abstract Block getDeathContainer(RandomSource random);

	public abstract Block getBossSpawner();

	protected boolean shouldSpawnLoot() {
		return this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
	}

	protected boolean shouldCreateSpawner() {
		return true;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(HOME_POINT, Optional.empty());
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (!this.level().isClientSide()) {
			this.getBossBar().setProgress(this.getHealth() / this.getMaxHealth());
		}
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.getBossBar().addPlayer(player);
		if (this.deathTime > 0) PacketDistributor.sendToPlayersTrackingEntity(this, new UpdateDeathTimePacket(this.getId(), this.deathTime));
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.getBossBar().removePlayer(player);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		this.saveHomePointToNbt(compound);
		this.addDeathItemsSaveData(compound, this.registryAccess());
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.readDeathItemsSaveData(compound, this.registryAccess());
		this.loadHomePointFromNbt(compound);
	}

	@Override
	public void lavaHurt() {
		if (!this.fireImmune()) {
			this.igniteForSeconds(5);
			if (this.hurt(this.damageSources().lava(), 4.0F)) {
				this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.getRandom().nextFloat() * 0.4F);
				EntityUtil.killLavaAround(this);
			}
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		if (this.shouldSpawnLoot() && this.level() instanceof ServerLevel server) this.postmortem(server, cause);
	}

	// mark the boss structure as conquered, separate method, so it can be overridden
	protected void postmortem(ServerLevel serverLevel, DamageSource cause) {
		this.getBossBar().setProgress(0.0F);
		IBossLootBuffer.saveDropsIntoBoss(this, TFLootTables.createLootParams(this, true, cause).create(LootContextParamSets.ENTITY), serverLevel);
		LandmarkUtil.markStructureConquered(serverLevel, this, this.getHomeStructure(), true);
	}

	@Override
	public void remove(RemovalReason reason) {
		if (this.level() instanceof ServerLevel serverLevel) this.postRemoval(serverLevel, reason);
		super.remove(reason);
	}

	// drop loot into a chest after removal, separate method, so it can be overridden
	protected void postRemoval(ServerLevel serverLevel, RemovalReason reason) {
		if (reason.equals(RemovalReason.KILLED) && this.shouldSpawnLoot()) {
			IBossLootBuffer.depositDropsIntoChest(this, this.getDeathContainer(this.getRandom()).defaultBlockState().setValue(ChestBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(this.level().getRandom())), EntityUtil.bossChestLocation(this), serverLevel);
		}
	}

	@Override
	public void checkDespawn() {
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
			if (this.shouldCreateSpawner() && this.isRestrictionPointValid(this.level().dimension()) && this.level().isLoaded(this.getRestrictionPoint().pos())) {
				this.level().setBlockAndUpdate(this.getRestrictionPoint().pos(), this.getBossSpawner().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	@Override
	protected boolean shouldDropLoot() {
		return !TFConfig.bossDropChests;
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean isPushedByFluid(FluidType type) {
		return false;
	}

	@Override
	protected float getWaterSlowDown() {
		return 1.0F;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public @Nullable GlobalPos getRestrictionPoint() {
		return this.getEntityData().get(HOME_POINT).orElse(null);
	}

	@Override
	public void setRestrictionPoint(@Nullable GlobalPos pos) {
		this.getEntityData().set(HOME_POINT, Optional.ofNullable(pos));
	}

	@Override
	public NonNullList<ItemStack> getItemStacks() {
		return this.dyingInventory;
	}

	@Override
	protected void tickDeath() {
		this.deathTime++;
		if (!this.isRemoved()) {
			if (this.isDeathAnimationFinished() && !this.level().isClientSide()) {
				this.level().broadcastEntityEvent(this, (byte) 60); // makePoofParticles()
				this.remove(RemovalReason.KILLED);
			} else if (this.level().isClientSide()) this.tickDeathAnimation();
		}
	}

	public boolean isDeathAnimationFinished() {
		return this.deathTime >= 20;
	}

	public void tickDeathAnimation() {

	}

	public ServerBossEvent getBossBar() {
		return this.bossEvent;
	}

	@Override
	public void setId(int pId) {
		super.setId(pId);
		this.getBossBar().setName(this.createBossBarID());
	}

	protected ServerBossEvent createBossEvent() {
		return new ServerBossEvent(this.createBossBarID(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
	}

	protected Component createBossBarID() {
		return Component.literal(TwilightForestMod.ID + ":" + this.getId());
	}

	public Component getBossBarTitle() {
		return this.getDisplayName() != null ? this.getDisplayName() : this.getTypeName();
	}

	public BossEvent.BossBarOverlay getBossBarOverlay() {
		return BossEvent.BossBarOverlay.PROGRESS;
	}

	public abstract int getBossBarColor();

	private static final ResourceLocation BAR_BACKGROUND = new ResourceLocation("boss_bar/white_background");
	private static final ResourceLocation BAR_PROGRESS = new ResourceLocation("boss_bar/white_progress");

	public void renderBossBar(LerpingBossEvent bossEvent, GuiGraphics guiGraphics, int x, int y, float progress) {
		RenderSystem.enableBlend();
		int color = this.getBossBarColor();
		RenderSystem.setShaderColor(((color >> 16) & 255) / 255F, ((color >> 8) & 255) / 255F, (color & 255) / 255F, 1.0F);

		BossEvent.BossBarOverlay style = this.getBossBarOverlay();
		guiGraphics.blitSprite(BAR_BACKGROUND, 182, 5, 0, 0, x, y, 182, 5);
		if (style != BossEvent.BossBarOverlay.PROGRESS) guiGraphics.blitSprite(BossHealthOverlay.OVERLAY_BACKGROUND_SPRITES[style.ordinal() - 1], 182, 5, 0, 0, x, y, 182, 5);
		int i = Mth.lerpDiscrete(progress, 0, 182);
		if (i > 0) {
			guiGraphics.blitSprite(BAR_PROGRESS, 182, 5, 0, 0, x, y, i, 5);
			if (style != BossEvent.BossBarOverlay.PROGRESS) guiGraphics.blitSprite(BossHealthOverlay.OVERLAY_PROGRESS_SPRITES[style.ordinal() - 1], 182, 5, 0, 0, x, y, i, 5);
		}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
	}
}
