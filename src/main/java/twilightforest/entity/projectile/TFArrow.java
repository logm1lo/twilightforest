package twilightforest.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public abstract class TFArrow extends AbstractArrow implements ITFProjectile {

	public TFArrow(EntityType<? extends TFArrow> type, Level level) {
		super(type, level, new ItemStack(Items.ARROW));
	}

	public TFArrow(EntityType<? extends TFArrow> type, Level level, Entity shooter) {
		super(type, level, new ItemStack(Items.ARROW));
		this.setOwner(shooter);
		this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
	}
}
