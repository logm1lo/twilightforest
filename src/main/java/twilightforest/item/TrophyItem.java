package twilightforest.item;

import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.ISTER;

import java.util.function.Consumer;

public class TrophyItem extends StandingAndWallBlockItem implements CurioItem {

	public TrophyItem(Block floorBlock, Block wallBlock, Properties properties) {
		super(floorBlock, wallBlock, properties, Direction.DOWN);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(ISTER.CLIENT_ITEM_EXTENSION);
	}
}