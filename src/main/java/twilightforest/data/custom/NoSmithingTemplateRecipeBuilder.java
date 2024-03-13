package twilightforest.data.custom;

import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.item.recipe.NoTemplateSmithingRecipe;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class NoSmithingTemplateRecipeBuilder {

	private final RecipeCategory category;
	private final Ingredient base;
	private final Ingredient addition;
	private CompoundTag additionalData = new CompoundTag();
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

	private NoSmithingTemplateRecipeBuilder(RecipeCategory category, Ingredient base, Ingredient addition) {
		this.category = category;
		this.base = base;
		this.addition = addition;
	}

	public static NoSmithingTemplateRecipeBuilder noTemplate(Ingredient base, Ingredient addition, RecipeCategory category) {
		return new NoSmithingTemplateRecipeBuilder(category, base, addition);
	}

	public NoSmithingTemplateRecipeBuilder unlocks(String key, Criterion<?> criterion) {
		this.criteria.put(key, criterion);
		return this;
	}

	public NoSmithingTemplateRecipeBuilder attachData(Consumer<CompoundTag> tag) {
		this.additionalData = Util.make(new CompoundTag(), tag);
		return this;
	}

	public void save(RecipeOutput output, ResourceLocation id) {
		this.ensureValid(id);
		Advancement.Builder advancement$builder = output.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id))
				.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancement$builder::addCriterion);
		NoTemplateSmithingRecipe smithingtrimrecipe = new NoTemplateSmithingRecipe(this.base, this.addition, this.additionalData);
		output.accept(id, smithingtrimrecipe, advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
	}

	private void ensureValid(ResourceLocation location) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + location);
		}
	}
}
