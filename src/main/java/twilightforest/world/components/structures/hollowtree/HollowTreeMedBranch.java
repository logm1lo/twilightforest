package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.FeatureLogic;
import twilightforest.util.VoxelBresenhamIterator;

public class HollowTreeMedBranch extends StructurePiece {
	protected final BlockPos src, dest;  // source and destination of branch, array of 3 ints representing x, y, z
	protected final double length;
	protected final double angle;
	protected final double tilt;
	protected final boolean leafy;

	protected HollowTreeMedBranch(int i, BlockPos src, double length, double angle, double tilt, boolean leafy) {
		this(i, src, FeatureLogic.translate(src, length, angle, tilt), length, angle, tilt, leafy);
	}

	protected HollowTreeMedBranch(int i, BlockPos src, BlockPos dest, double length, double angle, double tilt, boolean leafy) {
		this(i, src, dest, branchBoundingBox(src, dest), length, angle, tilt, leafy);
	}

	protected HollowTreeMedBranch(StructurePieceType type, int i, BlockPos src, BlockPos dest, double length, double angle, double tilt, boolean leafy) {
		this(type, i, src, dest, branchBoundingBox(src, dest), length, angle, tilt, leafy);
	}

	protected HollowTreeMedBranch(int i, BlockPos src, BlockPos dest, BoundingBox boundingBox, double length, double angle, double tilt, boolean leafy) {
		this(TFStructurePieceTypes.TFHTMB.value(), i, src, dest, boundingBox, length, angle, tilt, leafy);
	}

	protected HollowTreeMedBranch(StructurePieceType type, int i, BlockPos src, BlockPos dest, BoundingBox boundingBox, double length, double angle, double tilt, boolean leafy) {
		super(type, i, boundingBox);

		this.setOrientation(Direction.SOUTH);

		this.src = src;
		this.dest = dest;

		this.boundingBox = boundingBox;

		this.length = length;
		this.angle = angle;
		this.tilt = tilt;
		this.leafy = leafy;
	}

	public HollowTreeMedBranch(StructurePieceSerializationContext context, CompoundTag tag) {
		this(TFStructurePieceTypes.TFHTMB.value(), tag);
	}

	protected HollowTreeMedBranch(StructurePieceType type, CompoundTag tag) {
		super(type, tag);

		this.src = new BlockPos(tag.getInt("srcPosX"), tag.getInt("srcPosY"), tag.getInt("srcPosZ"));
		this.dest = new BlockPos(tag.getInt("destPosX"), tag.getInt("destPosY"), tag.getInt("destPosZ"));

		this.length = tag.getDouble("branchLength");
		this.angle = tag.getDouble("branchAngle");
		this.tilt = tag.getDouble("branchTilt");
		this.leafy = tag.getBoolean("branchLeafy");
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putInt("srcPosX", this.src.getX());
		tag.putInt("srcPosY", this.src.getY());
		tag.putInt("srcPosZ", this.src.getZ());
		
		tag.putInt("destPosX", this.dest.getX());
		tag.putInt("destPosY", this.dest.getY());
		tag.putInt("destPosZ", this.dest.getZ());

		tag.putDouble("branchLength", this.length);
		tag.putDouble("branchAngle", this.angle);
		tag.putDouble("branchTilt", this.tilt);
		tag.putBoolean("branchLeafy", this.leafy);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource decoRNG, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		BlockPos rSrc = new BlockPos(this.src.getX() - this.boundingBox.minX(), this.src.getY() - this.boundingBox.minY(), this.src.getZ() - this.boundingBox.minZ());
		BlockPos rDest = new BlockPos(this.dest.getX() - this.boundingBox.minX(), this.dest.getY() - this.boundingBox.minY(), this.dest.getZ() - this.boundingBox.minZ());

//		placeVoid(world, sbb, boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ, TFBlocks.wood, 0, false);
//		System.out.println("Drawing a medium branch from " + rsrc.posX + ", " + rsrc.posY + ", " + rsrc.posZ + " to " + rdest.posX + ", " + rdest.posY + ", " + rdest.posZ);
//		System.out.println("My bounding box is  " + boundingBox.minX + ", " + boundingBox.minY + ", " + boundingBox.minZ + " to "  + boundingBox.maxX + ", " + boundingBox.maxY + ", " + boundingBox.maxZ);
//		System.out.println("Draw bounding box is  " + sbb.minX + ", " + sbb.minY + ", " + sbb.minZ + " to "  + sbb.maxX + ", " + sbb.maxY + ", " + sbb.maxZ);

		this.drawBresehnam(level, writeableBounds, rSrc.getX(), rSrc.getY(), rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), TFBlocks.TWILIGHT_OAK_WOOD.value().defaultBlockState());
		this.drawBresehnam(level, writeableBounds, rSrc.getX(), rSrc.getY() + 1, rSrc.getZ(), rDest.getX(), rDest.getY(), rDest.getZ(), TFBlocks.TWILIGHT_OAK_WOOD.value().defaultBlockState());

		//Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		// and several small branches
		int numShoots = Math.min(decoRNG.nextInt(3) + 1, (int)(this.length / 5));
		double angleInc, angleVar, outVar;

		angleInc = 0.8 / numShoots;

