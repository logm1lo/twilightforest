package twilightforest.world.components.structures.darktower;

import net.minecraft.world.level.block.Blocks;
import twilightforest.init.TFBlocks;
import twilightforest.world.components.structures.TFStructureDecorator;

public class StructureDecoratorDarkTower extends TFStructureDecorator {

	public StructureDecoratorDarkTower() {
		this.blockState = TFBlocks.TOWERWOOD.value().defaultBlockState();
		this.accentState = TFBlocks.ENCASED_TOWERWOOD.value().defaultBlockState();
		this.fenceState = Blocks.OAK_FENCE.defaultBlockState();
		this.stairState = Blocks.SPRUCE_STAIRS.defaultBlockState();
		this.pillarState = TFBlocks.ENCASED_TOWERWOOD.value().defaultBlockState();
		this.platformState = TFBlocks.ENCASED_TOWERWOOD.value().defaultBlockState();
		this.randomBlocks = new TowerwoodProcessor();
	}

}
