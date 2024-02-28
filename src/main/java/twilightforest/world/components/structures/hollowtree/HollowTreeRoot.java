package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.FeatureLogic;
import twilightforest.util.VoxelBresenhamIterator;

public class HollowTreeRoot extends HollowTreeMedBranch {
	protected HollowTreeRoot(int i, BlockPos src, double length, double angle, double tilt, boolean leafy) {
		this(i, src, FeatureLogic.translate(src, length, angle, tilt), length, angle, tilt, leafy);
	}

	protected HollowTreeRoot(int i, BlockPos src, BlockPos dest, double length, double angle, double tilt, boolean leafy) {
		super(TFStructurePieceTypes.TFHTRo.value(), i, src, dest, branchBoundingBox(src, dest), length, angle, tilt, leafy);
	}

	public HollowTreeRoot(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFHTRo.value(), tag);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		BlockPos rSrc = new BlockPos(this.src.getX() - this.boundingBox.minX(), this.src.getY() - this.boundingBox.minY(), this.src.getZ() - this.boundingBox.minZ());
		BlockPos rDest = new BlockPos(this.dest.getX() - this.boundingBox.minX(), this.dest.getY() - this.boundingBox.minY(), this.dest.getZ() - this.boundingBox.minZ());

		this.drawRootLine(level, writeableBounds, rSrc.getX(), rSrc.getY(), rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), TFBlocks.ROOT_BLOCK.value().defaultBlockState());
		this.drawRootLine(level, writeableBounds, rSrc.getX(), rSrc.getY() - 1, rSrc.getZ(), rDest.getX(), rDest.getY() - 1, rDest.getZ(), TFBlocks.ROOT_BLOCK.value().defaultBlockState());
	}

	/**
	 * Draws a line
	 */
	protected void drawRootLine(WorldGenLevel world, BoundingBox sbb, int x1, int y1, int z1, int x2, int y2, int z2, BlockState blockValue) {
		BlockState wood = TFBlocks.TWILIGHT_OAK_WOOD.value().defaultBlockState();

		for (BlockPos coords : new VoxelBresenhamIterator(new BlockPos.MutableBlockPos(x1, y1, z1), x1, y1, z1)) {
			BlockState block = this.getBlock(world, coords.getX(), coords.getY(), coords.getZ(), sbb);

			// three choices here
			if (block.canBeReplaced() || !block.isCollisionShapeFullBlock(world, coords)) {
				// air, other non-solid, or grass, make wood block
				this.placeBlock(world, wood, coords.getX(), coords.getY(), coords.getZ(), sbb);
			} else if (block.is(BlockTags.LOGS)) {
				// wood, do nothing
			} else {
				// solid, make root block
				this.placeBlock(world, blockValue, coords.getX(), coords.getY(), coords.getZ(), sbb);
			}
		}
	}
}
