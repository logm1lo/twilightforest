package twilightforest.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.compat.ModdedEntityTagGenerator;
import twilightforest.init.TFEntities;

import java.util.concurrent.CompletableFuture;

public class EntityTagGenerator extends ModdedEntityTagGenerator {
	public static final TagKey<EntityType<?>> BOSSES = create(TwilightForestMod.prefix("bosses"));
	public static final TagKey<EntityType<?>> LICH_POPPABLES = create(TwilightForestMod.prefix("lich_poppables"));
	public static final TagKey<EntityType<?>> LIFEDRAIN_DROPS_NO_FLESH = create(TwilightForestMod.prefix("lifedrain_drops_no_flesh"));
	public static final TagKey<EntityType<?>> RIDES_OBSTRUCT_SNATCHING = create(TwilightForestMod.prefix("rides_obstruct_snatching"));
	public static final TagKey<EntityType<?>> DONT_KILL_BUGS = create(TwilightForestMod.prefix("dont_kill_bugs"));
	public static final TagKey<EntityType<?>> SORTABLE_ENTITIES = create(TwilightForestMod.prefix("sortable_entities"));

	public EntityTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper helper) {
		super(output, provider, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);
		tag(EntityTypeTags.SKELETONS).add(TFEntities.SKELETON_DRUID.value());
		tag(EntityTypeTags.ARROWS).add(TFEntities.ICE_ARROW.value(), TFEntities.SEEKER_ARROW.value());
		tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(TFEntities.FIRE_BEETLE.value());
		tag(EntityTypeTags.FROG_FOOD).add(TFEntities.MAZE_SLIME.value());

		tag(BOSSES).add(
				TFEntities.NAGA.value(),
				TFEntities.LICH.value(),
				TFEntities.MINOSHROOM.value(),
				TFEntities.HYDRA.value(),
				TFEntities.KNIGHT_PHANTOM.value(),
				TFEntities.UR_GHAST.value(),
				TFEntities.ALPHA_YETI.value(),
				TFEntities.SNOW_QUEEN.value(),
				TFEntities.PLATEAU_BOSS.value()
		);

		tag(EntityTypeTags.IMPACT_PROJECTILES).add(
				TFEntities.NATURE_BOLT.value(),
				TFEntities.LICH_BOLT.value(),
				TFEntities.WAND_BOLT.value(),
				TFEntities.LICH_BOMB.value(),
				TFEntities.MOONWORM_SHOT.value(),
				TFEntities.SLIME_BLOB.value(),
				TFEntities.THROWN_WEP.value(),
				TFEntities.THROWN_ICE.value(),
				TFEntities.FALLING_ICE.value(),
				TFEntities.ICE_SNOWBALL.value()
		);

		tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(
				TFEntities.PENGUIN.value(),
				TFEntities.STABLE_ICE_CORE.value(),
				TFEntities.UNSTABLE_ICE_CORE.value(),
				TFEntities.SNOW_GUARDIAN.value(),
				TFEntities.ICE_CRYSTAL.value()
		).add(
				TFEntities.RAVEN.value(),
				TFEntities.SQUIRREL.value(),
				TFEntities.DWARF_RABBIT.value(),
				TFEntities.TINY_BIRD.value(),
				TFEntities.KOBOLD.value(),
				TFEntities.DEATH_TOME.value(),
				TFEntities.MOSQUITO_SWARM.value(),
				TFEntities.TOWERWOOD_BORER.value()
		);

		tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(
				TFEntities.PENGUIN.value(),
				TFEntities.STABLE_ICE_CORE.value(),
				TFEntities.UNSTABLE_ICE_CORE.value(),
				TFEntities.SNOW_GUARDIAN.value(),
				TFEntities.ICE_CRYSTAL.value()
		).add(
				TFEntities.WRAITH.value(),
				TFEntities.KNIGHT_PHANTOM.value(),
				TFEntities.WINTER_WOLF.value(),
				TFEntities.YETI.value()
		).addTag(BOSSES);

		tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(
				TFEntities.NAGA.value(),
				TFEntities.SQUIRREL.value(),
				TFEntities.WRAITH.value(),
				TFEntities.CARMINITE_GOLEM.value(),
				TFEntities.DEATH_TOME.value(),
				TFEntities.UR_GHAST.value(),
				TFEntities.CARMINITE_GHASTLING.value(),
				TFEntities.KNIGHT_PHANTOM.value(),
				TFEntities.SNOW_QUEEN.value(),
				TFEntities.PENGUIN.value(),
				TFEntities.RAVEN.value(),
				TFEntities.SNOW_GUARDIAN.value(),
				TFEntities.STABLE_ICE_CORE.value(),
				TFEntities.MOSQUITO_SWARM.value(),
				TFEntities.UNSTABLE_ICE_CORE.value(),
				TFEntities.ICE_CRYSTAL.value(),
				TFEntities.CARMINITE_GHASTGUARD.value(),
				TFEntities.TINY_BIRD.value());

		tag(LICH_POPPABLES).addTag(EntityTypeTags.SKELETONS).add(EntityType.ZOMBIE, EntityType.ENDERMAN, EntityType.SPIDER, EntityType.CREEPER, TFEntities.SWARM_SPIDER.value());

		tag(LIFEDRAIN_DROPS_NO_FLESH).addTag(EntityTypeTags.SKELETONS).addTag(EntityTypeTags.FROG_FOOD).add(
				EntityType.BLAZE,
				EntityType.IRON_GOLEM,
				EntityType.PHANTOM,
				EntityType.SHULKER,
				EntityType.SKELETON_HORSE,
				EntityType.SNOW_GOLEM,
				EntityType.VEX,
				EntityType.WITHER,
				TFEntities.CARMINITE_GOLEM.value(),
				TFEntities.DEATH_TOME.value(),
				TFEntities.ICE_CRYSTAL.value(),
				TFEntities.KNIGHT_PHANTOM.value(),
				TFEntities.LICH.value(),
				TFEntities.MOSQUITO_SWARM.value(),
				TFEntities.SNOW_GUARDIAN.value(),
				TFEntities.STABLE_ICE_CORE.value(),
				TFEntities.UNSTABLE_ICE_CORE.value(),
				TFEntities.WRAITH.value());

		// These entities forcefully take players from the entity they're riding
		tag(RIDES_OBSTRUCT_SNATCHING).add(TFEntities.PINCH_BEETLE.value(), TFEntities.YETI.value(), TFEntities.ALPHA_YETI.value());

		tag(DONT_KILL_BUGS).add(TFEntities.MOONWORM_SHOT.value());

		tag(SORTABLE_ENTITIES).add(
				EntityType.CHEST_MINECART,
				EntityType.HOPPER_MINECART,
				EntityType.LLAMA,
				EntityType.TRADER_LLAMA,
				EntityType.DONKEY,
				EntityType.MULE);

		tag(Tags.EntityTypes.BOSSES).addTag(BOSSES);
	}

	private static TagKey<EntityType<?>> create(ResourceLocation rl) {
		return TagKey.create(Registries.ENTITY_TYPE, rl);
	}

	@Override
	public String getName() {
		return "Twilight Forest Entity Tags";
	}
}
