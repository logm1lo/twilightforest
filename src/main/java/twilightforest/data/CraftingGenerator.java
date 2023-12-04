package twilightforest.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.TrueCondition;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.UncraftingGenerator;
import twilightforest.data.helpers.CraftingDataHelper;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.item.recipe.UncraftingTableCondition;

import java.util.concurrent.CompletableFuture;

public class CraftingGenerator extends CraftingDataHelper {
	public CraftingGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void buildRecipes(RecipeOutput output) {
		StonecuttingGenerator.buildRecipes(output);
		UncraftingGenerator.buildRecipes(output);

		// The Recipe Builder currently doesn't support enchantment-resulting recipes, those must be manually created.
		blockCompressionRecipes(output);
		equipmentRecipes(output);
		emptyMapRecipes(output);
		woodRecipes(output);
		fieryConversions(output);

		nagastoneRecipes(output);
		darkTowerRecipes(output);
		castleRecipes(output);

		bannerPattern(output, "naga_banner_pattern", TFBlocks.NAGA_TROPHY, TFItems.NAGA_BANNER_PATTERN);
		bannerPattern(output, "lich_banner_pattern", TFBlocks.LICH_TROPHY, TFItems.LICH_BANNER_PATTERN);
		bannerPattern(output, "minoshroom_banner_pattern", TFBlocks.MINOSHROOM_TROPHY, TFItems.MINOSHROOM_BANNER_PATTERN);
		bannerPattern(output, "hydra_banner_pattern", TFBlocks.HYDRA_TROPHY, TFItems.HYDRA_BANNER_PATTERN);
		bannerPattern(output, "knight_phantom_banner_pattern", TFBlocks.KNIGHT_PHANTOM_TROPHY, TFItems.KNIGHT_PHANTOM_BANNER_PATTERN);
		bannerPattern(output, "ur_ghast_banner_pattern", TFBlocks.UR_GHAST_TROPHY, TFItems.UR_GHAST_BANNER_PATTERN);
		bannerPattern(output, "alpha_yeti_banner_pattern", TFBlocks.ALPHA_YETI_TROPHY, TFItems.ALPHA_YETI_BANNER_PATTERN);
		bannerPattern(output, "snow_queen_banner_pattern", TFBlocks.SNOW_QUEEN_TROPHY, TFItems.SNOW_QUEEN_BANNER_PATTERN);
		bannerPattern(output, "questing_ram_banner_pattern", TFBlocks.QUEST_RAM_TROPHY, TFItems.QUEST_RAM_BANNER_PATTERN);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.MOSS_BLOCK)
				.pattern("mmm")
				.pattern("mtm")
				.pattern("mmm")
				.define('m', Ingredient.of(TFBlocks.MOSS_PATCH.value()))
				.define('t', Ingredient.of(TFItems.TRANSFORMATION_POWDER.value()))
				.unlockedBy("has_item", has(TFItems.TRANSFORMATION_POWDER.value()))
				.save(output, TwilightForestMod.prefix("tf_moss_to_vanilla"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, TFBlocks.MOSS_PATCH.value(), 8)
				.requires(Ingredient.of(Items.MOSS_BLOCK))
				.requires(Ingredient.of(TFItems.TRANSFORMATION_POWDER.value()))
				.unlockedBy("has_item", has(TFItems.TRANSFORMATION_POWDER.value()))
				.save(output, TwilightForestMod.prefix("vanilla_to_tf_moss"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, TFBlocks.HUGE_LILY_PAD.value())
				.requires(Ingredient.of(Blocks.LILY_PAD), 4)
				.requires(Ingredient.of(TFItems.TRANSFORMATION_POWDER.value()))
				.unlockedBy("has_item", has(TFItems.TRANSFORMATION_POWDER.value()))
				.save(output, TwilightForestMod.prefix("vanilla_to_tf_lilypad"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, Blocks.LILY_PAD, 4)
				.requires(Ingredient.of(TFBlocks.HUGE_LILY_PAD.value()))
				.requires(Ingredient.of(TFItems.TRANSFORMATION_POWDER.value()))
				.unlockedBy("has_item", has(TFItems.TRANSFORMATION_POWDER.value()))
				.save(output, TwilightForestMod.prefix("tf_to_vanilla_lilypad"));

		slabBlock(output, "aurora_slab", TFBlocks.AURORA_SLAB, TFBlocks.AURORA_BLOCK);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.AURORA_PILLAR.value(), 2)
				.pattern("#")
				.pattern("#")
				.define('#', Ingredient.of(TFBlocks.AURORA_BLOCK.value()))
				.unlockedBy("has_slab", has(TFBlocks.AURORA_SLAB.value()))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.IRON_LADDER.value(), 3)
				.pattern("-#-")
				.pattern("-#-")
				.define('#', Ingredient.of(Blocks.IRON_BARS))
				.define('-', Tags.Items.NUGGETS_IRON)
				.unlockedBy("has_iron_bars", has(Blocks.IRON_BARS))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, TFBlocks.FIREFLY_JAR.value())
				.requires(Ingredient.of(TFBlocks.FIREFLY.value()))
				.requires(Ingredient.of(Items.GLASS_BOTTLE))
				.unlockedBy("has_item", has(TFBlocks.FIREFLY.value()))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, TFBlocks.FIREFLY_SPAWNER.value())
				.requires(Ingredient.of(TFBlocks.FIREFLY_JAR.value()))
				.requires(Ingredient.of(TFBlocks.FIREFLY.value()))
				.requires(Ingredient.of(Blocks.POPPY))
				.unlockedBy("has_jar", has(TFBlocks.FIREFLY_JAR.value()))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, TFBlocks.CICADA_JAR.value())
				.requires(Ingredient.of(TFBlocks.CICADA.value()))
				.requires(Ingredient.of(Items.GLASS_BOTTLE))
				.unlockedBy("has_item", has(TFBlocks.CICADA.value()))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MAGENTA_DYE)
				.requires(Ingredient.of(TFBlocks.HUGE_WATER_LILY.value()))
				.unlockedBy("has_item", has(TFBlocks.HUGE_WATER_LILY.value()))
				.save(output, TwilightForestMod.prefix("waterlily_to_magenta"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.RED_DYE)
				.requires(Ingredient.of(TFBlocks.THORN_ROSE.value()))
				.unlockedBy("has_item", has(TFBlocks.THORN_ROSE.value()))
				.save(output, TwilightForestMod.prefix("thorn_rose_to_red"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STICK)
				.requires(Ingredient.of(TFBlocks.ROOT_STRAND.value()))
				.unlockedBy("has_item", has(TFBlocks.ROOT_STRAND.value()))
				.save(output, TwilightForestMod.prefix("root_stick"));

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Blocks.TORCH, 5)
				.pattern("∴")
				.pattern("|")
				.define('∴', Ingredient.of(TFItems.TORCHBERRIES.value()))
				.define('|', Tags.Items.RODS_WOODEN)
				.unlockedBy("has_item", has(TFItems.TORCHBERRIES.value()))
				.save(output, TwilightForestMod.prefix("berry_torch"));

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TFBlocks.UNCRAFTING_TABLE.value())
								.pattern("###")
								.pattern("#X#")
								.pattern("###")
								.define('#', Blocks.CRAFTING_TABLE)
								.define('X', TFItems.MAZE_MAP_FOCUS.value())
								.unlockedBy("has_uncrafting_table", has(TFBlocks.UNCRAFTING_TABLE.value()))
				.save(output.withConditions(UncraftingTableCondition.INSTANCE), TwilightForestMod.prefix("uncrafting_table"));

		cookingRecipes(output, "smelted", RecipeSerializer.SMELTING_RECIPE, 200);
		cookingRecipes(output, "smoked", RecipeSerializer.SMOKING_RECIPE, 100);
		cookingRecipes(output, "campfired", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, 600);

		ingotRecipes(output, "smelted", RecipeSerializer.SMELTING_RECIPE, 200);
		ingotRecipes(output, "blasted", RecipeSerializer.BLASTING_RECIPE, 100);

		crackedWoodRecipes(output);
		crackedStoneRecipes(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.EMPTY_CANOPY_BOOKSHELF.value())
				.pattern("---")
				.pattern("   ")
				.pattern("---")
				.define('-', TFBlocks.CANOPY_SLAB.value())
				.unlockedBy("has_item", has(TFBlocks.CANOPY_SLAB.value()))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.CANOPY_BOOKSHELF.value())
				.pattern("---")
				.pattern("B B")
				.pattern("---")
				.define('-', TFBlocks.CANOPY_PLANKS.value())
				.define('B', Items.BOOK)
				.unlockedBy("has_item", has(TFBlocks.CANOPY_PLANKS.value()))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.ARMOR_SHARD_CLUSTER.value())
				.requires(Ingredient.of(TFItems.ARMOR_SHARD.value()), 9)
				.unlockedBy("has_item", has(TFItems.ARMOR_SHARD.value()))
				.save(output, TwilightForestMod.prefix("material/" + TFItems.ARMOR_SHARD_CLUSTER.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_UNDERBRICK.value(), 1)
				.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
				.requires(Ingredient.of(TFBlocks.UNDERBRICK.value()))
				.unlockedBy("has_item", has(TFBlocks.UNDERBRICK.value()))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_MAZESTONE.value(), 1)
				.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
				.requires(Ingredient.of(TFBlocks.MAZESTONE_BRICK.value()))
				.unlockedBy("has_item", has(TFBlocks.MAZESTONE_BRICK.value()))
				.save(output, TwilightForestMod.prefix("maze_stone/mossy_mazestone"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.CARMINITE.value())
				.requires(Ingredient.of(TFItems.BORER_ESSENCE.value()))
				.requires(Tags.Items.DUSTS_REDSTONE)
				.requires(Ingredient.of(TFItems.BORER_ESSENCE.value()))
				.requires(Tags.Items.DUSTS_REDSTONE)
				.requires(Ingredient.of(Items.GHAST_TEAR))
				.requires(Tags.Items.DUSTS_REDSTONE)
				.requires(Ingredient.of(TFItems.BORER_ESSENCE.value()))
				.requires(Tags.Items.DUSTS_REDSTONE)
				.requires(Ingredient.of(TFItems.BORER_ESSENCE.value()))
				.unlockedBy("has_item", has(TFItems.BORER_ESSENCE.value()))
				.save(output, TwilightForestMod.prefix("material/" + TFItems.CARMINITE.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.RAW_IRONWOOD.value(), 2)
				.requires(Ingredient.of(TFItems.LIVEROOT.value()))
				.requires(Ingredient.of(Items.RAW_IRON))
				.requires(Tags.Items.NUGGETS_GOLD)
				.unlockedBy("has_item", has(TFItems.LIVEROOT.value()))
				.save(output, TwilightForestMod.prefix("material/" + TFItems.RAW_IRONWOOD.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.RAINY_CLOUD.value(), 8)
				.pattern("ccc")
				.pattern("cbc")
				.pattern("ccc")
				.define('c', Ingredient.of(TFBlocks.FLUFFY_CLOUD.value()))
				.define('b', Ingredient.of(Items.WATER_BUCKET))
				.unlockedBy("has_item", has(TFBlocks.FLUFFY_CLOUD.value()))
				.save(output, TwilightForestMod.prefix("rainy_cloud"));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.SNOWY_CLOUD.value(), 8)
				.pattern("ccc")
				.pattern("cbc")
				.pattern("ccc")
				.define('c', Ingredient.of(TFBlocks.FLUFFY_CLOUD.value()))
				.define('b', Ingredient.of(Items.POWDER_SNOW_BUCKET))
				.unlockedBy("has_item", has(TFBlocks.FLUFFY_CLOUD.value()))
				.save(output, TwilightForestMod.prefix("snowy_cloud"));
	}

	private void darkTowerRecipes(RecipeOutput output) {
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.ENCASED_FIRE_JET.value())
				.pattern("#∴#")
				.pattern("∴^∴")
				.pattern("uuu")
				.define('∴', Tags.Items.DUSTS_REDSTONE)
				.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.value()))
				.define('^', Ingredient.of(TFBlocks.FIRE_JET.value()))
				.define('u', Ingredient.of(Items.LAVA_BUCKET))
				.unlockedBy("has_item", has(TFBlocks.FIRE_JET.value()))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.ENCASED_SMOKER.value())
				.pattern("#∴#")
				.pattern("∴^∴")
				.pattern("#∴#")
				.define('∴', Tags.Items.DUSTS_REDSTONE)
				.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.value()))
				.define('^', Ingredient.of(TFBlocks.SMOKER.value()))
				.unlockedBy("has_item", has(TFBlocks.SMOKER.value()))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.CARMINITE_BUILDER.value())
				.pattern("#6#")
				.pattern("6o6")
				.pattern("#6#")
				.define('6', ItemTagGenerator.CARMINITE_GEMS)
				.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.value()))
				.define('o', Ingredient.of(Blocks.DISPENSER))
				.unlockedBy("has_item", has(ItemTagGenerator.CARMINITE_GEMS))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.CARMINITE_REACTOR.value())
				.pattern("#6#")
				.pattern("6%6")
				.pattern("#6#")
				.define('6', ItemTagGenerator.CARMINITE_GEMS)
				.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.value()))
				.define('%', Tags.Items.ORES_REDSTONE)
				.unlockedBy("has_item", has(ItemTagGenerator.CARMINITE_GEMS))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.REAPPEARING_BLOCK.value(), 2)
				.pattern("#∴#")
				.pattern("∴6∴")
				.pattern("#∴#")
				.define('∴', Tags.Items.DUSTS_REDSTONE)
				.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.value()))
				.define('6', ItemTagGenerator.CARMINITE_GEMS)
				.unlockedBy("has_item", has(TFBlocks.REAPPEARING_BLOCK.value()))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, TFBlocks.VANISHING_BLOCK.value(), 8)
				.pattern("#w#")
				.pattern("w6w")
				.pattern("#w#")
				.define('w', ItemTagGenerator.TOWERWOOD)
				.define('#', Ingredient.of(TFBlocks.ENCASED_TOWERWOOD.value()))
				.define('6', ItemTagGenerator.CARMINITE_GEMS)
				.unlockedBy("has_item", has(TFBlocks.REAPPEARING_BLOCK.value()))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_TOWERWOOD.value())
				.requires(Ingredient.of(TFBlocks.TOWERWOOD.value()))
				.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
				.unlockedBy("has_item", has(TFBlocks.TOWERWOOD.value()))
				.save(output, TwilightForestMod.prefix("wood/" + TFBlocks.MOSSY_TOWERWOOD.getId().getPath()));

	}

	private void equipmentRecipes(RecipeOutput output) {
		bootsItem(output, "arctic_boots", TFItems.ARCTIC_BOOTS, ItemTagGenerator.ARCTIC_FUR);
		chestplateItem(output, "arctic_chestplate", TFItems.ARCTIC_CHESTPLATE, ItemTagGenerator.ARCTIC_FUR);
		helmetItem(output, "arctic_helmet", TFItems.ARCTIC_HELMET, ItemTagGenerator.ARCTIC_FUR);
		leggingsItem(output, "arctic_leggings", TFItems.ARCTIC_LEGGINGS, ItemTagGenerator.ARCTIC_FUR);

		bootsItem(output, "fiery_boots", TFItems.FIERY_BOOTS, ItemTagGenerator.FIERY_INGOTS);
		chestplateItem(output, "fiery_chestplate", TFItems.FIERY_CHESTPLATE, ItemTagGenerator.FIERY_INGOTS);
		helmetItem(output, "fiery_helmet", TFItems.FIERY_HELMET, ItemTagGenerator.FIERY_INGOTS);
		leggingsItem(output, "fiery_leggings", TFItems.FIERY_LEGGINGS, ItemTagGenerator.FIERY_INGOTS);
		swordItem(output, "fiery_sword", TFItems.FIERY_SWORD, ItemTagGenerator.FIERY_INGOTS, Tags.Items.RODS_BLAZE);
		pickaxeItem(output, "fiery_pickaxe", TFItems.FIERY_PICKAXE, ItemTagGenerator.FIERY_INGOTS, Tags.Items.RODS_BLAZE);

		bootsItem(output, "knightmetal_boots", TFItems.KNIGHTMETAL_BOOTS, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		chestplateItem(output, "knightmetal_chestplate", TFItems.KNIGHTMETAL_CHESTPLATE, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		helmetItem(output, "knightmetal_helmet", TFItems.KNIGHTMETAL_HELMET, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		leggingsItem(output, "knightmetal_leggings", TFItems.KNIGHTMETAL_LEGGINGS, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		pickaxeItem(output, "knightmetal_pickaxe", TFItems.KNIGHTMETAL_PICKAXE, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
		swordItem(output, "knightmetal_sword", TFItems.KNIGHTMETAL_SWORD, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);
		axeItem(output, "knightmetal_axe", TFItems.KNIGHTMETAL_AXE, ItemTagGenerator.KNIGHTMETAL_INGOTS, Tags.Items.RODS_WOODEN);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TFItems.GIANT_PICKAXE.value())
				.pattern("###")
				.pattern(" X ")
				.pattern(" X ")
				.define('#', TFBlocks.GIANT_COBBLESTONE.value())
				.define('X', TFBlocks.GIANT_LOG.value())
				.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE.value()))
				.save(output, locEquip(TFItems.GIANT_PICKAXE.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TFItems.GIANT_SWORD.value())
				.pattern("#")
				.pattern("#")
				.pattern("X")
				.define('#', TFBlocks.GIANT_COBBLESTONE.value())
				.define('X', TFBlocks.GIANT_LOG.value())
				.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE.value()))
				.save(output, locEquip(TFItems.GIANT_SWORD.getId().getPath()));

		charmRecipe(output, "charm_of_keeping_2", TFItems.CHARM_OF_KEEPING_2, TFItems.CHARM_OF_KEEPING_1);
		charmRecipe(output, "charm_of_keeping_3", TFItems.CHARM_OF_KEEPING_3, TFItems.CHARM_OF_KEEPING_2);
		charmRecipe(output, "charm_of_life_2", TFItems.CHARM_OF_LIFE_2, TFItems.CHARM_OF_LIFE_1);

		SpecialRecipeBuilder.special(TFRecipes.MOONWORM_QUEEN_REPAIR_RECIPE.value()).save(output, TwilightForestMod.prefix("moonworm_queen_repair_recipe").toString());
		SpecialRecipeBuilder.special(TFRecipes.MAGIC_MAP_CLONING_RECIPE.value()).save(output, TwilightForestMod.prefix("magic_map_cloning_recipe").toString());
		SpecialRecipeBuilder.special(TFRecipes.MAZE_MAP_CLONING_RECIPE.value()).save(output, TwilightForestMod.prefix("maze_map_cloning_recipe").toString());

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE, 64)
				.requires(TFBlocks.GIANT_COBBLESTONE.value())
				.unlockedBy("has_item", has(TFBlocks.GIANT_COBBLESTONE.value()))
				.save(output, TwilightForestMod.prefix(TFBlocks.GIANT_COBBLESTONE.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.COBBLESTONE).getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_PLANKS, 64)
				.requires(TFBlocks.GIANT_LOG.value())
				.unlockedBy("has_item", has(TFBlocks.GIANT_LOG.value()))
				.save(output, TwilightForestMod.prefix(TFBlocks.GIANT_LOG.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OAK_PLANKS).getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_LEAVES, 64)
				.requires(TFBlocks.GIANT_LEAVES.value())
				.unlockedBy("has_item", has(TFBlocks.GIANT_LEAVES.value()))
				.save(output, TwilightForestMod.prefix(TFBlocks.GIANT_LEAVES.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OAK_LEAVES).getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.OBSIDIAN, 64)
				.requires(TFBlocks.GIANT_OBSIDIAN.value())
				.unlockedBy("has_item", has(TFBlocks.GIANT_OBSIDIAN.value()))
				.save(output, TwilightForestMod.prefix(TFBlocks.GIANT_OBSIDIAN.getId().getPath() + "_to_" + BuiltInRegistries.ITEM.getKey(Items.OBSIDIAN).getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, TFItems.BLOCK_AND_CHAIN.value())
				.requires(Ingredient.of(ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL))
				.requires(Ingredient.of(ItemTagGenerator.KNIGHTMETAL_INGOTS), 3)
				.requires(Ingredient.of(TFItems.KNIGHTMETAL_RING.value()))
				.unlockedBy("has_block", has(ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL))
				.unlockedBy("has_ingot", has(ItemTagGenerator.KNIGHTMETAL_INGOTS))
				.unlockedBy("has_ring", has(TFItems.KNIGHTMETAL_RING.value()))
				.save(output, locEquip(TFItems.BLOCK_AND_CHAIN.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFItems.KNIGHTMETAL_RING.value())
				.pattern(" - ")
				.pattern("- -")
				.pattern(" - ")
				.define('-', ItemTagGenerator.KNIGHTMETAL_INGOTS)
				.unlockedBy("has_item", has(ItemTagGenerator.KNIGHTMETAL_INGOTS))
				.save(output, locEquip(TFItems.KNIGHTMETAL_RING.getId().getPath()));

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TFItems.KNIGHTMETAL_SHIELD.value())
				.pattern("-#")
				.pattern("-o")
				.pattern("-#")
				.define('-', ItemTagGenerator.KNIGHTMETAL_INGOTS)
				.define('#', ItemTagGenerator.TOWERWOOD)
				.define('o', Ingredient.of(TFItems.KNIGHTMETAL_RING.value()))
				.unlockedBy("has_ingot", has(ItemTagGenerator.KNIGHTMETAL_INGOTS))
				.unlockedBy("has_ring", has(TFItems.KNIGHTMETAL_RING.value()))
				.save(output, locEquip(TFItems.KNIGHTMETAL_SHIELD.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, TFItems.LIFEDRAIN_SCEPTER.value())
				.requires(scepter(TFItems.LIFEDRAIN_SCEPTER.value()))
				.requires(Ingredient.of(Items.FERMENTED_SPIDER_EYE))
				.unlockedBy("has_item", has(TFItems.LIFEDRAIN_SCEPTER.value()))
				.save(output, locEquip(TFItems.LIFEDRAIN_SCEPTER.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, TFItems.FORTIFICATION_SCEPTER.value())
				.requires(scepter(TFItems.FORTIFICATION_SCEPTER.value()))
				.requires(Ingredient.of(Items.GOLDEN_APPLE))
				.unlockedBy("has_item", has(TFItems.FORTIFICATION_SCEPTER.value()))
				.save(output, locEquip(TFItems.FORTIFICATION_SCEPTER.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, TFItems.TWILIGHT_SCEPTER.value())
				.requires(scepter(TFItems.TWILIGHT_SCEPTER.value()))
				.requires(Tags.Items.ENDER_PEARLS)
				.unlockedBy("has_item", has(TFItems.TWILIGHT_SCEPTER.value()))
				.save(output, locEquip(TFItems.TWILIGHT_SCEPTER.getId().getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, TFItems.ZOMBIE_SCEPTER.value())
				.requires(CompoundIngredient.of(potion(Potions.STRENGTH), potion(Potions.STRONG_STRENGTH), potion(Potions.LONG_STRENGTH)))
				.requires(scepter(TFItems.ZOMBIE_SCEPTER.value()))
				.requires(Ingredient.of(Items.ROTTEN_FLESH))
				.unlockedBy("has_item", has(TFItems.ZOMBIE_SCEPTER.value()))
				.save(output, locEquip(TFItems.ZOMBIE_SCEPTER.getId().getPath()));
	}

	private void blockCompressionRecipes(RecipeOutput output) {
		reverseCompressBlock(output, "arctic_block_to_item", TFItems.ARCTIC_FUR, ItemTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR);
		reverseCompressBlock(output, "carminite_block_to_item", TFItems.CARMINITE, ItemTagGenerator.STORAGE_BLOCKS_CARMINITE);
		reverseCompressBlock(output, "fiery_block_to_ingot", TFItems.FIERY_INGOT, ItemTagGenerator.STORAGE_BLOCKS_FIERY);
		reverseCompressBlock(output, "ironwood_block_ingot", TFItems.IRONWOOD_INGOT, ItemTagGenerator.STORAGE_BLOCKS_IRONWOOD);
		reverseCompressBlock(output, "knightmetal_block_ingot", TFItems.KNIGHTMETAL_INGOT, ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL);
		reverseCompressBlock(output, "steeleaf_block_ingot", TFItems.STEELEAF_INGOT, ItemTagGenerator.STORAGE_BLOCKS_STEELEAF);

		compressedBlock(output, "arctic_block", TFBlocks.ARCTIC_FUR_BLOCK, ItemTagGenerator.ARCTIC_FUR);
		compressedBlock(output, "carminite_block", TFBlocks.CARMINITE_BLOCK, ItemTagGenerator.CARMINITE_GEMS);
		compressedBlock(output, "fiery_block", TFBlocks.FIERY_BLOCK, ItemTagGenerator.FIERY_INGOTS);
		compressedBlock(output, "ironwood_block", TFBlocks.IRONWOOD_BLOCK, ItemTagGenerator.IRONWOOD_INGOTS);
		compressedBlock(output, "knightmetal_block", TFBlocks.KNIGHTMETAL_BLOCK, ItemTagGenerator.KNIGHTMETAL_INGOTS);
		compressedBlock(output, "steeleaf_block", TFBlocks.STEELEAF_BLOCK, ItemTagGenerator.STEELEAF_INGOTS);
	}

	private void emptyMapRecipes(RecipeOutput output) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.MAGIC_MAP_FOCUS.value())
				.requires(TFItems.RAVEN_FEATHER.value())
				.requires(TFItems.TORCHBERRIES.value())
				.requires(Tags.Items.DUSTS_GLOWSTONE)
				.unlockedBy("has_berries", has(TFItems.TORCHBERRIES.value()))
				.unlockedBy("has_feather", has(TFItems.RAVEN_FEATHER.value()))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFItems.MAGIC_MAP.value())
				.pattern("###")
				.pattern("#•#")
				.pattern("###")
				.define('#', ItemTagGenerator.PAPER)
				.define('•', Ingredient.of(TFItems.MAGIC_MAP_FOCUS.value()))
				.unlockedBy("has_item", has(TFItems.MAGIC_MAP_FOCUS.value()))
				.save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TFItems.MAZE_MAP.value())
				.pattern("###")
				.pattern("#•#")
				.pattern("###")
				.define('#', ItemTagGenerator.PAPER)
				.define('•', Ingredient.of(TFItems.MAZE_MAP_FOCUS.value()))
				.unlockedBy("has_item", has(TFItems.MAZE_MAP_FOCUS.value()))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.ORE_MAP.value())
				.requires(TFItems.MAZE_MAP.value())
				.requires(Tags.Items.STORAGE_BLOCKS_DIAMOND)
				.requires(Tags.Items.STORAGE_BLOCKS_GOLD)
				.requires(Tags.Items.STORAGE_BLOCKS_IRON)
				.unlockedBy("has_item", has(TFItems.MAZE_MAP.value()))
				.save(output);
	}

	private void woodRecipes(RecipeOutput output) {
		buttonBlock(output, "canopy", TFBlocks.CANOPY_BUTTON, TFBlocks.CANOPY_PLANKS);
		buttonBlock(output, "dark", TFBlocks.DARK_BUTTON, TFBlocks.DARK_PLANKS);
		buttonBlock(output, "mangrove", TFBlocks.MANGROVE_BUTTON, TFBlocks.MANGROVE_PLANKS);
		buttonBlock(output, "mining", TFBlocks.MINING_BUTTON, TFBlocks.MINING_PLANKS);
		buttonBlock(output, "sorting", TFBlocks.SORTING_BUTTON, TFBlocks.SORTING_PLANKS);
		buttonBlock(output, "time", TFBlocks.TIME_BUTTON, TFBlocks.TIME_PLANKS);
		buttonBlock(output, "transformation", TFBlocks.TRANSFORMATION_BUTTON, TFBlocks.TRANSFORMATION_PLANKS);
		buttonBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_BUTTON, TFBlocks.TWILIGHT_OAK_PLANKS);

		doorBlock(output, "canopy", TFBlocks.CANOPY_DOOR, TFBlocks.CANOPY_PLANKS);
		doorBlock(output, "dark", TFBlocks.DARK_DOOR, TFBlocks.DARK_PLANKS);
		doorBlock(output, "mangrove", TFBlocks.MANGROVE_DOOR, TFBlocks.MANGROVE_PLANKS);
		doorBlock(output, "mining", TFBlocks.MINING_DOOR, TFBlocks.MINING_PLANKS);
		doorBlock(output, "sorting", TFBlocks.SORTING_DOOR, TFBlocks.SORTING_PLANKS);
		doorBlock(output, "time", TFBlocks.TIME_DOOR, TFBlocks.TIME_PLANKS);
		doorBlock(output, "transformation", TFBlocks.TRANSFORMATION_DOOR, TFBlocks.TRANSFORMATION_PLANKS);
		doorBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_DOOR, TFBlocks.TWILIGHT_OAK_PLANKS);

		fenceBlock(output, "canopy", TFBlocks.CANOPY_FENCE, TFBlocks.CANOPY_PLANKS);
		fenceBlock(output, "dark", TFBlocks.DARK_FENCE, TFBlocks.DARK_PLANKS);
		fenceBlock(output, "mangrove", TFBlocks.MANGROVE_FENCE, TFBlocks.MANGROVE_PLANKS);
		fenceBlock(output, "mining", TFBlocks.MINING_FENCE, TFBlocks.MINING_PLANKS);
		fenceBlock(output, "sorting", TFBlocks.SORTING_FENCE, TFBlocks.SORTING_PLANKS);
		fenceBlock(output, "time", TFBlocks.TIME_FENCE, TFBlocks.TIME_PLANKS);
		fenceBlock(output, "transformation", TFBlocks.TRANSFORMATION_FENCE, TFBlocks.TRANSFORMATION_PLANKS);
		fenceBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_FENCE, TFBlocks.TWILIGHT_OAK_PLANKS);

		gateBlock(output, "canopy", TFBlocks.CANOPY_GATE, TFBlocks.CANOPY_PLANKS);
		gateBlock(output, "dark", TFBlocks.DARK_GATE, TFBlocks.DARK_PLANKS);
		gateBlock(output, "mangrove", TFBlocks.MANGROVE_GATE, TFBlocks.MANGROVE_PLANKS);
		gateBlock(output, "mining", TFBlocks.MINING_GATE, TFBlocks.MINING_PLANKS);
		gateBlock(output, "sorting", TFBlocks.SORTING_GATE, TFBlocks.SORTING_PLANKS);
		gateBlock(output, "time", TFBlocks.TIME_GATE, TFBlocks.TIME_PLANKS);
		gateBlock(output, "transformation", TFBlocks.TRANSFORMATION_GATE, TFBlocks.TRANSFORMATION_PLANKS);
		gateBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_GATE, TFBlocks.TWILIGHT_OAK_PLANKS);

		planksBlock(output, "canopy", TFBlocks.CANOPY_PLANKS, ItemTagGenerator.CANOPY_LOGS);
		planksBlock(output, "dark", TFBlocks.DARK_PLANKS, ItemTagGenerator.DARKWOOD_LOGS);
		planksBlock(output, "mangrove", TFBlocks.MANGROVE_PLANKS, ItemTagGenerator.MANGROVE_LOGS);
		planksBlock(output, "mining", TFBlocks.MINING_PLANKS, ItemTagGenerator.MINING_LOGS);
		planksBlock(output, "sorting", TFBlocks.SORTING_PLANKS, ItemTagGenerator.SORTING_LOGS);
		planksBlock(output, "time", TFBlocks.TIME_PLANKS, ItemTagGenerator.TIME_LOGS);
		planksBlock(output, "transformation", TFBlocks.TRANSFORMATION_PLANKS, ItemTagGenerator.TRANSFORMATION_LOGS);
		planksBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_PLANKS, ItemTagGenerator.TWILIGHT_OAK_LOGS);

		woodBlock(output, "canopy", TFBlocks.CANOPY_WOOD, TFBlocks.CANOPY_LOG);
		woodBlock(output, "dark", TFBlocks.DARK_WOOD, TFBlocks.DARK_LOG);
		woodBlock(output, "mangrove", TFBlocks.MANGROVE_WOOD, TFBlocks.MANGROVE_LOG);
		woodBlock(output, "mining", TFBlocks.MINING_WOOD, TFBlocks.MINING_LOG);
		woodBlock(output, "sorting", TFBlocks.SORTING_WOOD, TFBlocks.SORTING_LOG);
		woodBlock(output, "time", TFBlocks.TIME_WOOD, TFBlocks.TIME_LOG);
		woodBlock(output, "transformation", TFBlocks.TRANSFORMATION_WOOD, TFBlocks.TRANSFORMATION_LOG);
		woodBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_WOOD, TFBlocks.TWILIGHT_OAK_LOG);

		strippedWoodBlock(output, "canopy", TFBlocks.STRIPPED_CANOPY_WOOD, TFBlocks.STRIPPED_CANOPY_LOG);
		strippedWoodBlock(output, "dark", TFBlocks.STRIPPED_DARK_WOOD, TFBlocks.STRIPPED_DARK_LOG);
		strippedWoodBlock(output, "mangrove", TFBlocks.STRIPPED_MANGROVE_WOOD, TFBlocks.STRIPPED_MANGROVE_LOG);
		strippedWoodBlock(output, "mining", TFBlocks.STRIPPED_MINING_WOOD, TFBlocks.STRIPPED_MINING_LOG);
		strippedWoodBlock(output, "sorting", TFBlocks.STRIPPED_SORTING_WOOD, TFBlocks.STRIPPED_SORTING_LOG);
		strippedWoodBlock(output, "time", TFBlocks.STRIPPED_TIME_WOOD, TFBlocks.STRIPPED_TIME_LOG);
		strippedWoodBlock(output, "transformation", TFBlocks.STRIPPED_TRANSFORMATION_WOOD, TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		strippedWoodBlock(output, "twilight_oak", TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);

		plateBlock(output, "canopy", TFBlocks.CANOPY_PLATE, TFBlocks.CANOPY_PLANKS);
		plateBlock(output, "dark", TFBlocks.DARK_PLATE, TFBlocks.DARK_PLANKS);
		plateBlock(output, "mangrove", TFBlocks.MANGROVE_PLATE, TFBlocks.MANGROVE_PLANKS);
		plateBlock(output, "mining", TFBlocks.MINING_PLATE, TFBlocks.MINING_PLANKS);
		plateBlock(output, "sorting", TFBlocks.SORTING_PLATE, TFBlocks.SORTING_PLANKS);
		plateBlock(output, "time", TFBlocks.TIME_PLATE, TFBlocks.TIME_PLANKS);
		plateBlock(output, "transformation", TFBlocks.TRANSFORMATION_PLATE, TFBlocks.TRANSFORMATION_PLANKS);
		plateBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_PLATE, TFBlocks.TWILIGHT_OAK_PLANKS);

		slabBlock(output, "canopy", TFBlocks.CANOPY_SLAB, TFBlocks.CANOPY_PLANKS);
		slabBlock(output, "dark", TFBlocks.DARK_SLAB, TFBlocks.DARK_PLANKS);
		slabBlock(output, "mangrove", TFBlocks.MANGROVE_SLAB, TFBlocks.MANGROVE_PLANKS);
		slabBlock(output, "mining", TFBlocks.MINING_SLAB, TFBlocks.MINING_PLANKS);
		slabBlock(output, "sorting", TFBlocks.SORTING_SLAB, TFBlocks.SORTING_PLANKS);
		slabBlock(output, "time", TFBlocks.TIME_SLAB, TFBlocks.TIME_PLANKS);
		slabBlock(output, "transformation", TFBlocks.TRANSFORMATION_SLAB, TFBlocks.TRANSFORMATION_PLANKS);
		slabBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_SLAB, TFBlocks.TWILIGHT_OAK_PLANKS);

		stairsBlock(output, locWood("canopy_stairs"), TFBlocks.CANOPY_STAIRS, TFBlocks.CANOPY_PLANKS, TFBlocks.CANOPY_PLANKS.value());
		stairsBlock(output, locWood("dark_stairs"), TFBlocks.DARK_STAIRS, TFBlocks.DARK_PLANKS, TFBlocks.DARK_PLANKS.value());
		stairsBlock(output, locWood("mangrove_stairs"), TFBlocks.MANGROVE_STAIRS, TFBlocks.MANGROVE_PLANKS, TFBlocks.MANGROVE_PLANKS.value());
		stairsBlock(output, locWood("mining_stairs"), TFBlocks.MINING_STAIRS, TFBlocks.MINING_PLANKS, TFBlocks.MINING_PLANKS.value());
		stairsBlock(output, locWood("sorting_stairs"), TFBlocks.SORTING_STAIRS, TFBlocks.SORTING_PLANKS, TFBlocks.SORTING_PLANKS.value());
		stairsBlock(output, locWood("time_stairs"), TFBlocks.TIME_STAIRS, TFBlocks.TIME_PLANKS, TFBlocks.TIME_PLANKS.value());
		stairsBlock(output, locWood("transformation_stairs"), TFBlocks.TRANSFORMATION_STAIRS, TFBlocks.TRANSFORMATION_PLANKS, TFBlocks.TRANSFORMATION_PLANKS.value());
		stairsBlock(output, locWood("twilight_oak_stairs"), TFBlocks.TWILIGHT_OAK_STAIRS, TFBlocks.TWILIGHT_OAK_PLANKS, TFBlocks.TWILIGHT_OAK_PLANKS.value());

		trapdoorBlock(output, "canopy", TFBlocks.CANOPY_TRAPDOOR, TFBlocks.CANOPY_PLANKS);
		trapdoorBlock(output, "dark", TFBlocks.DARK_TRAPDOOR, TFBlocks.DARK_PLANKS);
		trapdoorBlock(output, "mangrove", TFBlocks.MANGROVE_TRAPDOOR, TFBlocks.MANGROVE_PLANKS);
		trapdoorBlock(output, "mining", TFBlocks.MINING_TRAPDOOR, TFBlocks.MINING_PLANKS);
		trapdoorBlock(output, "sorting", TFBlocks.SORTING_TRAPDOOR, TFBlocks.SORTING_PLANKS);
		trapdoorBlock(output, "time", TFBlocks.TIME_TRAPDOOR, TFBlocks.TIME_PLANKS);
		trapdoorBlock(output, "transformation", TFBlocks.TRANSFORMATION_TRAPDOOR, TFBlocks.TRANSFORMATION_PLANKS);
		trapdoorBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_TRAPDOOR, TFBlocks.TWILIGHT_OAK_PLANKS);

		signBlock(output, "canopy", TFItems.CANOPY_SIGN, TFBlocks.CANOPY_PLANKS);
		signBlock(output, "dark", TFItems.DARK_SIGN, TFBlocks.DARK_PLANKS);
		signBlock(output, "mangrove", TFItems.MANGROVE_SIGN, TFBlocks.MANGROVE_PLANKS);
		signBlock(output, "mining", TFItems.MINING_SIGN, TFBlocks.MINING_PLANKS);
		signBlock(output, "sorting", TFItems.SORTING_SIGN, TFBlocks.SORTING_PLANKS);
		signBlock(output, "time", TFItems.TIME_SIGN, TFBlocks.TIME_PLANKS);
		signBlock(output, "transformation", TFItems.TRANSFORMATION_SIGN, TFBlocks.TRANSFORMATION_PLANKS);
		signBlock(output, "twilight_oak", TFItems.TWILIGHT_OAK_SIGN, TFBlocks.TWILIGHT_OAK_PLANKS);

		hangingSignBlock(output, "canopy", TFItems.CANOPY_HANGING_SIGN, TFBlocks.STRIPPED_CANOPY_LOG);
		hangingSignBlock(output, "dark", TFItems.DARK_HANGING_SIGN, TFBlocks.STRIPPED_DARK_LOG);
		hangingSignBlock(output, "mangrove", TFItems.MANGROVE_HANGING_SIGN, TFBlocks.STRIPPED_MANGROVE_LOG);
		hangingSignBlock(output, "mining", TFItems.MINING_HANGING_SIGN, TFBlocks.STRIPPED_MINING_LOG);
		hangingSignBlock(output, "sorting", TFItems.SORTING_HANGING_SIGN, TFBlocks.STRIPPED_SORTING_LOG);
		hangingSignBlock(output, "time", TFItems.TIME_HANGING_SIGN, TFBlocks.STRIPPED_TIME_LOG);
		hangingSignBlock(output, "transformation", TFItems.TRANSFORMATION_HANGING_SIGN, TFBlocks.STRIPPED_TRANSFORMATION_LOG);
		hangingSignBlock(output, "twilight_oak", TFItems.TWILIGHT_OAK_HANGING_SIGN, TFBlocks.STRIPPED_TWILIGHT_OAK_LOG);

		banisterBlock(output, "canopy", TFBlocks.CANOPY_BANISTER, TFBlocks.CANOPY_SLAB);
		banisterBlock(output, "dark", TFBlocks.DARK_BANISTER, TFBlocks.DARK_SLAB);
		banisterBlock(output, "mangrove", TFBlocks.MANGROVE_BANISTER, TFBlocks.MANGROVE_SLAB);
		banisterBlock(output, "mining", TFBlocks.MINING_BANISTER, TFBlocks.MINING_SLAB);
		banisterBlock(output, "sorting", TFBlocks.SORTING_BANISTER, TFBlocks.SORTING_SLAB);
		banisterBlock(output, "time", TFBlocks.TIME_BANISTER, TFBlocks.TIME_SLAB);
		banisterBlock(output, "transformation", TFBlocks.TRANSFORMATION_BANISTER, TFBlocks.TRANSFORMATION_SLAB);
		banisterBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_BANISTER, TFBlocks.TWILIGHT_OAK_SLAB);

		banisterBlock(output, "oak", TFBlocks.OAK_BANISTER, Blocks.OAK_SLAB);
		banisterBlock(output, "spruce", TFBlocks.SPRUCE_BANISTER, Blocks.SPRUCE_SLAB);
		banisterBlock(output, "birch", TFBlocks.BIRCH_BANISTER, Blocks.BIRCH_SLAB);
		banisterBlock(output, "jungle", TFBlocks.JUNGLE_BANISTER, Blocks.JUNGLE_SLAB);
		banisterBlock(output, "acacia", TFBlocks.ACACIA_BANISTER, Blocks.ACACIA_SLAB);
		banisterBlock(output, "dark_oak", TFBlocks.DARK_OAK_BANISTER, Blocks.DARK_OAK_SLAB);
		banisterBlock(output, "crimson", TFBlocks.CRIMSON_BANISTER, Blocks.CRIMSON_SLAB);
		banisterBlock(output, "warped", TFBlocks.WARPED_BANISTER, Blocks.WARPED_SLAB);
		banisterBlock(output, "vangrove", TFBlocks.VANGROVE_BANISTER, Blocks.MANGROVE_SLAB);
		banisterBlock(output, "bamboo", TFBlocks.BAMBOO_BANISTER, Blocks.BAMBOO_SLAB);
		banisterBlock(output, "cherry", TFBlocks.CHERRY_BANISTER, Blocks.CHERRY_SLAB);

		chestBlock(output, "twilight_oak", TFBlocks.TWILIGHT_OAK_CHEST, TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST, TFBlocks.TWILIGHT_OAK_PLANKS);
		chestBlock(output, "canopy", TFBlocks.CANOPY_CHEST, TFBlocks.CANOPY_TRAPPED_CHEST, TFBlocks.CANOPY_PLANKS);
		chestBlock(output, "mangrove", TFBlocks.MANGROVE_CHEST, TFBlocks.MANGROVE_TRAPPED_CHEST, TFBlocks.MANGROVE_PLANKS);
		chestBlock(output, "dark", TFBlocks.DARK_CHEST, TFBlocks.DARK_TRAPPED_CHEST, TFBlocks.DARK_PLANKS);
		chestBlock(output, "time", TFBlocks.TIME_CHEST, TFBlocks.TIME_TRAPPED_CHEST, TFBlocks.TIME_PLANKS);
		chestBlock(output, "transformation", TFBlocks.TRANSFORMATION_CHEST, TFBlocks.TRANSFORMATION_TRAPPED_CHEST, TFBlocks.TRANSFORMATION_PLANKS);
		chestBlock(output, "mining", TFBlocks.MINING_CHEST, TFBlocks.MINING_TRAPPED_CHEST, TFBlocks.MINING_PLANKS);
		chestBlock(output, "sorting", TFBlocks.SORTING_CHEST, TFBlocks.SORTING_TRAPPED_CHEST, TFBlocks.SORTING_PLANKS);

		buildBoats(output, TFItems.TWILIGHT_OAK_BOAT, TFItems.TWILIGHT_OAK_CHEST_BOAT, TFBlocks.TWILIGHT_OAK_PLANKS);
		buildBoats(output, TFItems.CANOPY_BOAT, TFItems.CANOPY_CHEST_BOAT, TFBlocks.CANOPY_PLANKS);
		buildBoats(output, TFItems.MANGROVE_BOAT, TFItems.MANGROVE_CHEST_BOAT, TFBlocks.MANGROVE_PLANKS);
		buildBoats(output, TFItems.DARK_BOAT, TFItems.DARK_CHEST_BOAT, TFBlocks.DARK_PLANKS);
		buildBoats(output, TFItems.TIME_BOAT, TFItems.TIME_CHEST_BOAT, TFBlocks.TIME_PLANKS);
		buildBoats(output, TFItems.TRANSFORMATION_BOAT, TFItems.TRANSFORMATION_CHEST_BOAT, TFBlocks.TRANSFORMATION_PLANKS);
		buildBoats(output, TFItems.MINING_BOAT, TFItems.MINING_CHEST_BOAT, TFBlocks.MINING_PLANKS);
		buildBoats(output, TFItems.SORTING_BOAT, TFItems.SORTING_CHEST_BOAT, TFBlocks.SORTING_PLANKS);
	}

	private void nagastoneRecipes(RecipeOutput output) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.SPIRAL_BRICKS.value(), 8)
				.pattern("BSS")
				.pattern("BSS")
				.pattern("BBB")
				.define('B', Ingredient.of(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))//Ingredient.merge(ImmutableList.of(Ingredient.fromTag(Tags.Items.STONE), Ingredient.fromItems(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS))))
				.define('S', Ingredient.of(Blocks.STONE_SLAB, Blocks.STONE_BRICK_SLAB))
				.unlockedBy("has_item", has(TFBlocks.SPIRAL_BRICKS.value()))
				.save(output, locNaga("nagastone_spiral"));

		stairsBlock(output, locNaga("nagastone_stairs_left"), TFBlocks.NAGASTONE_STAIRS_LEFT, TFBlocks.ETCHED_NAGASTONE, TFBlocks.ETCHED_NAGASTONE.value());
		stairsRightBlock(output, locNaga("nagastone_stairs_right"), TFBlocks.NAGASTONE_STAIRS_RIGHT, TFBlocks.ETCHED_NAGASTONE, TFBlocks.ETCHED_NAGASTONE.value());

		stairsBlock(output, locNaga("mossy_nagastone_stairs_left"), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_ETCHED_NAGASTONE.value());
		stairsRightBlock(output, locNaga("mossy_nagastone_stairs_right"), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT, TFBlocks.MOSSY_ETCHED_NAGASTONE, TFBlocks.MOSSY_ETCHED_NAGASTONE.value());

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_ETCHED_NAGASTONE.value(), 1)
				.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
				.requires(Ingredient.of(TFBlocks.ETCHED_NAGASTONE.value()))
				.unlockedBy("has_item", has(TFBlocks.ETCHED_NAGASTONE.value()))
				.save(output, locNaga("mossy_etched_nagastone"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_NAGASTONE_PILLAR.value(), 1)
				.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
				.requires(Ingredient.of(TFBlocks.NAGASTONE_PILLAR.value()))
				.unlockedBy("has_item", has(TFBlocks.NAGASTONE_PILLAR.value()))
				.save(output, locNaga("mossy_nagastone_pillar"));

		stairsBlock(output, locNaga("cracked_nagastone_stairs_left"), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_ETCHED_NAGASTONE.value());
		stairsRightBlock(output, locNaga("cracked_nagastone_stairs_right"), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT, TFBlocks.CRACKED_ETCHED_NAGASTONE, TFBlocks.CRACKED_ETCHED_NAGASTONE.value());
	}

	private void castleRecipes(RecipeOutput output) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, TFBlocks.MOSSY_CASTLE_BRICK.value(), 1)
				.requires(Ingredient.of(Blocks.VINE, Blocks.MOSS_BLOCK))
				.requires(Ingredient.of(TFBlocks.CASTLE_BRICK.value()))
				.unlockedBy("has_item", has(TFBlocks.CASTLE_BRICK.value()))
				.save(output, locCastle("mossy_castle_brick"));

		castleBlock(output, TFBlocks.THICK_CASTLE_BRICK, TFBlocks.CASTLE_BRICK.value(), TFBlocks.WORN_CASTLE_BRICK.value(), TFBlocks.CRACKED_CASTLE_BRICK.value(), TFBlocks.MOSSY_CASTLE_BRICK.value());
		castleBlock(output, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.THICK_CASTLE_BRICK.value());
		castleBlock(output, TFBlocks.BOLD_CASTLE_BRICK_TILE, TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value());

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value(), 4)
				.pattern("##")
				.pattern("##")
				.define('#', Ingredient.of(TFBlocks.BOLD_CASTLE_BRICK_TILE.value()))
				.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK.value()))
				.save(output, locCastle("bold_castle_pillar_from_tile"));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.value(), 6)
				.pattern("#H#")
				.pattern("#H#")
				.define('#', Ingredient.of(TFBlocks.CASTLE_BRICK.value(), TFBlocks.WORN_CASTLE_BRICK.value(), TFBlocks.CRACKED_CASTLE_BRICK.value(), TFBlocks.MOSSY_CASTLE_BRICK.value(), TFBlocks.THICK_CASTLE_BRICK.value()))
				.define('H', Ingredient.of(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.value(), TFBlocks.ENCASED_CASTLE_BRICK_TILE.value(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value(), TFBlocks.BOLD_CASTLE_BRICK_TILE.value()))
				.unlockedBy("has_castle_brick", has(TFBlocks.CASTLE_BRICK.value()))
				.save(output, locCastle("encased_castle_pillar"));

		stairsBlock(output, locCastle("bold_castle_brick_stairs"), TFBlocks.BOLD_CASTLE_BRICK_STAIRS, TFBlocks.BOLD_CASTLE_BRICK_PILLAR, TFBlocks.BOLD_CASTLE_BRICK_PILLAR.value(), TFBlocks.BOLD_CASTLE_BRICK_TILE.value());
		stairsBlock(output, locCastle("castle_brick_stairs"), TFBlocks.CASTLE_BRICK_STAIRS, TFBlocks.CASTLE_BRICK, TFBlocks.CASTLE_BRICK.value());
		stairsBlock(output, locCastle("cracked_castle_brick_stairs"), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS, TFBlocks.CRACKED_CASTLE_BRICK, TFBlocks.CRACKED_CASTLE_BRICK.value());
		stairsBlock(output, locCastle("encased_castle_brick_stairs"), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR, TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.value(), TFBlocks.ENCASED_CASTLE_BRICK_TILE.value());
		stairsBlock(output, locCastle("mossy_castle_brick_stairs"), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS, TFBlocks.MOSSY_CASTLE_BRICK, TFBlocks.MOSSY_CASTLE_BRICK.value());
		stairsBlock(output, locCastle("worn_castle_brick_stairs"), TFBlocks.WORN_CASTLE_BRICK_STAIRS, TFBlocks.WORN_CASTLE_BRICK, TFBlocks.WORN_CASTLE_BRICK.value());
	}

	private void fieryConversions(RecipeOutput output) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TFItems.FIERY_INGOT.value())
				.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL))
				.requires(Ingredient.of(Tags.Items.INGOTS_IRON))
				.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
				.save(output, locEquip("fiery_ingot_crafting"));

		fieryConversion(output, TFItems.FIERY_HELMET, Items.IRON_HELMET, 5);
		fieryConversion(output, TFItems.FIERY_CHESTPLATE, Items.IRON_CHESTPLATE, 8);
		fieryConversion(output, TFItems.FIERY_LEGGINGS, Items.IRON_LEGGINGS, 7);
		fieryConversion(output, TFItems.FIERY_BOOTS, Items.IRON_BOOTS, 4);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, TFItems.FIERY_SWORD.value())
				.requires(Items.IRON_SWORD)
				.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL), 2)
				.requires(Ingredient.of(Tags.Items.RODS_BLAZE))
				.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
				.save(output, locEquip("fiery_" + BuiltInRegistries.ITEM.getKey(Items.IRON_SWORD).getPath()));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, TFItems.FIERY_PICKAXE.value())
				.requires(Items.IRON_PICKAXE)
				.requires(Ingredient.of(ItemTagGenerator.FIERY_VIAL), 3)
				.requires(Ingredient.of(Tags.Items.RODS_BLAZE), 2)
				.unlockedBy("has_item", has(ItemTagGenerator.FIERY_VIAL))
				.save(output, locEquip("fiery_" + BuiltInRegistries.ITEM.getKey(Items.IRON_PICKAXE).getPath()));
	}

	private void cookingRecipes(RecipeOutput output, String processName, RecipeSerializer<? extends AbstractCookingRecipe> process, int smeltingTime) {
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.RAW_MEEF.value()), RecipeCategory.FOOD, TFItems.COOKED_MEEF.value(), 0.3f, smeltingTime, process).unlockedBy("has_food", has(TFItems.RAW_MEEF.value())).save(output, TwilightForestMod.prefix("food/" + processName + "_meef").toString());
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.RAW_VENISON.value()), RecipeCategory.FOOD, TFItems.COOKED_VENISON.value(), 0.3f, smeltingTime, process).unlockedBy("has_food", has(TFItems.RAW_VENISON.value())).save(output, TwilightForestMod.prefix("food/" + processName + "_venison").toString());
	}

	private void ingotRecipes(RecipeOutput output, String processName, RecipeSerializer<? extends AbstractCookingRecipe> process, int smeltingTime) {
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.ARMOR_SHARD_CLUSTER.value()), RecipeCategory.MISC, TFItems.KNIGHTMETAL_INGOT.value(), 1.0f, smeltingTime, process).unlockedBy("has_item", has(TFItems.ARMOR_SHARD_CLUSTER.value())).save(output, TwilightForestMod.prefix("material/" + processName + "_knightmetal_ingot").toString());
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFItems.RAW_IRONWOOD.value()), RecipeCategory.MISC, TFItems.IRONWOOD_INGOT.value(), 1.0f, smeltingTime, process).unlockedBy("has_item", has(TFItems.RAW_IRONWOOD.value())).save(output, TwilightForestMod.prefix("material/" + processName + "_ironwood_ingot").toString());
	}

	private void crackedWoodRecipes(RecipeOutput output) {
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFBlocks.TOWERWOOD.value()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_TOWERWOOD.value(), 0.3f, 100, RecipeSerializer.SMOKING_RECIPE).unlockedBy("has_item", has(TFBlocks.TOWERWOOD.value())).save(output, TwilightForestMod.prefix("wood/" + "smoked" + "_cracked_towerwood").toString());
	}

	private void crackedStoneRecipes(RecipeOutput output) {
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFBlocks.NAGASTONE_PILLAR.value()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_NAGASTONE_PILLAR.value(), 0.3f, 200, RecipeSerializer.SMELTING_RECIPE).unlockedBy("has_item", has(TFBlocks.NAGASTONE_PILLAR.value())).save(output, TwilightForestMod.prefix("nagastone/" + "smelted" + "_cracked_nagastone_pillar").toString());
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFBlocks.ETCHED_NAGASTONE.value()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_ETCHED_NAGASTONE.value(), 0.3f, 200, RecipeSerializer.SMELTING_RECIPE).unlockedBy("has_item", has(TFBlocks.ETCHED_NAGASTONE.value())).save(output, TwilightForestMod.prefix("nagastone/" + "smelted" + "_cracked_etched_nagastone").toString());
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFBlocks.MAZESTONE_BRICK.value()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_MAZESTONE.value(), 0.3f, 200, RecipeSerializer.SMELTING_RECIPE).unlockedBy("has_item", has(TFBlocks.MAZESTONE_BRICK.value())).save(output, TwilightForestMod.prefix("maze_stone/" + "smelted" + "_maze_stone_cracked").toString());
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFBlocks.CASTLE_BRICK.value()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_CASTLE_BRICK.value(), 0.3f, 200, RecipeSerializer.SMELTING_RECIPE).unlockedBy("has_item", has(TFBlocks.CASTLE_BRICK.value())).save(output, TwilightForestMod.prefix("castleblock/" + "smelted" + "_cracked_castle_brick").toString());
		SimpleCookingRecipeBuilder.generic(Ingredient.of(TFBlocks.UNDERBRICK.value()), RecipeCategory.BUILDING_BLOCKS, TFBlocks.CRACKED_UNDERBRICK.value(), 0.3f, 200, RecipeSerializer.SMELTING_RECIPE).unlockedBy("has_item", has(TFBlocks.UNDERBRICK.value())).save(output, TwilightForestMod.prefix("smelted" + "_cracked_underbrick").toString());
	}
}
