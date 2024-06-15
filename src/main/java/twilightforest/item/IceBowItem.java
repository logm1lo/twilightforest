package twilightforest.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.entity.projectile.IceArrow;
import twilightforest.util.TFToolMaterials;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class IceBowItem extends BowItem {

	public IceBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow, ItemStack stack) {
		return new IceArrow(arrow.level(), (LivingEntity) arrow.getOwner(), new ItemStack(Items.ARROW), stack.copyWithCount(1));
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repairWith) {
		return TFToolMaterials.ICE.getRepairIngredient().test(repairWith);
	}
}