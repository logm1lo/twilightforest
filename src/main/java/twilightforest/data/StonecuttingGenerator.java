package twilightforest.data;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.Collections;
import java.util.Optional;

import static twilightforest.TwilightForestMod.prefix;

public class StonecuttingGenerator {

	protected static void buildRecipes(RecipeOutput output) {
		stonecutting(output, TFBlocks.CASTLE_BRICK.value(), TFBlocks.CASTLE_BRICK_STAIRS.value());
		stonecutting(output, TFBlocks.CASTLE_BRICK.value(), TFBlocks.THICK_CASTLE_BRICK.value());
		stonecutting(output, TFBlocks.CASTLE_BRICK.value(), TFBlocks.WORN_CASTLE_BRICK.value());
		stonecutting(output, TFBlocks.CASTLE_BRICK.value(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.value());
		stonecutting(output, TFBlocks.CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_TILE.value());
		stonecutting(output, TFBlocks.CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value());
		stonecutting(output, TFBlocks.CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.value());
		stonecutting(output, TFBlocks.CASTLE_BRICK.value(), TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.value());
		stonecutting(output, TFBlocks.CASTLE_BRICK.value(), TFBlocks.ENCASED_CASTLE_BRICK_TILE.value());
		stonecutting(output, TFBlocks.CASTLE_BRICK.value(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.value());
		stonecutting(output, TFBlocks.CASTLE_BRICK.value(), TFBlocks.CASTLE_ROOF_TILE.value());

		stonecutting(output, TFBlocks.CRACKED_CASTLE_BRICK.value(), TFBlocks.THICK_CASTLE_BRICK.value());
		stonecutting(output, TFBlocks.CRACKED_CASTLE_BRICK.value(), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.value());
		stonecutting(output, TFBlocks.CRACKED_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_TILE.value());
		stonecutting(output, TFBlocks.CRACKED_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value());
		stonecutting(output, TFBlocks.CRACKED_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.value());

		stonecutting(output, TFBlocks.WORN_CASTLE_BRICK.value(), TFBlocks.THICK_CASTLE_BRICK.value());
		stonecutting(output, TFBlocks.WORN_CASTLE_BRICK.value(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.value());
		stonecutting(output, TFBlocks.WORN_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_TILE.value());
		stonecutting(output, TFBlocks.WORN_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value());
		stonecutting(output, TFBlocks.WORN_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.value());

		stonecutting(output, TFBlocks.MOSSY_CASTLE_BRICK.value(), TFBlocks.THICK_CASTLE_BRICK.value());
		stonecutting(output, TFBlocks.MOSSY_CASTLE_BRICK.value(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.value());
		stonecutting(output, TFBlocks.MOSSY_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_TILE.value());
		stonecutting(output, TFBlocks.MOSSY_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value());
		stonecutting(output, TFBlocks.MOSSY_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.value());

		stonecutting(output, TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.value());
		stonecutting(output, TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value(), TFBlocks.BOLD_CASTLE_BRICK_TILE.value());
		stonecutting(output, TFBlocks.BOLD_CASTLE_BRICK_TILE.value(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.value());
		stonecutting(output, TFBlocks.BOLD_CASTLE_BRICK_TILE.value(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value());

		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.value(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.value());
		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.value(), TFBlocks.ENCASED_CASTLE_BRICK_TILE.value());
		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.value(), TFBlocks.CASTLE_ROOF_TILE.value());

		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_TILE.value(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.value());
		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_TILE.value(), TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.value());
		stonecutting(output, TFBlocks.ENCASED_CASTLE_BRICK_TILE.value(), TFBlocks.CASTLE_ROOF_TILE.value());

		stonecutting(output, TFBlocks.THICK_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value());
		stonecutting(output, TFBlocks.THICK_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_TILE.value());
		stonecutting(output, TFBlocks.THICK_CASTLE_BRICK.value(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.value());

		stonecutting(output, TFBlocks.ETCHED_NAGASTONE.value(), TFBlocks.NAGASTONE_STAIRS_LEFT.value());
		stonecutting(output, TFBlocks.ETCHED_NAGASTONE.value(), TFBlocks.NAGASTONE_STAIRS_RIGHT.value());
		stonecutting(output, TFBlocks.MOSSY_ETCHED_NAGASTONE.value(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.value());
		stonecutting(output, TFBlocks.MOSSY_ETCHED_NAGASTONE.value(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.value());
		stonecutting(output, TFBlocks.CRACKED_ETCHED_NAGASTONE.value(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.value());
		stonecutting(output, TFBlocks.CRACKED_ETCHED_NAGASTONE.value(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.value());

		stonecutting(output, TFBlocks.NAGASTONE_STAIRS_RIGHT.value(), TFBlocks.NAGASTONE_STAIRS_LEFT.value());
		stonecutting(output, TFBlocks.NAGASTONE_STAIRS_LEFT.value(), TFBlocks.NAGASTONE_STAIRS_RIGHT.value());
		stonecutting(output, TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.value(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.value());
		stonecutting(output, TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.value(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.value());
		stonecutting(output, TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.value(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.value());
		stonecutting(output, TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.value(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.value());

		stonecutting(output, TFBlocks.DARK_LOG.value(), TFBlocks.TOWERWOOD.value());
		stonecutting(output, TFBlocks.DARK_WOOD.value(), TFBlocks.TOWERWOOD.value());
		stonecutting(output, TFBlocks.DARK_LOG.value(), TFBlocks.ENCASED_TOWERWOOD.value());
		stonecutting(output, TFBlocks.DARK_WOOD.value(), TFBlocks.ENCASED_TOWERWOOD.value());

		stonecutting(output, TFBlocks.MAZESTONE.value(), TFBlocks.MAZESTONE_BORDER.value());
		stonecutting(output, TFBlocks.MAZESTONE.value(), TFBlocks.MAZESTONE_BRICK.value());
		stonecutting(output, TFBlocks.MAZESTONE.value(), TFBlocks.CUT_MAZESTONE.value());
		stonecutting(output, TFBlocks.MAZESTONE.value(), TFBlocks.DECORATIVE_MAZESTONE.value());
		stonecutting(output, TFBlocks.MAZESTONE.value(), TFBlocks.MAZESTONE_MOSAIC.value());

		stonecutting(output, TFBlocks.MAZESTONE_BRICK.value(), TFBlocks.MAZESTONE_BORDER.value());
		stonecutting(output, TFBlocks.MAZESTONE_BRICK.value(), TFBlocks.CUT_MAZESTONE.value());
		stonecutting(output, TFBlocks.MAZESTONE_BRICK.value(), TFBlocks.DECORATIVE_MAZESTONE.value());
		stonecutting(output, TFBlocks.MAZESTONE_BRICK.value(), TFBlocks.MAZESTONE_MOSAIC.value());

		stonecutting(output, TFBlocks.MAZESTONE_BORDER.value(), TFBlocks.MAZESTONE_BRICK.value());
		stonecutting(output, TFBlocks.MAZESTONE_BORDER.value(), TFBlocks.CUT_MAZESTONE.value());
		stonecutting(output, TFBlocks.MAZESTONE_BORDER.value(), TFBlocks.DECORATIVE_MAZESTONE.value());
		stonecutting(output, TFBlocks.MAZESTONE_BORDER.value(), TFBlocks.MAZESTONE_MOSAIC.value());

		stonecutting(output, TFBlocks.CUT_MAZESTONE.value(), TFBlocks.MAZESTONE_BORDER.value());
		stonecutting(output, TFBlocks.CUT_MAZESTONE.value(), TFBlocks.MAZESTONE_BRICK.value());
		stonecutting(output, TFBlocks.CUT_MAZESTONE.value(), TFBlocks.DECORATIVE_MAZESTONE.value());
		stonecutting(output, TFBlocks.CUT_MAZESTONE.value(), TFBlocks.MAZESTONE_MOSAIC.value());

		stonecutting(output, TFBlocks.DECORATIVE_MAZESTONE.value(), TFBlocks.MAZESTONE_BORDER.value());
		stonecutting(output, TFBlocks.DECORATIVE_MAZESTONE.value(), TFBlocks.CUT_MAZESTONE.value());
		stonecutting(output, TFBlocks.DECORATIVE_MAZESTONE.value(), TFBlocks.MAZESTONE_BRICK.value());
		stonecutting(output, TFBlocks.DECORATIVE_MAZESTONE.value(), TFBlocks.MAZESTONE_MOSAIC.value());

		stonecutting(output, TFBlocks.MAZESTONE_MOSAIC.value(), TFBlocks.MAZESTONE_BORDER.value());
		stonecutting(output, TFBlocks.MAZESTONE_MOSAIC.value(), TFBlocks.CUT_MAZESTONE.value());
		stonecutting(output, TFBlocks.MAZESTONE_MOSAIC.value(), TFBlocks.DECORATIVE_MAZESTONE.value());
		stonecutting(output, TFBlocks.MAZESTONE_MOSAIC.value(), TFBlocks.MAZESTONE_BRICK.value());

		stonecutting(output, TFBlocks.TWILIGHT_OAK_LOG.value(), TFItems.HOLLOW_TWILIGHT_OAK_LOG.value());
		stonecutting(output, TFBlocks.CANOPY_LOG.value(), TFItems.HOLLOW_CANOPY_LOG.value());
		stonecutting(output, TFBlocks.MANGROVE_LOG.value(), TFItems.HOLLOW_MANGROVE_LOG.value());
		stonecutting(output, TFBlocks.DARK_LOG.value(), TFItems.HOLLOW_DARK_LOG.value());
		stonecutting(output, TFBlocks.TIME_LOG.value(), TFItems.HOLLOW_TIME_LOG.value());
		stonecutting(output, TFBlocks.TRANSFORMATION_LOG.value(), TFItems.HOLLOW_TRANSFORMATION_LOG.value());
		stonecutting(output, TFBlocks.MINING_LOG.value(), TFItems.HOLLOW_MINING_LOG.value());
		stonecutting(output, TFBlocks.SORTING_LOG.value(), TFItems.HOLLOW_SORTING_LOG.value());

		stonecutting(output, Blocks.OAK_LOG, TFItems.HOLLOW_OAK_LOG.value());
		stonecutting(output, Blocks.SPRUCE_LOG, TFItems.HOLLOW_SPRUCE_LOG.value());
		stonecutting(output, Blocks.BIRCH_LOG, TFItems.HOLLOW_BIRCH_LOG.value());
		stonecutting(output, Blocks.JUNGLE_LOG, TFItems.HOLLOW_JUNGLE_LOG.value());
		stonecutting(output, Blocks.ACACIA_LOG, TFItems.HOLLOW_ACACIA_LOG.value());
		stonecutting(output, Blocks.DARK_OAK_LOG, TFItems.HOLLOW_DARK_OAK_LOG.value());
		stonecutting(output, Blocks.CRIMSON_STEM, TFItems.HOLLOW_CRIMSON_STEM.value());
		stonecutting(output, Blocks.WARPED_STEM, TFItems.HOLLOW_WARPED_STEM.value());
		stonecutting(output, Blocks.MANGROVE_LOG, TFItems.HOLLOW_VANGROVE_LOG.value());
		stonecutting(output, Blocks.CHERRY_LOG, TFItems.HOLLOW_CHERRY_LOG.value());
		stonecutting(output, Blocks.STONE, TFBlocks.TWISTED_STONE.value());
		stonecutting(output, Blocks.STONE, TFBlocks.BOLD_STONE_PILLAR.value());
		stonecutting(output, Blocks.STONE, TFBlocks.TWISTED_STONE_PILLAR.value());
		stonecutting(output, Blocks.STONE, TFBlocks.SPIRAL_BRICKS.value());
		stonecutting(output, TFBlocks.TWISTED_STONE.value(), TFBlocks.TWISTED_STONE_PILLAR.value());

		stonecutting(output, TFBlocks.UNDERBRICK.value(), TFBlocks.UNDERBRICK_FLOOR.value());
	}

	private static void stonecutting(RecipeOutput recipe, ItemLike input, ItemLike output) {
		stonecutting(recipe, input, output, 1);
	}

	private static void stonecutting(RecipeOutput recipe, ItemLike input, ItemLike output, int count) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output.asItem(), count).unlockedBy("has_block", has(input)).save(recipe, getIdFor(input, output));
	}

	private static ResourceLocation getIdFor(ItemLike input, ItemLike output) {
		String path = String.format("stonecutting/%s/%s", BuiltInRegistries.ITEM.getKey(input.asItem()).getPath(), BuiltInRegistries.ITEM.getKey(output.asItem()).getPath());
		return prefix(path);
	}

	protected static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
		return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, Collections.singletonList(ItemPredicate.Builder.item().of(item).build())));
	}
}
