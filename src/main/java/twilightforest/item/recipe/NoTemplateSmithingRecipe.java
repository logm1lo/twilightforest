package twilightforest.item.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.CommonHooks;
import twilightforest.init.TFRecipes;

import java.util.stream.Stream;

public class NoTemplateSmithingRecipe implements SmithingRecipe {

	private final Ingredient base;
	private final Ingredient addition;
	private final CompoundTag additionalData;

	public NoTemplateSmithingRecipe(Ingredient base, Ingredient addition, CompoundTag additionalData) {
		this.base = base;
		this.addition = addition;
		this.additionalData = additionalData;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(Container container, Level level) {
		if (!container.getItem(0).isEmpty() || !this.base.test(container.getItem(1)) || !this.addition.test(container.getItem(2))) return false;
		ItemStack armor = container.getItem(1);
		if (armor.getTag() != null) {
			for (String key : this.additionalData.getAllKeys()) {
				if (!key.equals(ItemStack.TAG_DAMAGE) && armor.getTag().contains(key)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public ItemStack assemble(Container container, RegistryAccess access) {
		ItemStack itemstack = container.getItem(1).copy();
		CompoundTag compoundtag = container.getItem(1).getTag();
		if (compoundtag != null) {
			if (!this.additionalData.isEmpty()) {
				//this avoids writing new data to the original item
				compoundtag = new CompoundTag().merge(this.additionalData).merge(compoundtag);
			}
			itemstack.setTag(compoundtag.copy());
		}

		return itemstack;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess access) {
		return Util.make(new ItemStack(Items.IRON_CHESTPLATE), stack -> stack.setTag(this.additionalData));
	}

	@Override
	public boolean isTemplateIngredient(ItemStack stack) {
		return stack.isEmpty();
	}

	@Override
	public boolean isBaseIngredient(ItemStack stack) {
		return this.base.test(stack);
	}

	@Override
	public boolean isAdditionIngredient(ItemStack stack) {
		return this.addition.test(stack);
	}

	public Ingredient getBase() {
		return this.base;
	}

	public Ingredient getAddition() {
		return this.addition;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return TFRecipes.NO_TEMPLATE_SMITHING_SERIALIZER.get();
	}

	@Override
	public boolean isIncomplete() {
		return Stream.of(this.base, this.addition).anyMatch(CommonHooks::hasNoElements);
	}

	public static class Serializer implements RecipeSerializer<NoTemplateSmithingRecipe> {
		private static final Codec<NoTemplateSmithingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Ingredient.CODEC.fieldOf("base").forGetter(o -> o.base),
						Ingredient.CODEC.fieldOf("addition").forGetter(o -> o.addition),
						CompoundTag.CODEC.optionalFieldOf("additional_data", new CompoundTag()).forGetter(o -> o.additionalData))
				.apply(instance, NoTemplateSmithingRecipe::new)
		);

		@Override
		public Codec<NoTemplateSmithingRecipe> codec() {
			return CODEC;
		}

		@Override
		public NoTemplateSmithingRecipe fromNetwork(FriendlyByteBuf buf) {
			Ingredient base = Ingredient.fromNetwork(buf);
			Ingredient addition = Ingredient.fromNetwork(buf);
			CompoundTag data = buf.readNbt();
			return new NoTemplateSmithingRecipe(base, addition, data == null ? new CompoundTag() : data);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, NoTemplateSmithingRecipe recipe) {
			recipe.base.toNetwork(buf);
			recipe.addition.toNetwork(buf);
			buf.writeNbt(recipe.additionalData);
		}
	}
}
