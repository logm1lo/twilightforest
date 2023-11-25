package twilightforest.data.custom;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

public class CrumbleHornGenerator extends CrumbleHornProvider {

	public CrumbleHornGenerator(PackOutput output, ExistingFileHelper helper) {
		super(output, TwilightForestMod.ID, helper);
	}

	@Override
	public void registerTransforms() {
		addTransform(Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);
		addTransform(Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS);
		addTransform(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
		addTransform(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.BLACKSTONE);
		addTransform(Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS);
		addTransform(Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS);
		addTransform(Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_TILES);
		addTransform(TFBlocks.MAZESTONE_BRICK.value(), TFBlocks.CRACKED_MAZESTONE.value());
		addTransform(TFBlocks.UNDERBRICK.value(), TFBlocks.CRACKED_UNDERBRICK.value());
		addTransform(TFBlocks.DEADROCK.value(), TFBlocks.CRACKED_DEADROCK.value());
		addTransform(TFBlocks.CRACKED_DEADROCK.value(), TFBlocks.WEATHERED_DEADROCK.value());
		addTransform(TFBlocks.TOWERWOOD.value(), TFBlocks.CRACKED_TOWERWOOD.value());
		addTransform(TFBlocks.CASTLE_BRICK.value(), TFBlocks.CRACKED_CASTLE_BRICK.value());
		addTransform(TFBlocks.CRACKED_CASTLE_BRICK.value(), TFBlocks.WORN_CASTLE_BRICK.value());
		addTransform(TFBlocks.NAGASTONE_PILLAR.value(), TFBlocks.CRACKED_NAGASTONE_PILLAR.value());
		addTransform(TFBlocks.ETCHED_NAGASTONE.value(), TFBlocks.CRACKED_ETCHED_NAGASTONE.value());
		addTransform(TFBlocks.CASTLE_BRICK_STAIRS.value(), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.value());
		addTransform(TFBlocks.NAGASTONE_STAIRS_LEFT.value(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.value());
		addTransform(TFBlocks.NAGASTONE_STAIRS_RIGHT.value(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.value());
		addTransform(Blocks.STONE, Blocks.COBBLESTONE);
		addTransform(Blocks.COBBLESTONE, Blocks.GRAVEL);
		addTransform(Blocks.SANDSTONE, Blocks.SAND);
		addTransform(Blocks.RED_SANDSTONE, Blocks.RED_SAND);
		addTransform(Blocks.GRASS_BLOCK, Blocks.DIRT);
		addTransform(Blocks.PODZOL, Blocks.DIRT);
		addTransform(Blocks.MYCELIUM, Blocks.DIRT);
		addTransform(Blocks.COARSE_DIRT, Blocks.DIRT);
		addTransform(Blocks.ROOTED_DIRT, Blocks.DIRT);
		addTransform(Blocks.OXIDIZED_COPPER, Blocks.WEATHERED_COPPER);
		addTransform(Blocks.WEATHERED_COPPER, Blocks.EXPOSED_COPPER);
		addTransform(Blocks.EXPOSED_COPPER, Blocks.COPPER_BLOCK);
		addTransform(Blocks.OXIDIZED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER);
		addTransform(Blocks.WEATHERED_CUT_COPPER, Blocks.EXPOSED_CUT_COPPER);
		addTransform(Blocks.EXPOSED_CUT_COPPER, Blocks.CUT_COPPER);
		addDissolve(Blocks.GRAVEL);
		addDissolve(Blocks.DIRT);
		addDissolve(Blocks.SAND);
		addDissolve(Blocks.RED_SAND);
		addDissolve(Blocks.CLAY);
		addDissolve(Blocks.ANDESITE);
		addDissolve(Blocks.DIORITE);
		addDissolve(Blocks.GRANITE);
	}
}
