package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.FeatureLogic;

public class HollowTreeSmallBranch extends
		HollowTreeMedBranch {

	protected HollowTreeSmallBranch(int i, BlockPos src, double length, double angle, double tilt, boolean leafy) {
		super(TFStructurePieceTypes.TFHTSB.value(), i, src, FeatureLogic.translate(src, length, angle, tilt), length, angle, tilt, leafy);
	}

	public HollowTreeSmallBranch(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFHTSB.value(), tag);
	}

	/**
	 * Add a leaf ball to the end
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, RandomSource rand) {
//		int index = getComponentType();
//
//		if (leafy) {
//			int leafRad = rand.nextInt(2) + 1;
//			ComponentTFLeafSphere leafBlob = new ComponentTFLeafSphere(index + 1, dest.posX, dest.posY, dest.posZ, leafRad);
//	        list.add(leafBlob);
//	        leafBlob.buildComponent(this, list, rand); // doesn't really need to be here for leaves.
//		}
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		BlockPos rSrc = new BlockPos(this.src.getX() - this.boundingBox.minX(), this.src.getY() - this.boundingBox.minY(), this.src.getZ() - this.boundingBox.minZ());
		BlockPos rDest = new BlockPos(this.dest.getX() - this.boundingBox.minX(), this.dest.getY() - this.boundingBox.minY(), this.dest.getZ() - this.boundingBox.minZ());

		this.drawBresehnam(level, writeableBounds, rSrc.getX(), rSrc.getY(), rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), TFBlocks.TWILIGHT_OAK_WOOD.value().defaultBlockState());

		// with leaves!
		if (this.leafy) {
			int leafRad = random.nextInt(2) + 1;
			this.makeLeafBlob(level, writeableBounds, rDest.getX(), rDest.getY(), rDest.getZ(), leafRad);
		}
	}
}
