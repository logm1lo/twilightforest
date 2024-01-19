package twilightforest.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.*;
import twilightforest.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFRecipes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeViewerConstants {
	public static final int CRUMBLE_HORN_WIDTH = 116;
	public static final int CRUMBLE_HORN_HEIGHT = 54;
	public static final int TRANSFORMATION_POWDER_WIDTH = 116;
	public static final int TRANSFORMATION_POWDER_HEIGHT = 54;
	public static final int UNCRAFTING_WIDTH = 116;
	public static final int UNCRAFTING_HEIGHT = 54;

	public static List<RecipeHolder<? extends CraftingRecipe>> getAllUncraftingRecipes(RecipeManager manager) {
		if (!TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingOnly.get()) { //we only do this if uncrafting is not disabled
			List<RecipeHolder<? extends CraftingRecipe>> recipes = new ArrayList<>(manager.getAllRecipesFor(RecipeType.CRAFTING));
			recipes = recipes.stream().filter(recipe ->
							!recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess()).isEmpty() && //get rid of empty items
									!recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess()).is(ItemTagGenerator.BANNED_UNCRAFTABLES) && //Prevents things that are tagged as banned from showing up
									TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.reverseRecipeBlacklist.get() == TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingRecipes.get().contains(recipe.id().toString()) && //remove disabled recipes
									TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.get() == TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get().contains(recipe.id().getNamespace())) //remove blacklisted mod ids
					.collect(Collectors.toList());
			recipes.removeIf(recipe -> (recipe.value() instanceof ShapelessRecipe && !TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.allowShapelessUncrafting.get()));
			recipes.addAll(manager.getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE.get()));
			return recipes;
		} else {
			return new ArrayList<>(manager.getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE.get()));
		}
	}
}
