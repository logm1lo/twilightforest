package twilightforest.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.*;
import twilightforest.TFConfig;
import twilightforest.compat.emi.recipes.EmiCrumbleHornRecipe;
import twilightforest.compat.emi.recipes.EmiTransformationPowderRecipe;
import twilightforest.compat.emi.recipes.EmiUncraftingRecipe;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFRecipes;
import twilightforest.item.recipe.CrumbleRecipe;
import twilightforest.item.recipe.TransformPowderRecipe;
import twilightforest.item.recipe.UncraftingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EmiEntrypoint
public class TFEmiCompat implements EmiPlugin {
	public static final TFEmiRecipeCategory UNCRAFTING = new TFEmiRecipeCategory("uncrafting", TFBlocks.UNCRAFTING_TABLE);
	public static final TFEmiRecipeCategory CRUMBLE_HORN = new TFEmiRecipeCategory("crumble_horn", TFItems.CRUMBLE_HORN);
	public static final TFEmiRecipeCategory TRANSFORMATION = new TFEmiRecipeCategory("transformation", TFItems.TRANSFORMATION_POWDER);

	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(UNCRAFTING);
		registry.addCategory(CRUMBLE_HORN);
		registry.addCategory(TRANSFORMATION);

		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(TFBlocks.UNCRAFTING_TABLE));
		registry.addWorkstation(UNCRAFTING, EmiStack.of(TFBlocks.UNCRAFTING_TABLE));
		registry.addWorkstation(CRUMBLE_HORN, EmiStack.of(TFItems.CRUMBLE_HORN));
		registry.addWorkstation(TRANSFORMATION, EmiStack.of(TFItems.TRANSFORMATION_POWDER));

		RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
		if (!TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingOnly.get()) { //we only do this if uncrafting is not disabled
			List<RecipeHolder<? extends CraftingRecipe>> recipes = new ArrayList<>(manager.getAllRecipesFor(RecipeType.CRAFTING));
			recipes = recipes.stream().filter(recipe ->
							!recipe.value().getResultItem(registryAccess).isEmpty() && //get rid of empty items
									!recipe.value().getResultItem(registryAccess).is(ItemTagGenerator.BANNED_UNCRAFTABLES) && //Prevents things that are tagged as banned from showing up
									TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.reverseRecipeBlacklist.get() == TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableUncraftingRecipes.get().contains(recipe.id().toString()) && //remove disabled recipes
									TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.flipUncraftingModIdList.get() == TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.blacklistedUncraftingModIds.get().contains(recipe.id().getNamespace())) //remove blacklisted mod ids
					.collect(Collectors.toList());
			recipes.removeIf(recipe -> (recipe.value() instanceof ShapelessRecipe && !TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.allowShapelessUncrafting.get()));
			recipes.addAll(manager.getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE.get()));
			recipes.forEach(craftingRecipe -> registry.addRecipe(new EmiUncraftingRecipe<>(craftingRecipe)));
		} else if (!TFConfig.COMMON_CONFIG.UNCRAFTING_STUFFS.disableEntireTable.get()) {
			List<RecipeHolder<UncraftingRecipe>> recipes = new ArrayList<>(manager.getAllRecipesFor(TFRecipes.UNCRAFTING_RECIPE.get()));
			recipes.forEach(recipe -> registry.addRecipe(new EmiUncraftingRecipe<>(recipe)));
		}
		for (RecipeHolder<CrumbleRecipe> recipe : manager.getAllRecipesFor(TFRecipes.CRUMBLE_RECIPE.get())) {
			registry.addRecipe(new EmiCrumbleHornRecipe(recipe));
		}
		for (RecipeHolder<TransformPowderRecipe> recipe : manager.getAllRecipesFor(TFRecipes.TRANSFORM_POWDER_RECIPE.get())) {
			registry.addRecipe(new EmiTransformationPowderRecipe(recipe));
		}
	}
}
