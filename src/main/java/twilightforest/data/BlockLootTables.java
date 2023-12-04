package twilightforest.data;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.block.*;
import twilightforest.enums.HollowLogVariants;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.Set;
import java.util.stream.Collectors;

public class BlockLootTables extends BlockLootSubProvider {
	// [VanillaCopy] of BlockLoot fields, just changed shears to work with modded ones
	private static final float[] DEFAULT_SAPLING_DROP_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
	private static final float[] RARE_SAPLING_DROP_RATES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};
	private static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS));

	public BlockLootTables() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() {
		dropSelf(TFBlocks.TOWERWOOD.value());
		dropSelf(TFBlocks.ENCASED_TOWERWOOD.value());
		dropSelf(TFBlocks.CRACKED_TOWERWOOD.value());
		dropSelf(TFBlocks.MOSSY_TOWERWOOD.value());
		dropSelf(TFBlocks.CARMINITE_BUILDER.value());
		dropSelf(TFBlocks.GHAST_TRAP.value());
		dropSelf(TFBlocks.CARMINITE_REACTOR.value());
		dropSelf(TFBlocks.REAPPEARING_BLOCK.value());
		dropSelf(TFBlocks.VANISHING_BLOCK.value());
		dropSelf(TFBlocks.LOCKED_VANISHING_BLOCK.value());
		dropSelf(TFBlocks.FIREFLY.value());
		dropSelf(TFBlocks.CICADA.value());
		dropSelf(TFBlocks.MOONWORM.value());
		dropSelf(TFBlocks.TROPHY_PEDESTAL.value());
		dropSelf(TFBlocks.AURORA_BLOCK.value());
		dropSelf(TFBlocks.AURORA_PILLAR.value());
		add(TFBlocks.AURORA_SLAB.value(), createSlabItemTable(TFBlocks.AURORA_SLAB.value()));
		dropWhenSilkTouch(TFBlocks.AURORALIZED_GLASS.value());
		dropSelf(TFBlocks.UNDERBRICK.value());
		dropSelf(TFBlocks.CRACKED_UNDERBRICK.value());
		dropSelf(TFBlocks.MOSSY_UNDERBRICK.value());
		dropSelf(TFBlocks.UNDERBRICK_FLOOR.value());
		dropSelf(TFBlocks.THORN_ROSE.value());
		add(TFBlocks.THORN_LEAVES.value(), silkAndStick(TFBlocks.THORN_LEAVES.value(), Items.STICK, RARE_SAPLING_DROP_RATES));
		add(TFBlocks.BEANSTALK_LEAVES.value(), silkAndStick(TFBlocks.BEANSTALK_LEAVES.value(), TFItems.MAGIC_BEANS.value(), RARE_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.DEADROCK.value());
		dropSelf(TFBlocks.CRACKED_DEADROCK.value());
		dropSelf(TFBlocks.WEATHERED_DEADROCK.value());
		dropSelf(TFBlocks.TROLLSTEINN.value());
		dropWhenSilkTouch(TFBlocks.WISPY_CLOUD.value());
		dropSelf(TFBlocks.FLUFFY_CLOUD.value());
		dropSelf(TFBlocks.RAINY_CLOUD.value());
		dropSelf(TFBlocks.SNOWY_CLOUD.value());
		dropSelf(TFBlocks.GIANT_COBBLESTONE.value());
		dropSelf(TFBlocks.GIANT_LOG.value());
		dropSelf(TFBlocks.GIANT_LEAVES.value());
		dropSelf(TFBlocks.GIANT_OBSIDIAN.value());
		add(TFBlocks.UBEROUS_SOIL.value(), createSingleItemTable(Blocks.DIRT));
		dropSelf(TFBlocks.HUGE_STALK.value());
		add(TFBlocks.HUGE_MUSHGLOOM.value(), createMushroomBlockDrop(TFBlocks.HUGE_MUSHGLOOM.value(), TFBlocks.MUSHGLOOM.value()));
		add(TFBlocks.HUGE_MUSHGLOOM_STEM.value(), createMushroomBlockDrop(TFBlocks.HUGE_MUSHGLOOM_STEM.value(), TFBlocks.MUSHGLOOM.value()));
		add(TFBlocks.TROLLVIDR.value(), createShearsOnlyDrop(TFBlocks.TROLLVIDR.value()));
		add(TFBlocks.UNRIPE_TROLLBER.value(), createShearsOnlyDrop(TFBlocks.UNRIPE_TROLLBER.value()));
		add(TFBlocks.TROLLBER.value(), createShearsDispatchTable(TFBlocks.TROLLBER.value(), LootItem.lootTableItem(TFItems.TORCHBERRIES.value()).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
		dropSelf(TFBlocks.HUGE_LILY_PAD.value());
		dropSelf(TFBlocks.HUGE_WATER_LILY.value());
		dropSelf(TFBlocks.CASTLE_BRICK.value());
		dropSelf(TFBlocks.WORN_CASTLE_BRICK.value());
		dropSelf(TFBlocks.CRACKED_CASTLE_BRICK.value());
		dropSelf(TFBlocks.MOSSY_CASTLE_BRICK.value());
		dropSelf(TFBlocks.THICK_CASTLE_BRICK.value());
		dropSelf(TFBlocks.CASTLE_ROOF_TILE.value());
		dropSelf(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.value());
		dropSelf(TFBlocks.ENCASED_CASTLE_BRICK_TILE.value());
		dropSelf(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value());
		dropSelf(TFBlocks.BOLD_CASTLE_BRICK_TILE.value());
		dropSelf(TFBlocks.CASTLE_BRICK_STAIRS.value());
		dropSelf(TFBlocks.WORN_CASTLE_BRICK_STAIRS.value());
		dropSelf(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.value());
		dropSelf(TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.value());
		dropSelf(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.value());
		dropSelf(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.value());
		dropSelf(TFBlocks.VIOLET_CASTLE_RUNE_BRICK.value());
		dropSelf(TFBlocks.YELLOW_CASTLE_RUNE_BRICK.value());
		dropSelf(TFBlocks.PINK_CASTLE_RUNE_BRICK.value());
		dropSelf(TFBlocks.BLUE_CASTLE_RUNE_BRICK.value());
		dropSelf(TFBlocks.CINDER_FURNACE.value());
		dropSelf(TFBlocks.CINDER_LOG.value());
		dropSelf(TFBlocks.CINDER_WOOD.value());
		dropSelf(TFBlocks.VIOLET_CASTLE_DOOR.value());
		dropSelf(TFBlocks.YELLOW_CASTLE_DOOR.value());
		dropSelf(TFBlocks.PINK_CASTLE_DOOR.value());
		dropSelf(TFBlocks.BLUE_CASTLE_DOOR.value());
		dropSelf(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.value());
		dropSelf(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.value());
		dropSelf(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.value());
		dropSelf(TFBlocks.KNIGHTMETAL_BLOCK.value());
		dropSelf(TFBlocks.IRONWOOD_BLOCK.value());
		dropSelf(TFBlocks.FIERY_BLOCK.value());
		dropSelf(TFBlocks.STEELEAF_BLOCK.value());
		dropSelf(TFBlocks.ARCTIC_FUR_BLOCK.value());
		dropSelf(TFBlocks.CARMINITE_BLOCK.value());
		dropSelf(TFBlocks.MAZESTONE.value());
		dropSelf(TFBlocks.MAZESTONE_BRICK.value());
		dropSelf(TFBlocks.CUT_MAZESTONE.value());
		dropSelf(TFBlocks.DECORATIVE_MAZESTONE.value());
		dropSelf(TFBlocks.CRACKED_MAZESTONE.value());
		dropSelf(TFBlocks.MOSSY_MAZESTONE.value());
		dropSelf(TFBlocks.MAZESTONE_MOSAIC.value());
		dropSelf(TFBlocks.MAZESTONE_BORDER.value());
		add(TFBlocks.RED_THREAD.value(), redThread());
		dropWhenSilkTouch(TFBlocks.HEDGE.value());
		add(TFBlocks.ROOT_BLOCK.value(), createSingleItemTableWithSilkTouch(TFBlocks.ROOT_BLOCK.value(), Items.STICK, UniformGenerator.between(3, 5)));
		add(TFBlocks.LIVEROOT_BLOCK.value(), createSilkTouchDispatchTable(TFBlocks.LIVEROOT_BLOCK.value(), applyExplosionCondition(TFBlocks.LIVEROOT_BLOCK.value(), LootItem.lootTableItem(TFItems.LIVEROOT.value()).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
		add(TFBlocks.MANGROVE_ROOT.value(), createSingleItemTableWithSilkTouch(TFBlocks.MANGROVE_ROOT.value(), Items.STICK, UniformGenerator.between(3, 5)));
		dropSelf(TFBlocks.UNCRAFTING_TABLE.value());
		dropSelf(TFBlocks.FIREFLY_JAR.value());
		add(TFBlocks.FIREFLY_SPAWNER.value(), particleSpawner());
		dropSelf(TFBlocks.CICADA_JAR.value());
		add(TFBlocks.MOSS_PATCH.value(), createShearsOnlyDrop(TFBlocks.MOSS_PATCH.value()));
		add(TFBlocks.MAYAPPLE.value(), createShearsOnlyDrop(TFBlocks.MAYAPPLE.value()));
		add(TFBlocks.CLOVER_PATCH.value(), createShearsOnlyDrop(TFBlocks.CLOVER_PATCH.value()));
		add(TFBlocks.FIDDLEHEAD.value(), createShearsOnlyDrop(TFBlocks.FIDDLEHEAD.value()));
		dropSelf(TFBlocks.MUSHGLOOM.value());
		add(TFBlocks.TORCHBERRY_PLANT.value(), torchberryPlant(TFBlocks.TORCHBERRY_PLANT.value()));
		add(TFBlocks.ROOT_STRAND.value(), block -> createShearsDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
		add(TFBlocks.FALLEN_LEAVES.value(), this.fallenLeaves());
		dropSelf(TFBlocks.SMOKER.value());
		dropSelf(TFBlocks.ENCASED_SMOKER.value());
		dropSelf(TFBlocks.FIRE_JET.value());
		dropSelf(TFBlocks.ENCASED_FIRE_JET.value());
		dropSelf(TFBlocks.NAGASTONE_HEAD.value());
		dropSelf(TFBlocks.NAGASTONE.value());
		dropSelf(TFBlocks.SPIRAL_BRICKS.value());
		dropSelf(TFBlocks.NAGASTONE_PILLAR.value());
		dropSelf(TFBlocks.MOSSY_NAGASTONE_PILLAR.value());
		dropSelf(TFBlocks.CRACKED_NAGASTONE_PILLAR.value());
		dropSelf(TFBlocks.ETCHED_NAGASTONE.value());
		dropSelf(TFBlocks.MOSSY_ETCHED_NAGASTONE.value());
		dropSelf(TFBlocks.CRACKED_ETCHED_NAGASTONE.value());
		dropSelf(TFBlocks.NAGASTONE_STAIRS_LEFT.value());
		dropSelf(TFBlocks.NAGASTONE_STAIRS_RIGHT.value());
		dropSelf(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.value());
		dropSelf(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.value());
		dropSelf(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.value());
		dropSelf(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.value());
		add(TFBlocks.NAGA_TROPHY.value(), createSingleItemTable(TFBlocks.NAGA_TROPHY.value().asItem()));
		add(TFBlocks.NAGA_WALL_TROPHY.value(), createSingleItemTable(TFBlocks.NAGA_TROPHY.value().asItem()));
		add(TFBlocks.LICH_TROPHY.value(), createSingleItemTable(TFBlocks.LICH_TROPHY.value().asItem()));
		add(TFBlocks.LICH_WALL_TROPHY.value(), createSingleItemTable(TFBlocks.LICH_TROPHY.value().asItem()));
		add(TFBlocks.MINOSHROOM_TROPHY.value(), createSingleItemTable(TFBlocks.MINOSHROOM_TROPHY.value().asItem()));
		add(TFBlocks.MINOSHROOM_WALL_TROPHY.value(), createSingleItemTable(TFBlocks.MINOSHROOM_TROPHY.value().asItem()));
		add(TFBlocks.HYDRA_TROPHY.value(), createSingleItemTable(TFBlocks.HYDRA_TROPHY.value().asItem()));
		add(TFBlocks.HYDRA_WALL_TROPHY.value(), createSingleItemTable(TFBlocks.HYDRA_TROPHY.value().asItem()));
		add(TFBlocks.KNIGHT_PHANTOM_TROPHY.value(), createSingleItemTable(TFBlocks.KNIGHT_PHANTOM_TROPHY.value().asItem()));
		add(TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.value(), createSingleItemTable(TFBlocks.KNIGHT_PHANTOM_TROPHY.value().asItem()));
		add(TFBlocks.UR_GHAST_TROPHY.value(), createSingleItemTable(TFBlocks.UR_GHAST_TROPHY.value().asItem()));
		add(TFBlocks.UR_GHAST_WALL_TROPHY.value(), createSingleItemTable(TFBlocks.UR_GHAST_TROPHY.value().asItem()));
		add(TFBlocks.ALPHA_YETI_TROPHY.value(), createSingleItemTable(TFBlocks.ALPHA_YETI_TROPHY.value().asItem()));
		add(TFBlocks.ALPHA_YETI_WALL_TROPHY.value(), createSingleItemTable(TFBlocks.ALPHA_YETI_TROPHY.value().asItem()));
		add(TFBlocks.SNOW_QUEEN_TROPHY.value(), createSingleItemTable(TFBlocks.SNOW_QUEEN_TROPHY.value().asItem()));
		add(TFBlocks.SNOW_QUEEN_WALL_TROPHY.value(), createSingleItemTable(TFBlocks.SNOW_QUEEN_TROPHY.value().asItem()));
		add(TFBlocks.QUEST_RAM_TROPHY.value(), createSingleItemTable(TFBlocks.QUEST_RAM_TROPHY.value().asItem()));
		add(TFBlocks.QUEST_RAM_WALL_TROPHY.value(), createSingleItemTable(TFBlocks.QUEST_RAM_TROPHY.value().asItem()));

		add(TFBlocks.ZOMBIE_SKULL_CANDLE.value(), createSingleItemTable(Blocks.ZOMBIE_HEAD));
		add(TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.value(), createSingleItemTable(Blocks.ZOMBIE_HEAD));
		add(TFBlocks.SKELETON_SKULL_CANDLE.value(), createSingleItemTable(Blocks.SKELETON_SKULL));
		add(TFBlocks.SKELETON_WALL_SKULL_CANDLE.value(), createSingleItemTable(Blocks.SKELETON_SKULL));
		add(TFBlocks.WITHER_SKELE_SKULL_CANDLE.value(), createSingleItemTable(Blocks.WITHER_SKELETON_SKULL));
		add(TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.value(), createSingleItemTable(Blocks.WITHER_SKELETON_SKULL));
		add(TFBlocks.CREEPER_SKULL_CANDLE.value(), createSingleItemTable(Blocks.CREEPER_HEAD));
		add(TFBlocks.CREEPER_WALL_SKULL_CANDLE.value(), createSingleItemTable(Blocks.CREEPER_HEAD));
		add(TFBlocks.PLAYER_SKULL_CANDLE.value(), createSingleItemTable(Blocks.PLAYER_HEAD).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("SkullOwner", "SkullOwner")));
		add(TFBlocks.PLAYER_WALL_SKULL_CANDLE.value(), createSingleItemTable(Blocks.PLAYER_HEAD).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("SkullOwner", "SkullOwner")));
		add(TFBlocks.PIGLIN_SKULL_CANDLE.value(), createSingleItemTable(Blocks.PIGLIN_HEAD));
		add(TFBlocks.PIGLIN_WALL_SKULL_CANDLE.value(), createSingleItemTable(Blocks.PIGLIN_HEAD));

		dropSelf(TFBlocks.IRON_LADDER.value());
		dropSelf(TFBlocks.ROPE.value());
		dropSelf(TFBlocks.TWISTED_STONE.value());
		dropSelf(TFBlocks.TWISTED_STONE_PILLAR.value());
		dropSelf(TFBlocks.BOLD_STONE_PILLAR.value());
		dropWhenSilkTouch(TFBlocks.EMPTY_CANOPY_BOOKSHELF.value());
		add(TFBlocks.KEEPSAKE_CASKET.value(), casketInfo(TFBlocks.KEEPSAKE_CASKET.value()));
		dropSelf(TFBlocks.CANDELABRA.value());
		dropSelf(TFBlocks.WROUGHT_IRON_FENCE.value());
		dropPottedContents(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.value());
		dropPottedContents(TFBlocks.POTTED_CANOPY_SAPLING.value());
		dropPottedContents(TFBlocks.POTTED_MANGROVE_SAPLING.value());
		dropPottedContents(TFBlocks.POTTED_DARKWOOD_SAPLING.value());
		dropPottedContents(TFBlocks.POTTED_HOLLOW_OAK_SAPLING.value());
		dropPottedContents(TFBlocks.POTTED_RAINBOW_OAK_SAPLING.value());
		dropPottedContents(TFBlocks.POTTED_TIME_SAPLING.value());
		dropPottedContents(TFBlocks.POTTED_TRANSFORMATION_SAPLING.value());
		dropPottedContents(TFBlocks.POTTED_MINING_SAPLING.value());
		dropPottedContents(TFBlocks.POTTED_SORTING_SAPLING.value());
		dropPottedContents(TFBlocks.POTTED_MAYAPPLE.value());
		dropPottedContents(TFBlocks.POTTED_FIDDLEHEAD.value());
		dropPottedContents(TFBlocks.POTTED_MUSHGLOOM.value());
		add(TFBlocks.POTTED_THORN.value(), createSingleItemTable(Items.FLOWER_POT));
		add(TFBlocks.POTTED_GREEN_THORN.value(), createSingleItemTable(Items.FLOWER_POT));
		add(TFBlocks.POTTED_DEAD_THORN.value(), createSingleItemTable(Items.FLOWER_POT));

		dropSelf(TFBlocks.OAK_BANISTER.value());
		dropSelf(TFBlocks.SPRUCE_BANISTER.value());
		dropSelf(TFBlocks.BIRCH_BANISTER.value());
		dropSelf(TFBlocks.JUNGLE_BANISTER.value());
		dropSelf(TFBlocks.ACACIA_BANISTER.value());
		dropSelf(TFBlocks.DARK_OAK_BANISTER.value());
		dropSelf(TFBlocks.CRIMSON_BANISTER.value());
		dropSelf(TFBlocks.WARPED_BANISTER.value());
		dropSelf(TFBlocks.VANGROVE_BANISTER.value());
		dropSelf(TFBlocks.BAMBOO_BANISTER.value());
		dropSelf(TFBlocks.CHERRY_BANISTER.value());

		add(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_CHERRY_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.value()));
		add(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.value(), hollowLog(TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.value()));

		add(TFBlocks.HOLLOW_OAK_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_OAK_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.value()));
		add(TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.value()));
		add(TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_CHERRY_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_DARK_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_DARK_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_TIME_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_TIME_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_MINING_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_MINING_LOG_VERTICAL.value()));
		add(TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.value(), verticalHollowLog(TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.value()));

		add(TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_VANGROVE_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_CHERRY_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.value()));
		add(TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.value(), hollowLog(TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.value()));


		dropSelf(TFBlocks.TWILIGHT_OAK_LOG.value());
		dropSelf(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.value());
		dropSelf(TFBlocks.TWILIGHT_OAK_WOOD.value());
		dropSelf(TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.value());
		dropSelf(TFBlocks.TWILIGHT_OAK_SAPLING.value());
		add(TFBlocks.TWILIGHT_OAK_LEAVES.value(), silkAndStick(TFBlocks.TWILIGHT_OAK_LEAVES.value(), TFBlocks.TWILIGHT_OAK_SAPLING.value(), DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.RAINBOW_OAK_SAPLING.value());
		add(TFBlocks.RAINBOW_OAK_LEAVES.value(), silkAndStick(TFBlocks.RAINBOW_OAK_LEAVES.value(), TFBlocks.RAINBOW_OAK_SAPLING.value(), RARE_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.HOLLOW_OAK_SAPLING.value());
		dropSelf(TFBlocks.TWILIGHT_OAK_PLANKS.value());
		dropSelf(TFBlocks.TWILIGHT_OAK_STAIRS.value());
		add(TFBlocks.TWILIGHT_OAK_SLAB.value(), createSlabItemTable(TFBlocks.TWILIGHT_OAK_SLAB.value()));
		dropSelf(TFBlocks.TWILIGHT_OAK_BUTTON.value());
		dropSelf(TFBlocks.TWILIGHT_OAK_FENCE.value());
		dropSelf(TFBlocks.TWILIGHT_OAK_GATE.value());
		dropSelf(TFBlocks.TWILIGHT_OAK_PLATE.value());
		add(TFBlocks.TWILIGHT_OAK_DOOR.value(), createSinglePropConditionTable(TFBlocks.TWILIGHT_OAK_DOOR.value(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.TWILIGHT_OAK_TRAPDOOR.value());
		add(TFBlocks.TWILIGHT_OAK_SIGN.value(), createSingleItemTable(TFBlocks.TWILIGHT_OAK_SIGN.value().asItem()));
		add(TFBlocks.TWILIGHT_WALL_SIGN.value(), createSingleItemTable(TFBlocks.TWILIGHT_OAK_SIGN.value().asItem()));
		add(TFBlocks.TWILIGHT_OAK_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.TWILIGHT_OAK_HANGING_SIGN.value().asItem()));
		add(TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.TWILIGHT_OAK_HANGING_SIGN.value().asItem()));
		dropSelf(TFBlocks.TWILIGHT_OAK_BANISTER.value());
		dropSelf(TFBlocks.TWILIGHT_OAK_CHEST.value());

		dropSelf(TFBlocks.CANOPY_LOG.value());
		dropSelf(TFBlocks.STRIPPED_CANOPY_LOG.value());
		dropSelf(TFBlocks.CANOPY_WOOD.value());
		dropSelf(TFBlocks.STRIPPED_CANOPY_WOOD.value());
		dropSelf(TFBlocks.CANOPY_SAPLING.value());
		add(TFBlocks.CANOPY_LEAVES.value(), silkAndStick(TFBlocks.CANOPY_LEAVES.value(), TFBlocks.CANOPY_SAPLING.value(), DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.CANOPY_PLANKS.value());
		dropSelf(TFBlocks.CANOPY_STAIRS.value());
		add(TFBlocks.CANOPY_SLAB.value(), createSlabItemTable(TFBlocks.CANOPY_SLAB.value()));
		dropSelf(TFBlocks.CANOPY_BUTTON.value());
		dropSelf(TFBlocks.CANOPY_FENCE.value());
		dropSelf(TFBlocks.CANOPY_GATE.value());
		dropSelf(TFBlocks.CANOPY_PLATE.value());
		add(TFBlocks.CANOPY_DOOR.value(), createSinglePropConditionTable(TFBlocks.CANOPY_DOOR.value(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.CANOPY_TRAPDOOR.value());
		add(TFBlocks.CANOPY_SIGN.value(), createSingleItemTable(TFBlocks.CANOPY_SIGN.value().asItem()));
		add(TFBlocks.CANOPY_WALL_SIGN.value(), createSingleItemTable(TFBlocks.CANOPY_SIGN.value().asItem()));
		add(TFBlocks.CANOPY_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.CANOPY_HANGING_SIGN.value().asItem()));
		add(TFBlocks.CANOPY_WALL_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.CANOPY_HANGING_SIGN.value().asItem()));
		add(TFBlocks.CANOPY_BOOKSHELF.value(), createSingleItemTableWithSilkTouch(TFBlocks.CANOPY_BOOKSHELF.value(), Items.BOOK, ConstantValue.exactly(2.0F)));
		dropSelf(TFBlocks.CANOPY_BANISTER.value());
		dropSelf(TFBlocks.CANOPY_CHEST.value());

		dropSelf(TFBlocks.MANGROVE_LOG.value());
		dropSelf(TFBlocks.STRIPPED_MANGROVE_LOG.value());
		dropSelf(TFBlocks.MANGROVE_WOOD.value());
		dropSelf(TFBlocks.STRIPPED_MANGROVE_WOOD.value());
		dropSelf(TFBlocks.MANGROVE_SAPLING.value());
		add(TFBlocks.MANGROVE_LEAVES.value(), silkAndStick(TFBlocks.MANGROVE_LEAVES.value(), TFBlocks.MANGROVE_SAPLING.value(), DEFAULT_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.MANGROVE_PLANKS.value());
		dropSelf(TFBlocks.MANGROVE_STAIRS.value());
		add(TFBlocks.MANGROVE_SLAB.value(), createSlabItemTable(TFBlocks.MANGROVE_SLAB.value()));
		dropSelf(TFBlocks.MANGROVE_BUTTON.value());
		dropSelf(TFBlocks.MANGROVE_FENCE.value());
		dropSelf(TFBlocks.MANGROVE_GATE.value());
		dropSelf(TFBlocks.MANGROVE_PLATE.value());
		add(TFBlocks.MANGROVE_DOOR.value(), createSinglePropConditionTable(TFBlocks.MANGROVE_DOOR.value(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.MANGROVE_TRAPDOOR.value());
		add(TFBlocks.MANGROVE_SIGN.value(), createSingleItemTable(TFBlocks.MANGROVE_SIGN.value().asItem()));
		add(TFBlocks.MANGROVE_WALL_SIGN.value(), createSingleItemTable(TFBlocks.MANGROVE_SIGN.value().asItem()));
		add(TFBlocks.MANGROVE_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.MANGROVE_HANGING_SIGN.value().asItem()));
		add(TFBlocks.MANGROVE_WALL_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.MANGROVE_HANGING_SIGN.value().asItem()));
		dropSelf(TFBlocks.MANGROVE_BANISTER.value());
		dropSelf(TFBlocks.MANGROVE_CHEST.value());

		dropSelf(TFBlocks.DARK_LOG.value());
		dropSelf(TFBlocks.STRIPPED_DARK_LOG.value());
		dropSelf(TFBlocks.DARK_WOOD.value());
		dropSelf(TFBlocks.STRIPPED_DARK_WOOD.value());
		dropSelf(TFBlocks.DARKWOOD_SAPLING.value());
		add(TFBlocks.DARK_LEAVES.value(), silkAndStick(TFBlocks.DARK_LEAVES.value(), TFBlocks.DARKWOOD_SAPLING.value(), RARE_SAPLING_DROP_RATES));
		add(TFBlocks.HARDENED_DARK_LEAVES.value(), silkAndStick(TFBlocks.DARK_LEAVES.value(), TFBlocks.DARKWOOD_SAPLING.value(), RARE_SAPLING_DROP_RATES));
		dropSelf(TFBlocks.DARK_PLANKS.value());
		dropSelf(TFBlocks.DARK_STAIRS.value());
		add(TFBlocks.DARK_SLAB.value(), createSlabItemTable(TFBlocks.DARK_SLAB.value()));
		dropSelf(TFBlocks.DARK_BUTTON.value());
		dropSelf(TFBlocks.DARK_FENCE.value());
		dropSelf(TFBlocks.DARK_GATE.value());
		dropSelf(TFBlocks.DARK_PLATE.value());
		add(TFBlocks.DARK_DOOR.value(), createSinglePropConditionTable(TFBlocks.DARK_DOOR.value(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.DARK_TRAPDOOR.value());
		add(TFBlocks.DARK_SIGN.value(), createSingleItemTable(TFBlocks.DARK_SIGN.value().asItem()));
		add(TFBlocks.DARK_WALL_SIGN.value(), createSingleItemTable(TFBlocks.DARK_SIGN.value().asItem()));
		add(TFBlocks.DARK_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.DARK_HANGING_SIGN.value().asItem()));
		add(TFBlocks.DARK_WALL_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.DARK_HANGING_SIGN.value().asItem()));
		dropSelf(TFBlocks.DARK_BANISTER.value());
		dropSelf(TFBlocks.DARK_CHEST.value());

		dropSelf(TFBlocks.TIME_LOG.value());
		dropSelf(TFBlocks.STRIPPED_TIME_LOG.value());
		dropSelf(TFBlocks.TIME_WOOD.value());
		dropSelf(TFBlocks.STRIPPED_TIME_WOOD.value());
		dropOther(TFBlocks.TIME_LOG_CORE.value(), TFBlocks.TIME_LOG.value());
		dropSelf(TFBlocks.TIME_SAPLING.value());
		registerLeavesNoSapling(TFBlocks.TIME_LEAVES.value());
		dropSelf(TFBlocks.TIME_PLANKS.value());
		dropSelf(TFBlocks.TIME_STAIRS.value());
		add(TFBlocks.TIME_SLAB.value(), createSlabItemTable(TFBlocks.TIME_SLAB.value()));
		dropSelf(TFBlocks.TIME_BUTTON.value());
		dropSelf(TFBlocks.TIME_FENCE.value());
		dropSelf(TFBlocks.TIME_GATE.value());
		dropSelf(TFBlocks.TIME_PLATE.value());
		add(TFBlocks.TIME_DOOR.value(), createSinglePropConditionTable(TFBlocks.TIME_DOOR.value(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.TIME_TRAPDOOR.value());
		add(TFBlocks.TIME_SIGN.value(), createSingleItemTable(TFBlocks.TIME_SIGN.value().asItem()));
		add(TFBlocks.TIME_WALL_SIGN.value(), createSingleItemTable(TFBlocks.TIME_SIGN.value().asItem()));
		add(TFBlocks.TIME_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.TIME_HANGING_SIGN.value().asItem()));
		add(TFBlocks.TIME_WALL_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.TIME_HANGING_SIGN.value().asItem()));
		dropSelf(TFBlocks.TIME_BANISTER.value());
		dropSelf(TFBlocks.TIME_CHEST.value());

		dropSelf(TFBlocks.TRANSFORMATION_LOG.value());
		dropSelf(TFBlocks.STRIPPED_TRANSFORMATION_LOG.value());
		dropSelf(TFBlocks.TRANSFORMATION_WOOD.value());
		dropSelf(TFBlocks.STRIPPED_TRANSFORMATION_WOOD.value());
		dropOther(TFBlocks.TRANSFORMATION_LOG_CORE.value(), TFBlocks.TRANSFORMATION_LOG.value());
		dropSelf(TFBlocks.TRANSFORMATION_SAPLING.value());
		registerLeavesNoSapling(TFBlocks.TRANSFORMATION_LEAVES.value());
		dropSelf(TFBlocks.TRANSFORMATION_PLANKS.value());
		dropSelf(TFBlocks.TRANSFORMATION_STAIRS.value());
		add(TFBlocks.TRANSFORMATION_SLAB.value(), createSlabItemTable(TFBlocks.TRANSFORMATION_SLAB.value()));
		dropSelf(TFBlocks.TRANSFORMATION_BUTTON.value());
		dropSelf(TFBlocks.TRANSFORMATION_FENCE.value());
		dropSelf(TFBlocks.TRANSFORMATION_GATE.value());
		dropSelf(TFBlocks.TRANSFORMATION_PLATE.value());
		add(TFBlocks.TRANSFORMATION_DOOR.value(), createSinglePropConditionTable(TFBlocks.TRANSFORMATION_DOOR.value(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.TRANSFORMATION_TRAPDOOR.value());
		add(TFBlocks.TRANSFORMATION_SIGN.value(), createSingleItemTable(TFBlocks.TRANSFORMATION_SIGN.value().asItem()));
		add(TFBlocks.TRANSFORMATION_WALL_SIGN.value(), createSingleItemTable(TFBlocks.TRANSFORMATION_SIGN.value().asItem()));
		add(TFBlocks.TRANSFORMATION_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.TRANSFORMATION_HANGING_SIGN.value().asItem()));
		add(TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.TRANSFORMATION_HANGING_SIGN.value().asItem()));
		dropSelf(TFBlocks.TRANSFORMATION_BANISTER.value());
		dropSelf(TFBlocks.TRANSFORMATION_CHEST.value());

		dropSelf(TFBlocks.MINING_LOG.value());
		dropSelf(TFBlocks.STRIPPED_MINING_LOG.value());
		dropSelf(TFBlocks.MINING_WOOD.value());
		dropSelf(TFBlocks.STRIPPED_MINING_WOOD.value());
		dropOther(TFBlocks.MINING_LOG_CORE.value(), TFBlocks.MINING_LOG.value());
		dropSelf(TFBlocks.MINING_SAPLING.value());
		registerLeavesNoSapling(TFBlocks.MINING_LEAVES.value());
		dropSelf(TFBlocks.MINING_PLANKS.value());
		dropSelf(TFBlocks.MINING_STAIRS.value());
		add(TFBlocks.MINING_SLAB.value(), createSlabItemTable(TFBlocks.MINING_SLAB.value()));
		dropSelf(TFBlocks.MINING_BUTTON.value());
		dropSelf(TFBlocks.MINING_FENCE.value());
		dropSelf(TFBlocks.MINING_GATE.value());
		dropSelf(TFBlocks.MINING_PLATE.value());
		add(TFBlocks.MINING_DOOR.value(), createSinglePropConditionTable(TFBlocks.MINING_DOOR.value(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.MINING_TRAPDOOR.value());
		add(TFBlocks.MINING_SIGN.value(), createSingleItemTable(TFBlocks.MINING_SIGN.value().asItem()));
		add(TFBlocks.MINING_WALL_SIGN.value(), createSingleItemTable(TFBlocks.MINING_SIGN.value().asItem()));
		add(TFBlocks.MINING_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.MINING_HANGING_SIGN.value().asItem()));
		add(TFBlocks.MINING_WALL_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.MINING_HANGING_SIGN.value().asItem()));
		dropSelf(TFBlocks.MINING_BANISTER.value());
		dropSelf(TFBlocks.MINING_CHEST.value());

		dropSelf(TFBlocks.SORTING_LOG.value());
		dropSelf(TFBlocks.STRIPPED_SORTING_LOG.value());
		dropSelf(TFBlocks.SORTING_WOOD.value());
		dropSelf(TFBlocks.STRIPPED_SORTING_WOOD.value());
		dropOther(TFBlocks.SORTING_LOG_CORE.value(), TFBlocks.SORTING_LOG.value());
		dropSelf(TFBlocks.SORTING_SAPLING.value());
		registerLeavesNoSapling(TFBlocks.SORTING_LEAVES.value());
		dropSelf(TFBlocks.SORTING_PLANKS.value());
		dropSelf(TFBlocks.SORTING_STAIRS.value());
		add(TFBlocks.SORTING_SLAB.value(), createSlabItemTable(TFBlocks.SORTING_SLAB.value()));
		dropSelf(TFBlocks.SORTING_BUTTON.value());
		dropSelf(TFBlocks.SORTING_FENCE.value());
		dropSelf(TFBlocks.SORTING_GATE.value());
		dropSelf(TFBlocks.SORTING_PLATE.value());
		add(TFBlocks.SORTING_DOOR.value(), createSinglePropConditionTable(TFBlocks.SORTING_DOOR.value(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
		dropSelf(TFBlocks.SORTING_TRAPDOOR.value());
		add(TFBlocks.SORTING_SIGN.value(), createSingleItemTable(TFBlocks.SORTING_SIGN.value().asItem()));
		add(TFBlocks.SORTING_WALL_SIGN.value(), createSingleItemTable(TFBlocks.SORTING_SIGN.value().asItem()));
		add(TFBlocks.SORTING_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.SORTING_HANGING_SIGN.value().asItem()));
		add(TFBlocks.SORTING_WALL_HANGING_SIGN.value(), createSingleItemTable(TFBlocks.SORTING_HANGING_SIGN.value().asItem()));
		dropSelf(TFBlocks.SORTING_BANISTER.value());
		dropSelf(TFBlocks.SORTING_CHEST.value());

	}

	private void registerLeavesNoSapling(Block leaves) {
		LootPoolEntryContainer.Builder<?> sticks = applyExplosionDecay(leaves, LootItem.lootTableItem(Items.STICK)
				.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
				.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F)));
		add(leaves, createSilkTouchOrShearsDispatchTable(leaves, sticks));
	}

	private LootTable.Builder hollowLog(Block log) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(log.asItem()).when(HAS_SILK_TOUCH).otherwise(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Blocks.GRASS).when(HAS_SILK_TOUCH).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogHorizontal.VARIANT, HollowLogVariants.Horizontal.MOSS_AND_GRASS)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TFBlocks.MOSS_PATCH.value()).when(HAS_SILK_TOUCH).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogHorizontal.VARIANT, HollowLogVariants.Horizontal.MOSS_AND_GRASS)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TFBlocks.MOSS_PATCH.value()).when(HAS_SILK_TOUCH).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogHorizontal.VARIANT, HollowLogVariants.Horizontal.MOSS)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.SNOWBALL).when(HAS_SILK_TOUCH).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogHorizontal.VARIANT, HollowLogVariants.Horizontal.SNOW)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Blocks.VINE).when(HAS_SILK_TOUCH).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogClimbable.VARIANT, HollowLogVariants.Climbable.VINE)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Blocks.LADDER).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogClimbable.VARIANT, HollowLogVariants.Climbable.LADDER)))))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Blocks.LADDER).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(log).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HollowLogClimbable.VARIANT, HollowLogVariants.Climbable.LADDER_WATERLOGGED)))));
	}

	private LootTable.Builder verticalHollowLog(Block log) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(log.asItem()).when(HAS_SILK_TOUCH).otherwise(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));
	}

	// [VanillaCopy] super.droppingWithChancesAndSticks, but non-silk touch parameter can be an item instead of a block
	private LootTable.Builder silkAndStick(Block block, ItemLike nonSilk, float... nonSilkFortune) {
		return createSilkTouchOrShearsDispatchTable(block, this.applyExplosionCondition(block, LootItem.lootTableItem(nonSilk.asItem())).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, nonSilkFortune))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when((HAS_SHEARS.or(HAS_SILK_TOUCH)).invert()).add(applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
	}

	private static LootTable.Builder casketInfo(Block block) {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).apply(CopyBlockState.copyState(block).copy(KeepsakeCasketBlock.BREAKAGE)));
	}

	private LootTable.Builder particleSpawner() {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(this.applyExplosionDecay(TFBlocks.FIREFLY_SPAWNER.value(), LootItem.lootTableItem(TFBlocks.FIREFLY_SPAWNER.value()))))
				.withPool(LootPool.lootPool()
						.add(AlternativesEntry.alternatives(AlternativesEntry.alternatives(FireflySpawnerBlock.RADIUS.getPossibleValues(), layer ->
								LootItem.lootTableItem(TFBlocks.FIREFLY.value())
										.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FIREFLY_SPAWNER.value())
												.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FireflySpawnerBlock.RADIUS, layer)))
										.apply(SetItemCountFunction.setCount(ConstantValue.exactly(layer - 1)))))));
	}

	protected LootTable.Builder torchberryPlant(Block pBlock) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(pBlock).when(HAS_SHEARS)))
				.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(TFItems.TORCHBERRIES.value())
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TorchberryPlantBlock.HAS_BERRIES, true)))));
	}

	protected LootTable.Builder redThread() {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(this.applyExplosionDecay(TFBlocks.RED_THREAD.value(), LootItem.lootTableItem(TFBlocks.RED_THREAD.value())
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
										.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.value())
												.setProperties(StatePropertiesPredicate.Builder.properties()
														.hasProperty(PipeBlock.EAST, true))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
										.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.value())
												.setProperties(StatePropertiesPredicate.Builder.properties()
														.hasProperty(PipeBlock.WEST, true))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
										.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.value())
												.setProperties(StatePropertiesPredicate.Builder.properties()
														.hasProperty(PipeBlock.NORTH, true))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
										.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.value())
												.setProperties(StatePropertiesPredicate.Builder.properties()
														.hasProperty(PipeBlock.SOUTH, true))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
										.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.value())
												.setProperties(StatePropertiesPredicate.Builder.properties()
														.hasProperty(PipeBlock.UP, true))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
										.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.RED_THREAD.value())
												.setProperties(StatePropertiesPredicate.Builder.properties()
														.hasProperty(PipeBlock.DOWN, true))))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))));
	}

	protected LootTable.Builder fallenLeaves() {
		return LootTable.lootTable().withPool(LootPool.lootPool()
				.add(AlternativesEntry.alternatives(AlternativesEntry.alternatives(FallenLeavesBlock.LAYERS.getPossibleValues(), layer ->
						LootItem.lootTableItem(TFBlocks.FALLEN_LEAVES.value())
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TFBlocks.FALLEN_LEAVES.value())
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FallenLeavesBlock.LAYERS, layer)))
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(layer)))).when(HAS_SHEARS))));
	}

	//[VanillaCopy] of a few different methods from BlockLoot. These are here just so we can use the modded shears thing
	protected static LootTable.Builder createShearsDispatchTable(Block block, LootPoolEntryContainer.Builder<?> builder) {
		return createSelfDropDispatchTable(block, HAS_SHEARS, builder);
	}

	protected static LootTable.Builder createSilkTouchOrShearsDispatchTable(Block block, LootPoolEntryContainer.Builder<?> builder) {
		return createSelfDropDispatchTable(block, HAS_SHEARS.or(HAS_SILK_TOUCH), builder);
	}

	protected static LootTable.Builder createShearsOnlyDrop(ItemLike p_124287_) {
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_SHEARS).add(LootItem.lootTableItem(p_124287_)));
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return TFBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::value).collect(Collectors.toList());
	}
}