		for(int i = 0; i < numShoots; i++) {
			angleVar = (angleInc * i) - 0.4;
			outVar = (decoRNG.nextDouble() * 0.8) + 0.2;

			BlockPos bSrc = FeatureLogic.translate(rSrc, this.length * outVar, this.angle, this.tilt);

			this.drawSmallBranch(level, writeableBounds, bSrc.getX(), bSrc.getY(), bSrc.getZ(), Math.max(this.length * 0.3F, 2F), this.angle + angleVar, this.tilt, this.leafy);
		}

		// with leaves!
		if (this.leafy) {
			int numLeafBalls = Math.min(decoRNG.nextInt(3) + 1, (int)(this.length / 5));
			for(int i = 0; i < numLeafBalls; i++) {

				double slength = (decoRNG.nextFloat() * 0.6F + 0.2F) * this.length;
				BlockPos bdst = FeatureLogic.translate(rSrc, slength, this.angle, this.tilt);

				this.makeLeafBlob(level, writeableBounds, bdst.getX(), bdst.getY(), bdst.getZ(), decoRNG.nextBoolean() ? 2 : 3);
			}

			this.makeLeafBlob(level, writeableBounds, rDest.getX(), rDest.getY(), rDest.getZ(), 3);
		}
	}

	/**
	 * Draws a line
	 */
	protected void drawBresehnam(WorldGenLevel world, BoundingBox sbb, int x1, int y1, int z1, int x2, int y2, int z2, BlockState blockValue) {
		for (BlockPos coords : new VoxelBresenhamIterator(this.getWorldPos(x1, y1, z1).immutable(), this.getWorldPos(x2, y2, z2).immutable()))
			if (sbb.isInside(coords))
				world.setBlock(coords, blockValue, 2);
	}

	/**
	 * Make a leaf blob
	 */
	protected void makeLeafBlob(WorldGenLevel world, BoundingBox sbb, int sx, int sy, int sz, int radius) {
		BlockState blockState = TFBlocks.TWILIGHT_OAK_LEAVES.value().defaultBlockState();

		// then trace out a quadrant
		for (int dx = 0; dx <= radius; dx++) {
			for (int dy = 0; dy <= radius; dy++) {
				for (int dz = 0; dz <= radius; dz++) {
					// determine how far we are from the center.
					int dist;

					if (dx >= dy && dx >= dz) {
						dist = (int) (dx + ((Math.max(dy, dz) * 0.5F) + (Math.min(dy, dz) * 0.25F)));
					} else if (dy >= dx && dy >= dz) {
						dist = (int) (dy + ((Math.max(dx, dz) * 0.5F) + (Math.min(dx, dz) * 0.25F)));
					} else {
						dist = (int) (dz + ((Math.max(dx, dy) * 0.5F) + (Math.min(dx, dy) * 0.25F)));
					}

					// if we're inside the blob, fill it
					if (dist <= radius) {
						// do eight at a time for easiness!
						this.placeLeafBlock(world, blockState, sx + dx, sy + dy, sz + dz, sbb);
						this.placeLeafBlock(world, blockState, sx + dx, sy + dy, sz - dz, sbb);
						this.placeLeafBlock(world, blockState, sx - dx, sy + dy, sz + dz, sbb);
						this.placeLeafBlock(world, blockState, sx - dx, sy + dy, sz - dz, sbb);
						this.placeLeafBlock(world, blockState, sx + dx, sy - dy, sz + dz, sbb);
						this.placeLeafBlock(world, blockState, sx + dx, sy - dy, sz - dz, sbb);
						this.placeLeafBlock(world, blockState, sx - dx, sy - dy, sz + dz, sbb);
						this.placeLeafBlock(world, blockState, sx - dx, sy - dy, sz - dz, sbb);
					}
				}
			}
		}
	}

	/**
	 * Puts a block only if leaves can go there.
	 */
	protected void placeLeafBlock(WorldGenLevel world, BlockState blockID, int x, int y, int z, BoundingBox sbb) {
		BlockPos pos = this.getWorldPos(x, y, z);

		if (sbb.isInside(pos))
			if (FeatureLogic.worldGenReplaceable(world.getBlockState(pos)))
				world.setBlock(pos, blockID, 2);
	}

	/**
	 * This is like the small branch component, but we're just drawing it directly into the world
	 */
	protected void drawSmallBranch(WorldGenLevel world, BoundingBox sbb, int sx, int sy, int sz, double branchLength, double branchAngle, double branchTilt, boolean leafy) {
		// draw a line
		BlockPos branchDest = FeatureLogic.translate(new BlockPos(sx, sy, sz), branchLength, branchAngle, branchTilt);

		this.drawBresehnam(world, sbb, sx, sy, sz, branchDest.getX(), branchDest.getY(), branchDest.getZ(), TFBlocks.TWILIGHT_OAK_WOOD.value().defaultBlockState());

		// leaf blob at the end
		this.makeLeafBlob(world, sbb,  branchDest.getX(), branchDest.getY(), branchDest.getZ(), 2);
	}

	protected static BoundingBox branchBoundingBox(BlockPos src, BlockPos dest) {
		return new BoundingBox(Math.min(src.getX(), dest.getX()), Math.min(src.getY(), dest.getY()), Math.min(src.getZ(), dest.getZ()), Math.max(src.getX(), dest.getX()), Math.max(src.getY(), dest.getY()), Math.max(src.getZ(), dest.getZ())).inflatedBy(3);
	}
}
