package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.feature.trees.HollowTreeFeature;
import twilightforest.world.registration.TreeConfigurations;

import java.util.function.BiConsumer;

public class HollowTreeComponent extends TFStructureComponentOld {

	public HollowTreeComponent(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(TFStructurePieceTypes.TFTree.get(), tag);
	}

	public HollowTreeComponent(int i, int x, int y, int z) {
		super(TFStructurePieceTypes.TFTree.get(), i, x, y, z);
	}


	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
		BiConsumer<BlockPos, BlockState> blockPlacer = (blockPos, blockState) -> level.setBlock(blockPos, blockState, 19);
		HollowTreeFeature.makeHollowTree(level, random, pos, blockPlacer, blockPlacer, blockPlacer, TreeConfigurations.HOLLOW_TREE);
	}
}