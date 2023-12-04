package twilightforest.data.tags;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.compat.ModdedBlockTagGenerator;
import twilightforest.init.TFBlocks;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class BlockTagGenerator extends ModdedBlockTagGenerator {
	public static final TagKey<Block> TOWERWOOD = BlockTags.create(TwilightForestMod.prefix("towerwood"));
	public static final TagKey<Block> MAZESTONE = BlockTags.create(TwilightForestMod.prefix("mazestone"));
	public static final TagKey<Block> CASTLE_BLOCKS = BlockTags.create(TwilightForestMod.prefix("castle_blocks"));

	public static final TagKey<Block> TWILIGHT_OAK_LOGS = BlockTags.create(TwilightForestMod.prefix("twilight_oak_logs"));
	public static final TagKey<Block> CANOPY_LOGS = BlockTags.create(TwilightForestMod.prefix("canopy_logs"));
	public static final TagKey<Block> MANGROVE_LOGS = BlockTags.create(TwilightForestMod.prefix("mangrove_logs"));
	public static final TagKey<Block> DARKWOOD_LOGS = BlockTags.create(TwilightForestMod.prefix("darkwood_logs"));
	public static final TagKey<Block> TIME_LOGS = BlockTags.create(TwilightForestMod.prefix("timewood_logs"));
	public static final TagKey<Block> TRANSFORMATION_LOGS = BlockTags.create(TwilightForestMod.prefix("transwood_logs"));
	public static final TagKey<Block> MINING_LOGS = BlockTags.create(TwilightForestMod.prefix("mining_logs"));
	public static final TagKey<Block> SORTING_LOGS = BlockTags.create(TwilightForestMod.prefix("sortwood_logs"));

	public static final TagKey<Block> TF_LOGS = BlockTags.create(TwilightForestMod.prefix("logs"));
	public static final TagKey<Block> BANISTERS = BlockTags.create(TwilightForestMod.prefix("banisters"));
	public static final TagKey<Block> HOLLOW_LOGS_HORIZONTAL = BlockTags.create(TwilightForestMod.prefix("hollow_logs_horizontal"));
	public static final TagKey<Block> HOLLOW_LOGS_VERTICAL = BlockTags.create(TwilightForestMod.prefix("hollow_logs_vertical"));
	public static final TagKey<Block> HOLLOW_LOGS_CLIMBABLE = BlockTags.create(TwilightForestMod.prefix("hollow_logs_climbable"));
	public static final TagKey<Block> HOLLOW_LOGS = BlockTags.create(TwilightForestMod.prefix("hollow_logs"));

	public static final TagKey<Block> STORAGE_BLOCKS_ARCTIC_FUR = BlockTags.create(TwilightForestMod.prefix("storage_blocks/arctic_fur"));
	public static final TagKey<Block> STORAGE_BLOCKS_CARMINITE = BlockTags.create(TwilightForestMod.prefix("storage_blocks/carminite"));
	public static final TagKey<Block> STORAGE_BLOCKS_FIERY = BlockTags.create(TwilightForestMod.prefix("storage_blocks/fiery"));
	public static final TagKey<Block> STORAGE_BLOCKS_IRONWOOD = BlockTags.create(TwilightForestMod.prefix("storage_blocks/ironwood"));
	public static final TagKey<Block> STORAGE_BLOCKS_KNIGHTMETAL = BlockTags.create(TwilightForestMod.prefix("storage_blocks/knightmetal"));
	public static final TagKey<Block> STORAGE_BLOCKS_STEELEAF = BlockTags.create(TwilightForestMod.prefix("storage_blocks/steeleaf"));

	public static final TagKey<Block> PORTAL_EDGE = BlockTags.create(TwilightForestMod.prefix("portal/edge"));
	public static final TagKey<Block> PORTAL_POOL = BlockTags.create(TwilightForestMod.prefix("portal/fluid"));
	public static final TagKey<Block> PORTAL_DECO = BlockTags.create(TwilightForestMod.prefix("portal/decoration"));
	public static final TagKey<Block> GENERATED_PORTAL_DECO = BlockTags.create(TwilightForestMod.prefix("portal/generated_decoration"));

	public static final TagKey<Block> DARK_TOWER_ALLOWED_POTS = BlockTags.create(TwilightForestMod.prefix("dark_tower_allowed_pots"));
	public static final TagKey<Block> TROPHIES = BlockTags.create(TwilightForestMod.prefix("trophies"));
	public static final TagKey<Block> FIRE_JET_FUEL = BlockTags.create(TwilightForestMod.prefix("fire_jet_fuel"));
	public static final TagKey<Block> ICE_BOMB_REPLACEABLES = BlockTags.create(TwilightForestMod.prefix("ice_bomb_replaceables"));
	public static final TagKey<Block> MAZEBREAKER_ACCELERATED = BlockTags.create(TwilightForestMod.prefix("mazebreaker_accelerated_mining"));
	public static final TagKey<Block> PLANTS_HANG_ON = BlockTags.create(TwilightForestMod.prefix("plants_hang_on"));

	public static final TagKey<Block> COMMON_PROTECTIONS = BlockTags.create(TwilightForestMod.prefix("common_protections"));
	public static final TagKey<Block> ANNIHILATION_INCLUSIONS = BlockTags.create(TwilightForestMod.prefix("annihilation_inclusions"));
	public static final TagKey<Block> ANTIBUILDER_IGNORES = BlockTags.create(TwilightForestMod.prefix("antibuilder_ignores"));
	public static final TagKey<Block> CARMINITE_REACTOR_IMMUNE = BlockTags.create(TwilightForestMod.prefix("carminite_reactor_immune"));
	public static final TagKey<Block> CARMINITE_REACTOR_ORES = BlockTags.create(TwilightForestMod.prefix("carminite_reactor_ores"));
	public static final TagKey<Block> STRUCTURE_BANNED_INTERACTIONS = BlockTags.create(TwilightForestMod.prefix("structure_banned_interactions"));
	public static final TagKey<Block> PROGRESSION_ALLOW_BREAKING = BlockTags.create(TwilightForestMod.prefix("progression_allow_breaking"));

	public static final TagKey<Block> WORLDGEN_REPLACEABLES = BlockTags.create(TwilightForestMod.prefix("worldgen_replaceables"));
	public static final TagKey<Block> ROOT_TRACE_SKIP = BlockTags.create(TwilightForestMod.prefix("tree_roots_skip"));

	public static final TagKey<Block> ORE_MAGNET_SAFE_REPLACE_BLOCK = BlockTags.create(TwilightForestMod.prefix("ore_magnet/ore_safe_replace_block"));
	public static final TagKey<Block> ORE_MAGNET_IGNORE = BlockTags.create(TwilightForestMod.prefix("ore_magnet/ignored_ores"));
	public static final TagKey<Block> ROOT_GROUND = BlockTags.create(new ResourceLocation("forge", "ore_bearing_ground/root"));
	public static final TagKey<Block> ROOT_ORES = BlockTags.create(new ResourceLocation("forge", "ores_in_ground/root"));

	public static final TagKey<Block> TIME_CORE_EXCLUDED = BlockTags.create(TwilightForestMod.prefix("time_core_excluded"));

	public static final TagKey<Block> PENGUINS_SPAWNABLE_ON = BlockTags.create(TwilightForestMod.prefix("penguins_spawnable_on"));
	public static final TagKey<Block> GIANTS_SPAWNABLE_ON = BlockTags.create(TwilightForestMod.prefix("giants_spawnable_on"));

	public static final TagKey<Block> RELOCATION_NOT_SUPPORTED = BlockTags.create(new ResourceLocation("forge", "relocation_not_supported"));
	public static final TagKey<Block> IMMOVABLE = BlockTags.create(new ResourceLocation("forge", "immovable"));

	public static final TagKey<Block> DRUID_PROJECTILE_REPLACEABLE = BlockTags.create(TwilightForestMod.prefix("druid_projectile_replaceable"));

	public static final TagKey<Block> CLOUDS = BlockTags.create(TwilightForestMod.prefix("clouds"));

	public static final TagKey<Block> TF_CHESTS = BlockTags.create(TwilightForestMod.prefix("chests"));

	public BlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
		super(output, future, helper);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);
		tag(TWILIGHT_OAK_LOGS)
				.add(TFBlocks.TWILIGHT_OAK_LOG.value(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.value(), TFBlocks.TWILIGHT_OAK_WOOD.value(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.value());
		tag(CANOPY_LOGS)
				.add(TFBlocks.CANOPY_LOG.value(), TFBlocks.STRIPPED_CANOPY_LOG.value(), TFBlocks.CANOPY_WOOD.value(), TFBlocks.STRIPPED_CANOPY_WOOD.value());
		tag(MANGROVE_LOGS)
				.add(TFBlocks.MANGROVE_LOG.value(), TFBlocks.STRIPPED_MANGROVE_LOG.value(), TFBlocks.MANGROVE_WOOD.value(), TFBlocks.STRIPPED_MANGROVE_WOOD.value());
		tag(DARKWOOD_LOGS)
				.add(TFBlocks.DARK_LOG.value(), TFBlocks.STRIPPED_DARK_LOG.value(), TFBlocks.DARK_WOOD.value(), TFBlocks.STRIPPED_DARK_WOOD.value());
		tag(TIME_LOGS)
				.add(TFBlocks.TIME_LOG.value(), TFBlocks.STRIPPED_TIME_LOG.value(), TFBlocks.TIME_WOOD.value(), TFBlocks.STRIPPED_TIME_WOOD.value());
		tag(TRANSFORMATION_LOGS)
				.add(TFBlocks.TRANSFORMATION_LOG.value(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.value(), TFBlocks.TRANSFORMATION_WOOD.value(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.value());
		tag(MINING_LOGS)
				.add(TFBlocks.MINING_LOG.value(), TFBlocks.STRIPPED_MINING_LOG.value(), TFBlocks.MINING_WOOD.value(), TFBlocks.STRIPPED_MINING_WOOD.value());
		tag(SORTING_LOGS)
				.add(TFBlocks.SORTING_LOG.value(), TFBlocks.STRIPPED_SORTING_LOG.value(), TFBlocks.SORTING_WOOD.value(), TFBlocks.STRIPPED_SORTING_WOOD.value());

		tag(TF_LOGS)
				.addTags(TWILIGHT_OAK_LOGS, CANOPY_LOGS, MANGROVE_LOGS, DARKWOOD_LOGS, TIME_LOGS, TRANSFORMATION_LOGS, MINING_LOGS, SORTING_LOGS);
		tag(BlockTags.LOGS)
				.addTag(TF_LOGS);

		tag(BlockTags.LOGS_THAT_BURN)
				.addTags(TWILIGHT_OAK_LOGS, CANOPY_LOGS, MANGROVE_LOGS, TIME_LOGS, TRANSFORMATION_LOGS, MINING_LOGS, SORTING_LOGS);

		tag(BlockTags.SAPLINGS)
				.add(TFBlocks.TWILIGHT_OAK_SAPLING.value(), TFBlocks.CANOPY_SAPLING.value(), TFBlocks.MANGROVE_SAPLING.value(), TFBlocks.DARKWOOD_SAPLING.value())
				.add(TFBlocks.TIME_SAPLING.value(), TFBlocks.TRANSFORMATION_SAPLING.value(), TFBlocks.MINING_SAPLING.value(), TFBlocks.SORTING_SAPLING.value())
				.add(TFBlocks.HOLLOW_OAK_SAPLING.value(), TFBlocks.RAINBOW_OAK_SAPLING.value());
		tag(BlockTags.LEAVES)
				.add(TFBlocks.RAINBOW_OAK_LEAVES.value(), TFBlocks.TWILIGHT_OAK_LEAVES.value(), TFBlocks.CANOPY_LEAVES.value(), TFBlocks.MANGROVE_LEAVES.value(), TFBlocks.DARK_LEAVES.value())
				.add(TFBlocks.TIME_LEAVES.value(), TFBlocks.TRANSFORMATION_LEAVES.value(), TFBlocks.MINING_LEAVES.value(), TFBlocks.SORTING_LEAVES.value())
				.add(TFBlocks.THORN_LEAVES.value(), TFBlocks.BEANSTALK_LEAVES.value());

		tag(BlockTags.PLANKS)
				.add(TFBlocks.TWILIGHT_OAK_PLANKS.value(), TFBlocks.CANOPY_PLANKS.value(), TFBlocks.MANGROVE_PLANKS.value(), TFBlocks.DARK_PLANKS.value())
				.add(TFBlocks.TIME_PLANKS.value(), TFBlocks.TRANSFORMATION_PLANKS.value(), TFBlocks.MINING_PLANKS.value(), TFBlocks.SORTING_PLANKS.value())
				.add(TFBlocks.TOWERWOOD.value(), TFBlocks.ENCASED_TOWERWOOD.value(), TFBlocks.CRACKED_TOWERWOOD.value(), TFBlocks.MOSSY_TOWERWOOD.value(), TFBlocks.INFESTED_TOWERWOOD.value());

		tag(BlockTags.WOODEN_FENCES)
				.add(TFBlocks.TWILIGHT_OAK_FENCE.value(), TFBlocks.CANOPY_FENCE.value(), TFBlocks.MANGROVE_FENCE.value(), TFBlocks.DARK_FENCE.value())
				.add(TFBlocks.TIME_FENCE.value(), TFBlocks.TRANSFORMATION_FENCE.value(), TFBlocks.MINING_FENCE.value(), TFBlocks.SORTING_FENCE.value());
		tag(BlockTags.FENCE_GATES)
				.add(TFBlocks.TWILIGHT_OAK_GATE.value(), TFBlocks.CANOPY_GATE.value(), TFBlocks.MANGROVE_GATE.value(), TFBlocks.DARK_GATE.value())
				.add(TFBlocks.TIME_GATE.value(), TFBlocks.TRANSFORMATION_GATE.value(), TFBlocks.MINING_GATE.value(), TFBlocks.SORTING_GATE.value());
		tag(Tags.Blocks.FENCES)
				.add(TFBlocks.TWILIGHT_OAK_FENCE.value(), TFBlocks.CANOPY_FENCE.value(), TFBlocks.MANGROVE_FENCE.value(), TFBlocks.DARK_FENCE.value())
				.add(TFBlocks.TIME_FENCE.value(), TFBlocks.TRANSFORMATION_FENCE.value(), TFBlocks.MINING_FENCE.value(), TFBlocks.SORTING_FENCE.value());
		tag(Tags.Blocks.FENCE_GATES)
				.add(TFBlocks.TWILIGHT_OAK_GATE.value(), TFBlocks.CANOPY_GATE.value(), TFBlocks.MANGROVE_GATE.value(), TFBlocks.DARK_GATE.value())
				.add(TFBlocks.TIME_GATE.value(), TFBlocks.TRANSFORMATION_GATE.value(), TFBlocks.MINING_GATE.value(), TFBlocks.SORTING_GATE.value());
		tag(Tags.Blocks.FENCES_WOODEN)
				.add(TFBlocks.TWILIGHT_OAK_FENCE.value(), TFBlocks.CANOPY_FENCE.value(), TFBlocks.MANGROVE_FENCE.value(), TFBlocks.DARK_FENCE.value())
				.add(TFBlocks.TIME_FENCE.value(), TFBlocks.TRANSFORMATION_FENCE.value(), TFBlocks.MINING_FENCE.value(), TFBlocks.SORTING_FENCE.value());
		tag(Tags.Blocks.FENCE_GATES_WOODEN)
				.add(TFBlocks.TWILIGHT_OAK_GATE.value(), TFBlocks.CANOPY_GATE.value(), TFBlocks.MANGROVE_GATE.value(), TFBlocks.DARK_GATE.value())
				.add(TFBlocks.TIME_GATE.value(), TFBlocks.TRANSFORMATION_GATE.value(), TFBlocks.MINING_GATE.value(), TFBlocks.SORTING_GATE.value());

		tag(BlockTags.WOODEN_SLABS)
				.add(TFBlocks.TWILIGHT_OAK_SLAB.value(), TFBlocks.CANOPY_SLAB.value(), TFBlocks.MANGROVE_SLAB.value(), TFBlocks.DARK_SLAB.value())
				.add(TFBlocks.TIME_SLAB.value(), TFBlocks.TRANSFORMATION_SLAB.value(), TFBlocks.MINING_SLAB.value(), TFBlocks.SORTING_SLAB.value());
		tag(BlockTags.SLABS)
				.add(TFBlocks.AURORA_SLAB.value());
		tag(BlockTags.WOODEN_STAIRS)
				.add(TFBlocks.TWILIGHT_OAK_STAIRS.value(), TFBlocks.CANOPY_STAIRS.value(), TFBlocks.MANGROVE_STAIRS.value(), TFBlocks.DARK_STAIRS.value())
				.add(TFBlocks.TIME_STAIRS.value(), TFBlocks.TRANSFORMATION_STAIRS.value(), TFBlocks.MINING_STAIRS.value(), TFBlocks.SORTING_STAIRS.value());
		tag(BlockTags.STAIRS)
				.add(TFBlocks.CASTLE_BRICK_STAIRS.value(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.value(), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.value(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.value(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.value(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.value())
				.add(TFBlocks.NAGASTONE_STAIRS_LEFT.value(), TFBlocks.NAGASTONE_STAIRS_RIGHT.value(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.value(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.value(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.value(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.value());

		tag(BlockTags.WOODEN_BUTTONS)
				.add(TFBlocks.TWILIGHT_OAK_BUTTON.value(), TFBlocks.CANOPY_BUTTON.value(), TFBlocks.MANGROVE_BUTTON.value(), TFBlocks.DARK_BUTTON.value())
				.add(TFBlocks.TIME_BUTTON.value(), TFBlocks.TRANSFORMATION_BUTTON.value(), TFBlocks.MINING_BUTTON.value(), TFBlocks.SORTING_BUTTON.value());
		tag(BlockTags.WOODEN_PRESSURE_PLATES)
				.add(TFBlocks.TWILIGHT_OAK_PLATE.value(), TFBlocks.CANOPY_PLATE.value(), TFBlocks.MANGROVE_PLATE.value(), TFBlocks.DARK_PLATE.value())
				.add(TFBlocks.TIME_PLATE.value(), TFBlocks.TRANSFORMATION_PLATE.value(), TFBlocks.MINING_PLATE.value(), TFBlocks.SORTING_PLATE.value());

		tag(BlockTags.WOODEN_TRAPDOORS)
				.add(TFBlocks.TWILIGHT_OAK_TRAPDOOR.value(), TFBlocks.CANOPY_TRAPDOOR.value(), TFBlocks.MANGROVE_TRAPDOOR.value(), TFBlocks.DARK_TRAPDOOR.value())
				.add(TFBlocks.TIME_TRAPDOOR.value(), TFBlocks.TRANSFORMATION_TRAPDOOR.value(), TFBlocks.MINING_TRAPDOOR.value(), TFBlocks.SORTING_TRAPDOOR.value());
		tag(BlockTags.WOODEN_DOORS)
				.add(TFBlocks.TWILIGHT_OAK_DOOR.value(), TFBlocks.CANOPY_DOOR.value(), TFBlocks.MANGROVE_DOOR.value(), TFBlocks.DARK_DOOR.value())
				.add(TFBlocks.TIME_DOOR.value(), TFBlocks.TRANSFORMATION_DOOR.value(), TFBlocks.MINING_DOOR.value(), TFBlocks.SORTING_DOOR.value());

		tag(Tags.Blocks.CHESTS_WOODEN)
				.add(TFBlocks.TWILIGHT_OAK_CHEST.value(), TFBlocks.CANOPY_CHEST.value(), TFBlocks.MANGROVE_CHEST.value(), TFBlocks.DARK_CHEST.value())
				.add(TFBlocks.TIME_CHEST.value(), TFBlocks.TRANSFORMATION_CHEST.value(), TFBlocks.MINING_CHEST.value(), TFBlocks.SORTING_CHEST.value());

		tag(BlockTags.FLOWER_POTS)
				.add(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.value(), TFBlocks.POTTED_CANOPY_SAPLING.value(), TFBlocks.POTTED_MANGROVE_SAPLING.value(), TFBlocks.POTTED_DARKWOOD_SAPLING.value(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.value())
				.add(TFBlocks.POTTED_HOLLOW_OAK_SAPLING.value(), TFBlocks.POTTED_TIME_SAPLING.value(), TFBlocks.POTTED_TRANSFORMATION_SAPLING.value(), TFBlocks.POTTED_MINING_SAPLING.value(), TFBlocks.POTTED_SORTING_SAPLING.value())
				.add(TFBlocks.POTTED_MAYAPPLE.value(), TFBlocks.POTTED_FIDDLEHEAD.value(), TFBlocks.POTTED_MUSHGLOOM.value(), TFBlocks.POTTED_THORN.value(), TFBlocks.POTTED_GREEN_THORN.value(), TFBlocks.POTTED_DEAD_THORN.value());

		tag(BANISTERS).add(
				TFBlocks.OAK_BANISTER.value(),
				TFBlocks.SPRUCE_BANISTER.value(),
				TFBlocks.BIRCH_BANISTER.value(),
				TFBlocks.JUNGLE_BANISTER.value(),
				TFBlocks.ACACIA_BANISTER.value(),
				TFBlocks.DARK_OAK_BANISTER.value(),
				TFBlocks.CRIMSON_BANISTER.value(),
				TFBlocks.WARPED_BANISTER.value(),
				TFBlocks.VANGROVE_BANISTER.value(),
				TFBlocks.BAMBOO_BANISTER.value(),
				TFBlocks.CHERRY_BANISTER.value(),

				TFBlocks.TWILIGHT_OAK_BANISTER.value(),
				TFBlocks.CANOPY_BANISTER.value(),
				TFBlocks.MANGROVE_BANISTER.value(),
				TFBlocks.DARK_BANISTER.value(),
				TFBlocks.TIME_BANISTER.value(),
				TFBlocks.TRANSFORMATION_BANISTER.value(),
				TFBlocks.MINING_BANISTER.value(),
				TFBlocks.SORTING_BANISTER.value()
		);

		tag(HOLLOW_LOGS_HORIZONTAL).add(
				TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.value(),
				TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.value(),
				TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.value(),
				TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.value()
		);

		tag(HOLLOW_LOGS_VERTICAL).add(
				TFBlocks.HOLLOW_OAK_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.value(),
				TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.value(),
				TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_DARK_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_TIME_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_MINING_LOG_VERTICAL.value(),
				TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.value()
		);

		tag(HOLLOW_LOGS_CLIMBABLE).add(
				TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.value(),
				TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.value(),
				TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.value(),
				TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.value()
		);

		tag(HOLLOW_LOGS).addTags(HOLLOW_LOGS_HORIZONTAL, HOLLOW_LOGS_VERTICAL, HOLLOW_LOGS_CLIMBABLE);

		tag(BlockTags.STRIDER_WARM_BLOCKS).add(TFBlocks.FIERY_BLOCK.value());
		tag(BlockTags.PORTALS).add(TFBlocks.TWILIGHT_PORTAL.value());
		tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(TFBlocks.CANOPY_BOOKSHELF.value());
		tag(BlockTags.REPLACEABLE_BY_TREES).add(
				TFBlocks.HARDENED_DARK_LEAVES.value(),
				TFBlocks.MAYAPPLE.value(),
				TFBlocks.FIDDLEHEAD.value(),
				TFBlocks.MOSS_PATCH.value(),
				TFBlocks.CLOVER_PATCH.value(),
				TFBlocks.MUSHGLOOM.value(),
				TFBlocks.FALLEN_LEAVES.value(),
				TFBlocks.TORCHBERRY_PLANT.value(),
				TFBlocks.ROOT_STRAND.value(),
				TFBlocks.ROOT_BLOCK.value());

		tag(BlockTags.CLIMBABLE).add(TFBlocks.IRON_LADDER.value(), TFBlocks.ROPE.value(), TFBlocks.ROOT_STRAND.value()).addTag(HOLLOW_LOGS_CLIMBABLE);

		tag(BlockTags.STANDING_SIGNS).add(
				TFBlocks.TWILIGHT_OAK_SIGN.value(), TFBlocks.CANOPY_SIGN.value(),
				TFBlocks.MANGROVE_SIGN.value(), TFBlocks.DARK_SIGN.value(),
				TFBlocks.TIME_SIGN.value(), TFBlocks.TRANSFORMATION_SIGN.value(),
				TFBlocks.MINING_SIGN.value(), TFBlocks.SORTING_SIGN.value());

		tag(BlockTags.WALL_SIGNS).add(
				TFBlocks.TWILIGHT_WALL_SIGN.value(), TFBlocks.CANOPY_WALL_SIGN.value(),
				TFBlocks.MANGROVE_WALL_SIGN.value(), TFBlocks.DARK_WALL_SIGN.value(),
				TFBlocks.TIME_WALL_SIGN.value(), TFBlocks.TRANSFORMATION_WALL_SIGN.value(),
				TFBlocks.MINING_WALL_SIGN.value(), TFBlocks.SORTING_WALL_SIGN.value());

		tag(BlockTags.CEILING_HANGING_SIGNS).add(
				TFBlocks.TWILIGHT_OAK_HANGING_SIGN.value(), TFBlocks.CANOPY_HANGING_SIGN.value(),
				TFBlocks.MANGROVE_HANGING_SIGN.value(), TFBlocks.DARK_HANGING_SIGN.value(),
				TFBlocks.TIME_HANGING_SIGN.value(), TFBlocks.TRANSFORMATION_HANGING_SIGN.value(),
				TFBlocks.MINING_HANGING_SIGN.value(), TFBlocks.SORTING_HANGING_SIGN.value());

		tag(BlockTags.WALL_HANGING_SIGNS).add(
				TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.value(), TFBlocks.CANOPY_WALL_HANGING_SIGN.value(),
				TFBlocks.MANGROVE_WALL_HANGING_SIGN.value(), TFBlocks.DARK_WALL_HANGING_SIGN.value(),
				TFBlocks.TIME_WALL_HANGING_SIGN.value(), TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.value(),
				TFBlocks.MINING_WALL_HANGING_SIGN.value(), TFBlocks.SORTING_WALL_HANGING_SIGN.value());
		
		tag(TOWERWOOD).add(TFBlocks.TOWERWOOD.value(), TFBlocks.MOSSY_TOWERWOOD.value(), TFBlocks.CRACKED_TOWERWOOD.value(), TFBlocks.INFESTED_TOWERWOOD.value());

		tag(MAZESTONE).add(
				TFBlocks.MAZESTONE.value(), TFBlocks.MAZESTONE_BRICK.value(),
				TFBlocks.CRACKED_MAZESTONE.value(), TFBlocks.MOSSY_MAZESTONE.value(),
				TFBlocks.CUT_MAZESTONE.value(), TFBlocks.DECORATIVE_MAZESTONE.value(),
				TFBlocks.MAZESTONE_MOSAIC.value(), TFBlocks.MAZESTONE_BORDER.value());

		tag(CASTLE_BLOCKS).add(
				TFBlocks.CASTLE_BRICK.value(), TFBlocks.WORN_CASTLE_BRICK.value(),
				TFBlocks.CRACKED_CASTLE_BRICK.value(), TFBlocks.MOSSY_CASTLE_BRICK.value(),
				TFBlocks.CASTLE_ROOF_TILE.value(), TFBlocks.THICK_CASTLE_BRICK.value(),
				TFBlocks.BOLD_CASTLE_BRICK_TILE.value(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value(),
				TFBlocks.ENCASED_CASTLE_BRICK_TILE.value(), TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.value(),
				TFBlocks.CASTLE_BRICK_STAIRS.value(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.value(),
				TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.value(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.value(),
				TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.value(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.value(),
				TFBlocks.PINK_CASTLE_RUNE_BRICK.value(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.value(),
				TFBlocks.BLUE_CASTLE_RUNE_BRICK.value(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.value(),
				TFBlocks.PINK_CASTLE_DOOR.value(), TFBlocks.YELLOW_CASTLE_DOOR.value(),
				TFBlocks.BLUE_CASTLE_DOOR.value(), TFBlocks.VIOLET_CASTLE_DOOR.value()
		);

		tag(MAZEBREAKER_ACCELERATED).addTag(MAZESTONE).addTag(CASTLE_BLOCKS);

		tag(STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.value());
		tag(STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.value());
		tag(STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.value());
		tag(STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.value());
		tag(STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.value());
		tag(STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.value());

		tag(BlockTags.BEACON_BASE_BLOCKS).addTags(
				STORAGE_BLOCKS_ARCTIC_FUR,
				STORAGE_BLOCKS_CARMINITE,
				STORAGE_BLOCKS_FIERY,
				STORAGE_BLOCKS_IRONWOOD,
				STORAGE_BLOCKS_KNIGHTMETAL,
				STORAGE_BLOCKS_STEELEAF
		);

		tag(Tags.Blocks.STORAGE_BLOCKS).addTags(STORAGE_BLOCKS_ARCTIC_FUR, STORAGE_BLOCKS_CARMINITE, STORAGE_BLOCKS_FIERY, STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_STEELEAF);

		tag(BlockTags.DIRT).add(TFBlocks.UBEROUS_SOIL.value());
		tag(PORTAL_EDGE).add(Blocks.FARMLAND, Blocks.DIRT_PATH).addTags(BlockTags.DIRT);
		// So yes, we could do fluid tags for the portal pool but the problem is that we're -replacing- the block, effectively replacing what would be waterlogged, with the portal block
		// In the future if we can "portal log" blocks then we can re-explore doing it as a fluid
		tag(PORTAL_POOL).add(Blocks.WATER);
		tag(PORTAL_DECO).add(
						Blocks.BAMBOO,
						Blocks.GRASS, Blocks.TALL_GRASS,
						Blocks.FERN, Blocks.LARGE_FERN,
						Blocks.DEAD_BUSH,
						Blocks.SUGAR_CANE,
						Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER,
						Blocks.SWEET_BERRY_BUSH,
						Blocks.NETHER_WART,
						Blocks.COCOA,
						Blocks.VINE, Blocks.GLOW_LICHEN,
						Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM,
						Blocks.WARPED_FUNGUS, Blocks.CRIMSON_FUNGUS,
						Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM,
						Blocks.MOSS_CARPET,
						Blocks.PINK_PETALS,
						Blocks.BIG_DRIPLEAF,
						Blocks.BIG_DRIPLEAF_STEM,
						Blocks.SMALL_DRIPLEAF,
						TFBlocks.FIDDLEHEAD.value(),
						TFBlocks.MOSS_PATCH.value(),
						TFBlocks.MAYAPPLE.value(),
						TFBlocks.CLOVER_PATCH.value(),
						TFBlocks.MUSHGLOOM.value(),
						TFBlocks.FALLEN_LEAVES.value(),
						TFBlocks.GIANT_LEAVES.value(),
						TFBlocks.STEELEAF_BLOCK.value(),
						TFBlocks.HARDENED_DARK_LEAVES.value())
				.addTags(BlockTags.FLOWERS, BlockTags.LEAVES, BlockTags.SAPLINGS, BlockTags.CROPS);

		tag(GENERATED_PORTAL_DECO)
				.add(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM,
						Blocks.GRASS, Blocks.FERN,
						Blocks.BLUE_ORCHID, Blocks.AZURE_BLUET,
						Blocks.LILY_OF_THE_VALLEY, Blocks.OXEYE_DAISY,
						Blocks.ALLIUM, Blocks.CORNFLOWER,
						Blocks.WHITE_TULIP, Blocks.PINK_TULIP,
						Blocks.ORANGE_TULIP, Blocks.RED_TULIP,
						TFBlocks.MUSHGLOOM.value(),
						TFBlocks.MAYAPPLE.value(),
						TFBlocks.FIDDLEHEAD.value());

		tag(DARK_TOWER_ALLOWED_POTS)
				.add(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.value(), TFBlocks.POTTED_CANOPY_SAPLING.value(), TFBlocks.POTTED_MANGROVE_SAPLING.value(),
						TFBlocks.POTTED_DARKWOOD_SAPLING.value(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.value(), TFBlocks.POTTED_MAYAPPLE.value(),
						TFBlocks.POTTED_FIDDLEHEAD.value(), TFBlocks.POTTED_MUSHGLOOM.value())
				.add(Blocks.FLOWER_POT, Blocks.POTTED_POPPY, Blocks.POTTED_BLUE_ORCHID, Blocks.POTTED_ALLIUM, Blocks.POTTED_AZURE_BLUET,
						Blocks.POTTED_RED_TULIP, Blocks.POTTED_ORANGE_TULIP, Blocks.POTTED_WHITE_TULIP, Blocks.POTTED_PINK_TULIP,
						Blocks.POTTED_OXEYE_DAISY, Blocks.POTTED_DANDELION, Blocks.POTTED_OAK_SAPLING, Blocks.POTTED_SPRUCE_SAPLING,
						Blocks.POTTED_BIRCH_SAPLING, Blocks.POTTED_JUNGLE_SAPLING, Blocks.POTTED_ACACIA_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING,
						Blocks.POTTED_RED_MUSHROOM, Blocks.POTTED_BROWN_MUSHROOM, Blocks.POTTED_DEAD_BUSH, Blocks.POTTED_FERN,
						Blocks.POTTED_CACTUS, Blocks.POTTED_CORNFLOWER, Blocks.POTTED_LILY_OF_THE_VALLEY, Blocks.POTTED_WITHER_ROSE,
						Blocks.POTTED_BAMBOO, Blocks.POTTED_CRIMSON_FUNGUS, Blocks.POTTED_WARPED_FUNGUS, Blocks.POTTED_CRIMSON_ROOTS,
						Blocks.POTTED_WARPED_ROOTS, Blocks.POTTED_AZALEA, Blocks.POTTED_FLOWERING_AZALEA, Blocks.POTTED_MANGROVE_PROPAGULE);

		tag(BlockTags.FROG_PREFER_JUMP_TO).add(TFBlocks.HUGE_LILY_PAD.value());

		tag(TROPHIES)
				.add(TFBlocks.NAGA_TROPHY.value(), TFBlocks.NAGA_WALL_TROPHY.value())
				.add(TFBlocks.LICH_TROPHY.value(), TFBlocks.LICH_WALL_TROPHY.value())
				.add(TFBlocks.MINOSHROOM_TROPHY.value(), TFBlocks.MINOSHROOM_WALL_TROPHY.value())
				.add(TFBlocks.HYDRA_TROPHY.value(), TFBlocks.HYDRA_WALL_TROPHY.value())
				.add(TFBlocks.KNIGHT_PHANTOM_TROPHY.value(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.value())
				.add(TFBlocks.UR_GHAST_TROPHY.value(), TFBlocks.UR_GHAST_WALL_TROPHY.value())
				.add(TFBlocks.ALPHA_YETI_TROPHY.value(), TFBlocks.ALPHA_YETI_WALL_TROPHY.value())
				.add(TFBlocks.SNOW_QUEEN_TROPHY.value(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.value())
				.add(TFBlocks.QUEST_RAM_TROPHY.value(), TFBlocks.QUEST_RAM_WALL_TROPHY.value());

		tag(FIRE_JET_FUEL).add(Blocks.LAVA);

		tag(ICE_BOMB_REPLACEABLES)
				.add(TFBlocks.MAYAPPLE.value(), TFBlocks.FIDDLEHEAD.value(), Blocks.GRASS, Blocks.TALL_GRASS, Blocks.FERN, Blocks.LARGE_FERN)
				.addTag(BlockTags.FLOWERS);

		tag(PLANTS_HANG_ON)
				.addTag(BlockTags.DIRT)
				.add(Blocks.MOSS_BLOCK, TFBlocks.MANGROVE_ROOT.value(), TFBlocks.ROOT_BLOCK.value(), TFBlocks.LIVEROOT_BLOCK.value());

		tag(COMMON_PROTECTIONS).add( // For any blocks that absolutely should not be meddled with
				TFBlocks.NAGA_BOSS_SPAWNER.value(),
				TFBlocks.LICH_BOSS_SPAWNER.value(),
				TFBlocks.MINOSHROOM_BOSS_SPAWNER.value(),
				TFBlocks.HYDRA_BOSS_SPAWNER.value(),
				TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.value(),
				TFBlocks.UR_GHAST_BOSS_SPAWNER.value(),
				TFBlocks.ALPHA_YETI_BOSS_SPAWNER.value(),
				TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.value(),
				TFBlocks.FINAL_BOSS_BOSS_SPAWNER.value(),
				TFBlocks.STRONGHOLD_SHIELD.value(),
				TFBlocks.UNBREAKABLE_VANISHING_BLOCK.value(),
				TFBlocks.LOCKED_VANISHING_BLOCK.value(),
				TFBlocks.PINK_FORCE_FIELD.value(),
				TFBlocks.ORANGE_FORCE_FIELD.value(),
				TFBlocks.GREEN_FORCE_FIELD.value(),
				TFBlocks.BLUE_FORCE_FIELD.value(),
				TFBlocks.VIOLET_FORCE_FIELD.value(),
				TFBlocks.KEEPSAKE_CASKET.value(),
				TFBlocks.TROPHY_PEDESTAL.value()
		).add( // [VanillaCopy] WITHER_IMMUNE - Do NOT include that tag in this tag
				Blocks.BARRIER,
				Blocks.BEDROCK,
				Blocks.END_PORTAL,
				Blocks.END_PORTAL_FRAME,
				Blocks.END_GATEWAY,
				Blocks.COMMAND_BLOCK,
				Blocks.REPEATING_COMMAND_BLOCK,
				Blocks.CHAIN_COMMAND_BLOCK,
				Blocks.STRUCTURE_BLOCK,
				Blocks.JIGSAW,
				Blocks.MOVING_PISTON,
				Blocks.LIGHT,
				Blocks.REINFORCED_DEEPSLATE
		);

		tag(BlockTags.DRAGON_IMMUNE).addTag(COMMON_PROTECTIONS).add(TFBlocks.GIANT_OBSIDIAN.value(), TFBlocks.FAKE_DIAMOND.value(), TFBlocks.FAKE_GOLD.value());

		tag(BlockTags.WITHER_IMMUNE).addTag(COMMON_PROTECTIONS).add(TFBlocks.FAKE_DIAMOND.value(), TFBlocks.FAKE_GOLD.value());

		tag(CARMINITE_REACTOR_IMMUNE).addTag(COMMON_PROTECTIONS);

		tag(CARMINITE_REACTOR_ORES).add(Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_GOLD_ORE);

		tag(ANNIHILATION_INCLUSIONS) // This is NOT a blacklist! This is a whitelist
				.add(Blocks.NETHER_PORTAL)
				.add(TFBlocks.DEADROCK.value(), TFBlocks.CRACKED_DEADROCK.value(), TFBlocks.WEATHERED_DEADROCK.value())
				.add(TFBlocks.CASTLE_BRICK.value(), TFBlocks.CRACKED_DEADROCK.value(), TFBlocks.THICK_CASTLE_BRICK.value(), TFBlocks.MOSSY_CASTLE_BRICK.value(), TFBlocks.CASTLE_ROOF_TILE.value(), TFBlocks.WORN_CASTLE_BRICK.value())
				.add(TFBlocks.BLUE_CASTLE_RUNE_BRICK.value(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.value(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.value(), TFBlocks.PINK_CASTLE_RUNE_BRICK.value())
				.add(TFBlocks.PINK_FORCE_FIELD.value(), TFBlocks.ORANGE_FORCE_FIELD.value(), TFBlocks.GREEN_FORCE_FIELD.value(), TFBlocks.BLUE_FORCE_FIELD.value(), TFBlocks.VIOLET_FORCE_FIELD.value())
				.add(TFBlocks.BROWN_THORNS.value(), TFBlocks.GREEN_THORNS.value());

		tag(ANTIBUILDER_IGNORES).add(
				Blocks.REDSTONE_LAMP,
				Blocks.TNT,
				Blocks.WATER,
				TFBlocks.ANTIBUILDER.value(),
				TFBlocks.CARMINITE_BUILDER.value(),
				TFBlocks.BUILT_BLOCK.value(),
				TFBlocks.REACTOR_DEBRIS.value(),
				TFBlocks.CARMINITE_REACTOR.value(),
				TFBlocks.REAPPEARING_BLOCK.value(),
				TFBlocks.GHAST_TRAP.value(),
				TFBlocks.FAKE_DIAMOND.value(),
				TFBlocks.FAKE_GOLD.value()
		).addTag(COMMON_PROTECTIONS).addOptional(new ResourceLocation("gravestone:gravestone"));

		tag(STRUCTURE_BANNED_INTERACTIONS).add(Blocks.LEVER).add(TFBlocks.ANTIBUILDER.value()).addTags(BlockTags.BUTTONS, Tags.Blocks.CHESTS);

		// TODO add more grave mods to this list 
		tag(PROGRESSION_ALLOW_BREAKING)
				.add(TFBlocks.KEEPSAKE_CASKET.value())
				.addOptional(new ResourceLocation("gravestone", "gravestone"));

		tag(ORE_MAGNET_SAFE_REPLACE_BLOCK).addTags(
				BlockTags.DIRT,
				Tags.Blocks.GRAVEL,
				Tags.Blocks.SAND,
				BlockTags.NYLIUM,
				BlockTags.BASE_STONE_OVERWORLD,
				BlockTags.BASE_STONE_NETHER,
				Tags.Blocks.END_STONES,
				BlockTags.DEEPSLATE_ORE_REPLACEABLES,
				BlockTags.STONE_ORE_REPLACEABLES,
				ROOT_GROUND
		);

		tag(ORE_MAGNET_IGNORE).addTag(BlockTags.COAL_ORES);

		tag(ROOT_GROUND).add(TFBlocks.ROOT_BLOCK.value());
		tag(ROOT_ORES).add(TFBlocks.LIVEROOT_BLOCK.value());

		tag(CLOUDS).add(TFBlocks.FLUFFY_CLOUD.value(), TFBlocks.WISPY_CLOUD.value(), TFBlocks.RAINY_CLOUD.value(), TFBlocks.SNOWY_CLOUD.value());

		tag(TF_CHESTS).add(
				TFBlocks.TWILIGHT_OAK_CHEST.value(),
				TFBlocks.CANOPY_CHEST.value(),
				TFBlocks.MANGROVE_CHEST.value(),
				TFBlocks.DARK_CHEST.value(),
				TFBlocks.TIME_CHEST.value(),
				TFBlocks.TRANSFORMATION_CHEST.value(),
				TFBlocks.MINING_CHEST.value(),
				TFBlocks.SORTING_CHEST.value());

		tag(BlockTags.DAMPENS_VIBRATIONS).addTag(CLOUDS).add(TFBlocks.ARCTIC_FUR_BLOCK.value());
		tag(BlockTags.OCCLUDES_VIBRATION_SIGNALS).add(TFBlocks.ARCTIC_FUR_BLOCK.value());

		tag(BlockTags.SMALL_DRIPLEAF_PLACEABLE).add(TFBlocks.UBEROUS_SOIL.value());

		tag(BlockTags.FEATURES_CANNOT_REPLACE).addTag(COMMON_PROTECTIONS).add(TFBlocks.LIVEROOT_BLOCK.value(), TFBlocks.MANGROVE_ROOT.value());
		// For anything that permits replacement during Worldgen
		tag(WORLDGEN_REPLACEABLES).addTags(BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.REPLACEABLE_BY_TREES);

		tag(ROOT_TRACE_SKIP).add(TFBlocks.ROOT_BLOCK.value(), TFBlocks.LIVEROOT_BLOCK.value(), TFBlocks.MANGROVE_ROOT.value(), TFBlocks.TIME_WOOD.value()).addTags(BlockTags.FEATURES_CANNOT_REPLACE);

		tag(DRUID_PROJECTILE_REPLACEABLE).addTags(BlockTags.LEAVES, BlockTags.LOGS, BlockTags.PLANKS, BlockTags.OVERWORLD_CARVER_REPLACEABLES, BlockTags.NETHER_CARVER_REPLACEABLES, BlockTags.REPLACEABLE_BY_TREES, BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.SCULK_REPLACEABLE, Tags.Blocks.ORES);

		tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(TFBlocks.TROLLSTEINN.value());

		tag(TIME_CORE_EXCLUDED).add(Blocks.NETHER_PORTAL);

		tag(PENGUINS_SPAWNABLE_ON).add(Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE);
		tag(GIANTS_SPAWNABLE_ON).addTag(CLOUDS);

		tag(BlockTags.MINEABLE_WITH_AXE).add(
				TFBlocks.HEDGE.value(),
				TFBlocks.ROOT_BLOCK.value(),
				TFBlocks.LIVEROOT_BLOCK.value(),
				TFBlocks.MANGROVE_ROOT.value(),
				TFBlocks.UNCRAFTING_TABLE.value(),
				TFBlocks.ENCASED_SMOKER.value(),
				TFBlocks.ENCASED_FIRE_JET.value(),
				TFBlocks.TIME_LOG_CORE.value(),
				TFBlocks.TRANSFORMATION_LOG_CORE.value(),
				TFBlocks.MINING_LOG_CORE.value(),
				TFBlocks.SORTING_LOG_CORE.value(),
				TFBlocks.REAPPEARING_BLOCK.value(),
				TFBlocks.VANISHING_BLOCK.value(),
				TFBlocks.ANTIBUILDER.value(),
				TFBlocks.CARMINITE_REACTOR.value(),
				TFBlocks.CARMINITE_BUILDER.value(),
				TFBlocks.GHAST_TRAP.value(),
				TFBlocks.HUGE_STALK.value(),
				TFBlocks.HUGE_MUSHGLOOM.value(),
				TFBlocks.HUGE_MUSHGLOOM_STEM.value(),
				TFBlocks.CINDER_LOG.value(),
				TFBlocks.CINDER_WOOD.value(),
				TFBlocks.IRONWOOD_BLOCK.value(),
				TFBlocks.DEATH_TOME_SPAWNER.value(),
				TFBlocks.EMPTY_CANOPY_BOOKSHELF.value(),
				TFBlocks.CANOPY_BOOKSHELF.value(),
				TFBlocks.TWILIGHT_OAK_CHEST.value(),
				TFBlocks.CANOPY_CHEST.value(),
				TFBlocks.MANGROVE_CHEST.value(),
				TFBlocks.DARK_CHEST.value(),
				TFBlocks.TIME_CHEST.value(),
				TFBlocks.TRANSFORMATION_CHEST.value(),
				TFBlocks.MINING_CHEST.value(),
				TFBlocks.SORTING_CHEST.value()
		).addTags(BANISTERS, HOLLOW_LOGS, TOWERWOOD);

		tag(BlockTags.MINEABLE_WITH_HOE).add(
				//vanilla doesnt use the leaves tag
				TFBlocks.TWILIGHT_OAK_LEAVES.value(),
				TFBlocks.CANOPY_LEAVES.value(),
				TFBlocks.MANGROVE_LEAVES.value(),
				TFBlocks.DARK_LEAVES.value(),
				TFBlocks.RAINBOW_OAK_LEAVES.value(),
				TFBlocks.TIME_LEAVES.value(),
				TFBlocks.TRANSFORMATION_LEAVES.value(),
				TFBlocks.MINING_LEAVES.value(),
				TFBlocks.SORTING_LEAVES.value(),
				TFBlocks.THORN_LEAVES.value(),
				TFBlocks.THORN_ROSE.value(),
				TFBlocks.BEANSTALK_LEAVES.value(),
				TFBlocks.STEELEAF_BLOCK.value(),
				TFBlocks.ARCTIC_FUR_BLOCK.value()
		);

		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
				TFBlocks.NAGASTONE.value(),
				TFBlocks.NAGASTONE_HEAD.value(),
				TFBlocks.STRONGHOLD_SHIELD.value(),
				TFBlocks.TROPHY_PEDESTAL.value(),
				TFBlocks.AURORA_PILLAR.value(),
				TFBlocks.AURORA_SLAB.value(),
				TFBlocks.UNDERBRICK.value(),
				TFBlocks.MOSSY_UNDERBRICK.value(),
				TFBlocks.CRACKED_UNDERBRICK.value(),
				TFBlocks.UNDERBRICK_FLOOR.value(),
				TFBlocks.DEADROCK.value(),
				TFBlocks.CRACKED_DEADROCK.value(),
				TFBlocks.WEATHERED_DEADROCK.value(),
				TFBlocks.TROLLSTEINN.value(),
				TFBlocks.GIANT_LEAVES.value(),
				TFBlocks.GIANT_OBSIDIAN.value(),
				TFBlocks.GIANT_COBBLESTONE.value(),
				TFBlocks.GIANT_LOG.value(),
				TFBlocks.CINDER_FURNACE.value(),
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.value(),
				//TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE.value(),
				//TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE.value(),
				//TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE.value(),
				//TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE.value(),
				TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.value(),
				TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.value(),
				//TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.value(),
				//TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.value(),
				//TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.value(),
				//TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.value(),
				//TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE.value(),
				//TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE.value(),
				//TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE.value(),
				//TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE.value(),
				TFBlocks.KNIGHTMETAL_BLOCK.value(),
				TFBlocks.IRONWOOD_BLOCK.value(),
				TFBlocks.FIERY_BLOCK.value(),
				TFBlocks.SPIRAL_BRICKS.value(),
				TFBlocks.ETCHED_NAGASTONE.value(),
				TFBlocks.NAGASTONE_PILLAR.value(),
				TFBlocks.NAGASTONE_STAIRS_LEFT.value(),
				TFBlocks.NAGASTONE_STAIRS_RIGHT.value(),
				TFBlocks.MOSSY_ETCHED_NAGASTONE.value(),
				TFBlocks.MOSSY_NAGASTONE_PILLAR.value(),
				TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.value(),
				TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.value(),
				TFBlocks.CRACKED_ETCHED_NAGASTONE.value(),
				TFBlocks.CRACKED_NAGASTONE_PILLAR.value(),
				TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.value(),
				TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.value(),
				TFBlocks.IRON_LADDER.value(),
				TFBlocks.TWISTED_STONE.value(),
				TFBlocks.TWISTED_STONE_PILLAR.value(),
				TFBlocks.KEEPSAKE_CASKET.value(),
				TFBlocks.BOLD_STONE_PILLAR.value()
		).addTags(MAZESTONE, CASTLE_BLOCKS);

		tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
				TFBlocks.SMOKER.value(),
				TFBlocks.FIRE_JET.value(),
				TFBlocks.UBEROUS_SOIL.value()
		);

		tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(
				TFBlocks.NAGASTONE.value(),
				TFBlocks.NAGASTONE_HEAD.value(),
				TFBlocks.ETCHED_NAGASTONE.value(),
				TFBlocks.CRACKED_ETCHED_NAGASTONE.value(),
				TFBlocks.MOSSY_ETCHED_NAGASTONE.value(),
				TFBlocks.NAGASTONE_PILLAR.value(),
				TFBlocks.CRACKED_NAGASTONE_PILLAR.value(),
				TFBlocks.MOSSY_NAGASTONE_PILLAR.value(),
				TFBlocks.NAGASTONE_STAIRS_LEFT.value(),
				TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.value(),
				TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.value(),
				TFBlocks.NAGASTONE_STAIRS_RIGHT.value(),
				TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.value(),
				TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.value(),
				TFBlocks.SPIRAL_BRICKS.value(),
				TFBlocks.TWISTED_STONE.value(),
				TFBlocks.TWISTED_STONE_PILLAR.value(),
				TFBlocks.BOLD_STONE_PILLAR.value(),
				TFBlocks.AURORA_PILLAR.value(),
				TFBlocks.AURORA_SLAB.value(),
				TFBlocks.TROLLSTEINN.value()
		);

		tag(BlockTags.NEEDS_STONE_TOOL).add(
				TFBlocks.UNDERBRICK.value(),
				TFBlocks.CRACKED_UNDERBRICK.value(),
				TFBlocks.MOSSY_UNDERBRICK.value(),
				TFBlocks.UNDERBRICK_FLOOR.value(),
				TFBlocks.IRON_LADDER.value()
		);

		tag(BlockTags.NEEDS_IRON_TOOL).add(
				TFBlocks.FIERY_BLOCK.value(),
				TFBlocks.KNIGHTMETAL_BLOCK.value()
		);

		tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
				TFBlocks.AURORA_BLOCK.value(),
				TFBlocks.DEADROCK.value(),
				TFBlocks.CRACKED_DEADROCK.value(),
				TFBlocks.WEATHERED_DEADROCK.value()
		).addTags(CASTLE_BLOCKS, MAZESTONE);

		tag(BlockTags.MUSHROOM_GROW_BLOCK).add(TFBlocks.UBEROUS_SOIL.value());

		tag(BlockTags.MOSS_REPLACEABLE).add(
				TFBlocks.ROOT_BLOCK.value(),
				TFBlocks.LIVEROOT_BLOCK.value(),
				TFBlocks.TROLLSTEINN.value()
		);

		tag(BlockTags.INVALID_SPAWN_INSIDE).add(TFBlocks.TWILIGHT_PORTAL.value());

		tag(RELOCATION_NOT_SUPPORTED).add(TFBlocks.TWILIGHT_PORTAL.value(), TFBlocks.STRONGHOLD_SHIELD.value(),
				TFBlocks.TIME_LOG_CORE.value(), TFBlocks.TRANSFORMATION_LOG_CORE.value(),
				TFBlocks.MINING_LOG_CORE.value(), TFBlocks.SORTING_LOG_CORE.value(),
				TFBlocks.ANTIBUILDER.value(), TFBlocks.BUILT_BLOCK.value(),
				TFBlocks.FAKE_DIAMOND.value(), TFBlocks.FAKE_GOLD.value(),
				TFBlocks.REACTOR_DEBRIS.value(), TFBlocks.LOCKED_VANISHING_BLOCK.value(), TFBlocks.VANISHING_BLOCK.value(),
				TFBlocks.UNBREAKABLE_VANISHING_BLOCK.value(), TFBlocks.REAPPEARING_BLOCK.value(),
				TFBlocks.BEANSTALK_GROWER.value(), TFBlocks.GIANT_COBBLESTONE.value(),
				TFBlocks.GIANT_LOG.value(), TFBlocks.GIANT_LEAVES.value(),
				TFBlocks.GIANT_OBSIDIAN.value(), TFBlocks.BROWN_THORNS.value(),
				TFBlocks.GREEN_THORNS.value(), TFBlocks.BURNT_THORNS.value(),
				TFBlocks.PINK_FORCE_FIELD.value(), TFBlocks.ORANGE_FORCE_FIELD.value(),
				TFBlocks.GREEN_FORCE_FIELD.value(), TFBlocks.BLUE_FORCE_FIELD.value(),
				TFBlocks.VIOLET_FORCE_FIELD.value(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.value(),
				TFBlocks.NAGA_BOSS_SPAWNER.value(), TFBlocks.LICH_BOSS_SPAWNER.value(),
				TFBlocks.MINOSHROOM_BOSS_SPAWNER.value(), TFBlocks.HYDRA_BOSS_SPAWNER.value(),
				TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.value(), TFBlocks.UR_GHAST_BOSS_SPAWNER.value(),
				TFBlocks.ALPHA_YETI_BOSS_SPAWNER.value(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.value());

		tag(IMMOVABLE).add(TFBlocks.TWILIGHT_PORTAL.value(), TFBlocks.STRONGHOLD_SHIELD.value(),
				TFBlocks.TIME_LOG_CORE.value(), TFBlocks.TRANSFORMATION_LOG_CORE.value(),
				TFBlocks.MINING_LOG_CORE.value(), TFBlocks.SORTING_LOG_CORE.value(),
				TFBlocks.ANTIBUILDER.value(), TFBlocks.BUILT_BLOCK.value(),
				TFBlocks.FAKE_DIAMOND.value(), TFBlocks.FAKE_GOLD.value(),
				TFBlocks.REACTOR_DEBRIS.value(), TFBlocks.LOCKED_VANISHING_BLOCK.value(), TFBlocks.VANISHING_BLOCK.value(),
				TFBlocks.UNBREAKABLE_VANISHING_BLOCK.value(), TFBlocks.REAPPEARING_BLOCK.value(),
				TFBlocks.BEANSTALK_GROWER.value(), TFBlocks.GIANT_COBBLESTONE.value(),
				TFBlocks.GIANT_LOG.value(), TFBlocks.GIANT_LEAVES.value(),
				TFBlocks.GIANT_OBSIDIAN.value(), TFBlocks.BROWN_THORNS.value(),
				TFBlocks.GREEN_THORNS.value(), TFBlocks.BURNT_THORNS.value(),
				TFBlocks.PINK_FORCE_FIELD.value(), TFBlocks.ORANGE_FORCE_FIELD.value(),
				TFBlocks.GREEN_FORCE_FIELD.value(), TFBlocks.BLUE_FORCE_FIELD.value(),
				TFBlocks.VIOLET_FORCE_FIELD.value(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.value(),
				TFBlocks.NAGA_BOSS_SPAWNER.value(), TFBlocks.LICH_BOSS_SPAWNER.value(),
				TFBlocks.MINOSHROOM_BOSS_SPAWNER.value(), TFBlocks.HYDRA_BOSS_SPAWNER.value(),
				TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.value(), TFBlocks.UR_GHAST_BOSS_SPAWNER.value(),
				TFBlocks.ALPHA_YETI_BOSS_SPAWNER.value(), TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.value());
	}

	private static Block[] getAllMinecraftOrTwilightBlocks(Predicate<Block> predicate) {
		return BuiltInRegistries.BLOCK.stream()
				.filter(b -> BuiltInRegistries.BLOCK.getKey(b) != null && (BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals(TwilightForestMod.ID) || BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals("minecraft")) && predicate.test(b))
				.toArray(Block[]::new);
	}

	private static final Set<Block> plants;

	static {
		plants = ImmutableSet.<Block>builder().add(
				Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY, Blocks.WITHER_ROSE, // BlockTags.SMALL_FLOWERS
				Blocks.SUNFLOWER, Blocks.LILAC, Blocks.PEONY, Blocks.ROSE_BUSH, // BlockTags.TALL_FLOWERS
				Blocks.FLOWERING_AZALEA_LEAVES, Blocks.FLOWERING_AZALEA, // BlockTags.FLOWERS
				Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES, // BlockTags.LEAVES
				Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.AZALEA, Blocks.FLOWERING_AZALEA, // BlockTags.SAPLINGS
				Blocks.BEETROOTS, Blocks.CARROTS, Blocks.POTATOES, Blocks.WHEAT, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM // BlockTags.CROPS
		).add( // TF addons of above taglists
				TFBlocks.TWILIGHT_OAK_SAPLING.value(),
				TFBlocks.CANOPY_SAPLING.value(),
				TFBlocks.MANGROVE_SAPLING.value(),
				TFBlocks.DARKWOOD_SAPLING.value(),
				TFBlocks.TIME_SAPLING.value(),
				TFBlocks.TRANSFORMATION_SAPLING.value(),
				TFBlocks.MINING_SAPLING.value(),
				TFBlocks.SORTING_SAPLING.value(),
				TFBlocks.HOLLOW_OAK_SAPLING.value(),
				TFBlocks.RAINBOW_OAK_SAPLING.value(),
				TFBlocks.RAINBOW_OAK_LEAVES.value(),
				TFBlocks.TWILIGHT_OAK_LEAVES.value(),
				TFBlocks.CANOPY_LEAVES.value(),
				TFBlocks.MANGROVE_LEAVES.value(),
				TFBlocks.DARK_LEAVES.value(),
				TFBlocks.TIME_LEAVES.value(),
				TFBlocks.TRANSFORMATION_LEAVES.value(),
				TFBlocks.MINING_LEAVES.value(),
				TFBlocks.SORTING_LEAVES.value(),
				TFBlocks.THORN_LEAVES.value(),
				TFBlocks.BEANSTALK_LEAVES.value()
		).build();
	}

	@Override
	public String getName() {
		return "Twilight Forest Block Tags";
	}
}
