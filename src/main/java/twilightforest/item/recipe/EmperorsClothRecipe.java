package twilightforest.item.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;

public class EmperorsClothRecipe extends CustomRecipe {

	public static final String INVISIBLE_TAG = "twilightforest_emperors_cloth_applied";

	public EmperorsClothRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		boolean foundInk = false;
		boolean foundItem = false;

		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.is(TFItems.EMPERORS_CLOTH.get()) && !foundInk) {
					foundInk = true;
				} else if (!foundItem) {
					if (stack.getItem() instanceof ArmorItem && !stack.hasCraftingRemainingItem() && (stack.getTag() == null || !stack.getTag().contains(INVISIBLE_TAG))) {
						foundItem = true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}

		return foundInk && foundItem;
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
		ItemStack item = ItemStack.EMPTY;

		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem && item.isEmpty()) {
				item = stack;
			}
		}

		ItemStack copy = item.copy();
		copy.getOrCreateTag().putBoolean(INVISIBLE_TAG, true);
		return copy;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.EMPERORS_CLOTH_RECIPE.get();
	}
}
