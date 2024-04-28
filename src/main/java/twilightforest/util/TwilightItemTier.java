package twilightforest.util;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.SimpleTier;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

public class TwilightItemTier {
	// TODO Switch ingredients to using tags, except for Glass. That stays empty
	public static final Tier IRONWOOD = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_IRONWOOD_TOOL, 512, 6.5F, 2, 25, () -> Ingredient.of(TFItems.IRONWOOD_INGOT.get()));
	public static final Tier FIERY = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_FIERY_TOOL, 1024, 9F, 4, 10, () -> Ingredient.of(TFItems.FIERY_INGOT.get()));
	public static final Tier STEELEAF = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_STEELEAF_TOOL, 131, 8.0F, 3, 9, () -> Ingredient.of(TFItems.STEELEAF_INGOT.get()));
	public static final Tier KNIGHTMETAL = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_KNIGHTMETAL_TOOL, 512, 8.0F, 3, 8, () -> Ingredient.of(TFItems.KNIGHTMETAL_INGOT.get()));
	public static final Tier GIANT = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_GIANT_TOOL, 1024, 4.0F, 1.0F, 5, () -> Ingredient.of(TFBlocks.GIANT_COBBLESTONE.get()));
	public static final Tier ICE = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_ICE_TOOL, 32, 1.0F, 3.5F, 5, () -> Ingredient.of(Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE)); //there isnt an item tag for ice, what the hell
	public static final Tier GLASS = new SimpleTier(BlockTagGenerator.INCORRECT_FOR_GLASS_TOOL, 1, 1.0F, 36.0F, 30, () -> Ingredient.EMPTY);
}
