package twilightforest.init;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.*;
import twilightforest.block.entity.spawner.*;
import twilightforest.client.renderer.tileentity.*;

public class TFBlockEntities {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<AntibuilderBlockEntity>> ANTIBUILDER = BLOCK_ENTITIES.register("antibuilder", () ->
			BlockEntityType.Builder.of(AntibuilderBlockEntity::new, TFBlocks.ANTIBUILDER.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<CinderFurnaceBlockEntity>> CINDER_FURNACE = BLOCK_ENTITIES.register("cinder_furnace", () ->
			BlockEntityType.Builder.of(CinderFurnaceBlockEntity::new, TFBlocks.CINDER_FURNACE.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<CarminiteReactorBlockEntity>> CARMINITE_REACTOR = BLOCK_ENTITIES.register("carminite_reactor", () ->
			BlockEntityType.Builder.of(CarminiteReactorBlockEntity::new, TFBlocks.CARMINITE_REACTOR.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<FireJetBlockEntity>> FLAME_JET = BLOCK_ENTITIES.register("flame_jet", () ->
			BlockEntityType.Builder.of(FireJetBlockEntity::new, TFBlocks.FIRE_JET.value(), TFBlocks.ENCASED_FIRE_JET.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<GhastTrapBlockEntity>> GHAST_TRAP = BLOCK_ENTITIES.register("ghast_trap", () ->
			BlockEntityType.Builder.of(GhastTrapBlockEntity::new, TFBlocks.GHAST_TRAP.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TFSmokerBlockEntity>> SMOKER = BLOCK_ENTITIES.register("smoker", () ->
			BlockEntityType.Builder.of(TFSmokerBlockEntity::new, TFBlocks.SMOKER.value(), TFBlocks.ENCASED_SMOKER.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<CarminiteBuilderBlockEntity>> TOWER_BUILDER = BLOCK_ENTITIES.register("tower_builder", () ->
			BlockEntityType.Builder.of(CarminiteBuilderBlockEntity::new, TFBlocks.CARMINITE_BUILDER.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TrophyBlockEntity>> TROPHY = BLOCK_ENTITIES.register("trophy", () ->
			BlockEntityType.Builder.of(TrophyBlockEntity::new, TFBlocks.NAGA_TROPHY.value(), TFBlocks.LICH_TROPHY.value(), TFBlocks.MINOSHROOM_TROPHY.value(),
					TFBlocks.HYDRA_TROPHY.value(), TFBlocks.KNIGHT_PHANTOM_TROPHY.value(), TFBlocks.UR_GHAST_TROPHY.value(), TFBlocks.ALPHA_YETI_TROPHY.value(),
					TFBlocks.SNOW_QUEEN_TROPHY.value(), TFBlocks.QUEST_RAM_TROPHY.value(), TFBlocks.NAGA_WALL_TROPHY.value(), TFBlocks.LICH_WALL_TROPHY.value(),
					TFBlocks.MINOSHROOM_WALL_TROPHY.value(), TFBlocks.HYDRA_WALL_TROPHY.value(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.value(), TFBlocks.UR_GHAST_WALL_TROPHY.value(),
					TFBlocks.ALPHA_YETI_WALL_TROPHY.value(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.value(), TFBlocks.QUEST_RAM_WALL_TROPHY.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<AlphaYetiSpawnerBlockEntity>> ALPHA_YETI_SPAWNER = BLOCK_ENTITIES.register("alpha_yeti_spawner", () ->
			BlockEntityType.Builder.of(AlphaYetiSpawnerBlockEntity::new, TFBlocks.ALPHA_YETI_BOSS_SPAWNER.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<FinalBossSpawnerBlockEntity>> FINAL_BOSS_SPAWNER = BLOCK_ENTITIES.register("final_boss_spawner", () ->
			BlockEntityType.Builder.of(FinalBossSpawnerBlockEntity::new, TFBlocks.FINAL_BOSS_BOSS_SPAWNER.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<HydraSpawnerBlockEntity>> HYDRA_SPAWNER = BLOCK_ENTITIES.register("hydra_boss_spawner", () ->
			BlockEntityType.Builder.of(HydraSpawnerBlockEntity::new, TFBlocks.HYDRA_BOSS_SPAWNER.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<KnightPhantomSpawnerBlockEntity>> KNIGHT_PHANTOM_SPAWNER = BLOCK_ENTITIES.register("knight_phantom_spawner", () ->
			BlockEntityType.Builder.of(KnightPhantomSpawnerBlockEntity::new, TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<LichSpawnerBlockEntity>> LICH_SPAWNER = BLOCK_ENTITIES.register("lich_spawner", () ->
			BlockEntityType.Builder.of(LichSpawnerBlockEntity::new, TFBlocks.LICH_BOSS_SPAWNER.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<MinoshroomSpawnerBlockEntity>> MINOSHROOM_SPAWNER = BLOCK_ENTITIES.register("minoshroom_spawner", () ->
			BlockEntityType.Builder.of(MinoshroomSpawnerBlockEntity::new, TFBlocks.MINOSHROOM_BOSS_SPAWNER.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<NagaSpawnerBlockEntity>> NAGA_SPAWNER = BLOCK_ENTITIES.register("naga_spawner", () ->
			BlockEntityType.Builder.of(NagaSpawnerBlockEntity::new, TFBlocks.NAGA_BOSS_SPAWNER.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<SnowQueenSpawnerBlockEntity>> SNOW_QUEEN_SPAWNER = BLOCK_ENTITIES.register("snow_queen_spawner", () ->
			BlockEntityType.Builder.of(SnowQueenSpawnerBlockEntity::new, TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<UrGhastSpawnerBlockEntity>> UR_GHAST_SPAWNER = BLOCK_ENTITIES.register("tower_boss_spawner", () ->
			BlockEntityType.Builder.of(UrGhastSpawnerBlockEntity::new, TFBlocks.UR_GHAST_BOSS_SPAWNER.value()).build(null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<CicadaBlockEntity>> CICADA = BLOCK_ENTITIES.register("cicada", () ->
			BlockEntityType.Builder.of(CicadaBlockEntity::new, TFBlocks.CICADA.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<FireflyBlockEntity>> FIREFLY = BLOCK_ENTITIES.register("firefly", () ->
			BlockEntityType.Builder.of(FireflyBlockEntity::new, TFBlocks.FIREFLY.value()).build(null));
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<MoonwormBlockEntity>> MOONWORM = BLOCK_ENTITIES.register("moonworm", () ->
			BlockEntityType.Builder.of(MoonwormBlockEntity::new, TFBlocks.MOONWORM.value()).build(null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<KeepsakeCasketBlockEntity>> KEEPSAKE_CASKET = BLOCK_ENTITIES.register("keepsake_casket", () ->
			BlockEntityType.Builder.of(KeepsakeCasketBlockEntity::new, TFBlocks.KEEPSAKE_CASKET.value()).build(null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TFSignBlockEntity>> TF_SIGN = BLOCK_ENTITIES.register("tf_sign", () ->
			BlockEntityType.Builder.of(TFSignBlockEntity::new,
					TFBlocks.TWILIGHT_OAK_SIGN.value(), TFBlocks.TWILIGHT_WALL_SIGN.value(),
					TFBlocks.CANOPY_SIGN.value(), TFBlocks.CANOPY_WALL_SIGN.value(),
					TFBlocks.MANGROVE_SIGN.value(), TFBlocks.MANGROVE_WALL_SIGN.value(),
					TFBlocks.DARK_SIGN.value(), TFBlocks.DARK_WALL_SIGN.value(),
					TFBlocks.TIME_SIGN.value(), TFBlocks.TIME_WALL_SIGN.value(),
					TFBlocks.TRANSFORMATION_SIGN.value(), TFBlocks.TRANSFORMATION_WALL_SIGN.value(),
					TFBlocks.MINING_SIGN.value(), TFBlocks.MINING_WALL_SIGN.value(),
					TFBlocks.SORTING_SIGN.value(), TFBlocks.SORTING_WALL_SIGN.value()).build(null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TFHangingSignBlockEntity>> TF_HANGING_SIGN = BLOCK_ENTITIES.register("tf_hanging_sign", () ->
			BlockEntityType.Builder.of(TFHangingSignBlockEntity::new,
					TFBlocks.TWILIGHT_OAK_HANGING_SIGN.value(), TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.value(),
					TFBlocks.CANOPY_HANGING_SIGN.value(), TFBlocks.CANOPY_WALL_HANGING_SIGN.value(),
					TFBlocks.MANGROVE_HANGING_SIGN.value(), TFBlocks.MANGROVE_WALL_HANGING_SIGN.value(),
					TFBlocks.DARK_HANGING_SIGN.value(), TFBlocks.DARK_WALL_HANGING_SIGN.value(),
					TFBlocks.TIME_HANGING_SIGN.value(), TFBlocks.TIME_WALL_HANGING_SIGN.value(),
					TFBlocks.TRANSFORMATION_HANGING_SIGN.value(), TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.value(),
					TFBlocks.MINING_HANGING_SIGN.value(), TFBlocks.MINING_WALL_HANGING_SIGN.value(),
					TFBlocks.SORTING_HANGING_SIGN.value(), TFBlocks.SORTING_WALL_HANGING_SIGN.value()).build(null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TFChestBlockEntity>> TF_CHEST = BLOCK_ENTITIES.register("tf_chest", () ->
			BlockEntityType.Builder.of(TFChestBlockEntity::new,
					TFBlocks.TWILIGHT_OAK_CHEST.value(), TFBlocks.CANOPY_CHEST.value(), TFBlocks.MANGROVE_CHEST.value(),
					TFBlocks.DARK_CHEST.value(), TFBlocks.TIME_CHEST.value(), TFBlocks.TRANSFORMATION_CHEST.value(),
					TFBlocks.MINING_CHEST.value(), TFBlocks.SORTING_CHEST.value()).build(null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TFChestBlockEntity>> TF_TRAPPED_CHEST = BLOCK_ENTITIES.register("tf_trapped_chest", () ->
			BlockEntityType.Builder.of(TFChestBlockEntity::new,
					TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.value(), TFBlocks.CANOPY_TRAPPED_CHEST.value(), TFBlocks.MANGROVE_TRAPPED_CHEST.value(),
					TFBlocks.DARK_TRAPPED_CHEST.value(), TFBlocks.TIME_TRAPPED_CHEST.value(), TFBlocks.TRANSFORMATION_TRAPPED_CHEST.value(),
					TFBlocks.MINING_TRAPPED_CHEST.value(), TFBlocks.SORTING_TRAPPED_CHEST.value()).build(null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<SkullCandleBlockEntity>> SKULL_CANDLE = BLOCK_ENTITIES.register("skull_candle", () ->
			BlockEntityType.Builder.of(SkullCandleBlockEntity::new,
					TFBlocks.ZOMBIE_SKULL_CANDLE.value(), TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.value(),
					TFBlocks.SKELETON_SKULL_CANDLE.value(), TFBlocks.SKELETON_WALL_SKULL_CANDLE.value(),
					TFBlocks.WITHER_SKELE_SKULL_CANDLE.value(), TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.value(),
					TFBlocks.CREEPER_SKULL_CANDLE.value(), TFBlocks.CREEPER_WALL_SKULL_CANDLE.value(),
					TFBlocks.PLAYER_SKULL_CANDLE.value(), TFBlocks.PLAYER_WALL_SKULL_CANDLE.value(),
					TFBlocks.PIGLIN_SKULL_CANDLE.value(), TFBlocks.PIGLIN_WALL_SKULL_CANDLE.value()).build(null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TomeSpawnerBlockEntity>> TOME_SPAWNER = BLOCK_ENTITIES.register("tome_spawner", () ->
			BlockEntityType.Builder.of(TomeSpawnerBlockEntity::new, TFBlocks.DEATH_TOME_SPAWNER.value()).build(null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<GrowingBeanstalkBlockEntity>> BEANSTALK_GROWER = BLOCK_ENTITIES.register("beanstalk_grower", () ->
			BlockEntityType.Builder.of(GrowingBeanstalkBlockEntity::new, TFBlocks.BEANSTALK_GROWER.value()).build(null));

	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<RedThreadBlockEntity>> RED_THREAD = BLOCK_ENTITIES.register("red_thread", () ->
			BlockEntityType.Builder.of(RedThreadBlockEntity::new, TFBlocks.RED_THREAD.value()).build(null));

	@OnlyIn(Dist.CLIENT)
	public static void registerTileEntityRenders() {
		// tile entities
		BlockEntityRenderers.register(FIREFLY.value(), FireflyTileEntityRenderer::new);
		BlockEntityRenderers.register(CICADA.value(), CicadaTileEntityRenderer::new);
		BlockEntityRenderers.register(MOONWORM.value(), MoonwormTileEntityRenderer::new);
		BlockEntityRenderers.register(TROPHY.value(), TrophyTileEntityRenderer::new);
		BlockEntityRenderers.register(TF_SIGN.value(), SignRenderer::new);
		BlockEntityRenderers.register(TF_HANGING_SIGN.value(), HangingSignRenderer::new);
		BlockEntityRenderers.register(TF_CHEST.value(), TFChestTileEntityRenderer::new);
		BlockEntityRenderers.register(TF_TRAPPED_CHEST.value(), TFChestTileEntityRenderer::new);
		BlockEntityRenderers.register(KEEPSAKE_CASKET.value(), CasketTileEntityRenderer::new);
		BlockEntityRenderers.register(SKULL_CANDLE.value(), SkullCandleTileEntityRenderer::new);
		BlockEntityRenderers.register(RED_THREAD.value(), RedThreadRenderer::new);
	}
}
