package twilightforest.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import twilightforest.entity.projectile.SeekerArrow;

public class SeekerBowItem extends BowItem {

	public SeekerBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow, ItemStack stack) {
		return new SeekerArrow(arrow.level(), (LivingEntity) arrow.getOwner(), new ItemStack(Items.ARROW), stack.copyWithCount(1));
	}
}