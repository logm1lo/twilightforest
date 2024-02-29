package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.FeatureLogic;
import twilightforest.util.VoxelBresenhamIterator;
import twilightforest.world.components.structures.type.HollowTreeStructure;

public class HollowTreeMedBranch extends StructurePiece {
	protected final BlockPos src, dest;  // source and destination of branch, array of 3 ints representing x, y, z
	protected final double length;
	protected final double angle;
	protected final double tilt;
	protected final boolean leafy;

	protected final BlockStateProvider wood;
	protected final BlockStateProvider leaves;

	protected HollowTreeMedBranch(int i, BlockPos src, double length, double angle, double tilt, boolean leafy, BlockStateProvider wood, BlockStateProvider leaves) {
		this(i, src, FeatureLogic.translate(src, length, angle, tilt), length, angle, tilt, leafy, wood, leaves);
	}

	protected HollowTreeMedBranch(int i, BlockPos src, BlockPos dest, double length, double angle, double tilt, boolean leafy, BlockStateProvider wood, BlockStateProvider leaves) {
		this(i, src, dest, branchBoundingBox(src, dest), length, angle, tilt, leafy, wood, leaves);
	}

	protected HollowTreeMedBranch(StructurePieceType type, int i, BlockPos src, BlockPos dest, double length, double angle, double tilt, boolean leafy, BlockStateProvider wood, BlockStateProvider leaves) {
		this(type, i, src, dest, branchBoundingBox(src, dest), length, angle, tilt, leafy, wood, leaves);
	}

	protected HollowTreeMedBranch(int i, BlockPos src, BlockPos dest, BoundingBox boundingBox, double length, double angle, double tilt, boolean leafy, BlockStateProvider wood, BlockStateProvider leaves) {
		this(TFStructurePieceTypes.TFHTMB.value(), i, src, dest, boundingBox, length, angle, tilt, leafy, wood, leaves);
	}

	protected HollowTreeMedBranch(StructurePieceType type, int i, BlockPos src, BlockPos dest, BoundingBox boundingBox, double length, double angle, double tilt, boolean leafy, BlockStateProvider wood, BlockStateProvider leaves) {
		super(type, i, boundingBox);

		this.setOrientation(Direction.SOUTH);

		this.src = src;
		this.dest = dest;

		this.boundingBox = boundingBox;

		this.length = length;
		this.angle = angle;
		this.tilt = tilt;
		this.leafy = leafy;

		this.wood = wood;
		this.leaves = leaves;
	}

	public HollowTreeMedBranch(StructurePieceSerializationContext context, CompoundTag tag) {
		this(TFStructurePieceTypes.TFHTMB.value(), context, tag);
	}

	protected HollowTreeMedBranch(StructurePieceType type, StructurePieceSerializationContext context, CompoundTag tag) {
		super(type, tag);

		this.src = new BlockPos(tag.getInt("srcPosX"), tag.getInt("srcPosY"), tag.getInt("srcPosZ"));
		this.dest = new BlockPos(tag.getInt("destPosX"), tag.getInt("destPosY"), tag.getInt("destPosZ"));

		this.length = tag.getDouble("branchLength");
		this.angle = tag.getDouble("branchAngle");
		this.tilt = tag.getDouble("branchTilt");
		this.leafy = tag.getBoolean("branchLeafy");

		RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, context.registryAccess());
		this.wood = BlockStateProvider.CODEC.parse(ops, tag.getCompound("wood")).result().orElse(HollowTreeStructure.DEFAULT_WOOD);
		this.leaves = BlockStateProvider.CODEC.parse(ops, tag.getCompound("leaves")).result().orElse(HollowTreeStructure.DEFAULT_LEAVES);
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

		tag.put("wood", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.wood).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("leaves", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.leaves).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource decoRNG, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		this.drawBresehnam(level, writeableBounds, this.src, this.dest, this.wood, decoRNG);
		this.drawBresehnam(level, writeableBounds, this.src.above(), this.dest, this.wood, decoRNG);

		// and several small branches
		int numShoots = Math.min(decoRNG.nextInt(3) + 1, (int)(this.length / 5));
		double angleInc, angleVar, outVar;

		angleInc = 0.8 / numShoots;

		for(int i = 0; i < numShoots; i++) {
			angleVar = (angleInc * i) - 0.4;
			outVar = (decoRNG.nextDouble() * 0.8) + 0.2;

			BlockPos bSrc = FeatureLogic.translate(this.src, this.length * outVar, this.angle, this.tilt);

			this.drawSmallBranch(level, writeableBounds, bSrc, Math.max(this.length * 0.3F, 2F), this.angle + angleVar, this.tilt, decoRNG, this.wood, this.leaves);
		}

		// with leaves!
		if (this.leafy) {
			int numLeafBalls = Math.min(decoRNG.nextInt(3) + 1, (int)(this.length / 5));

			for(int i = 0; i < numLeafBalls; i++) {

				double slength = (decoRNG.nextFloat() * 0.6F + 0.2F) * this.length;
				BlockPos bdst = FeatureLogic.translate(new BlockPos(this.src.getX() - this.boundingBox.minX(), this.src.getY() - this.boundingBox.minY(), this.src.getZ() - this.boundingBox.minZ()), slength, this.angle, this.tilt);

				this.makeLeafBlob(level, writeableBounds, bdst.getX(), bdst.getY(), bdst.getZ(), decoRNG.nextBoolean() ? 2 : 3, decoRNG, this.leaves);
			}

			this.makeLeafBlob(level, writeableBounds, this.dest.getX() - this.boundingBox.minX(), this.dest.getY() - this.boundingBox.minY(), this.dest.getZ() - this.boundingBox.minZ(), 3, decoRNG, this.leaves);
		}
	}

	/**
	 * Draws a line
	 */
	protected void drawBresehnam(WorldGenLevel level, BoundingBox writeableBounds, BlockPos startPos, BlockPos endPos, BlockStateProvider stateProvider, RandomSource random) {
		for (BlockPos coords : new VoxelBresenhamIterator(startPos, endPos))
			if (writeableBounds.isInside(coords))
				level.setBlock(coords, stateProvider.getState(random, coords), 2);
	}

	/**
	 * Make a leaf blob
	 */
	protected void makeLeafBlob(WorldGenLevel world, BoundingBox sbb, int sx, int sy, int sz, int radius, RandomSource random, BlockStateProvider stateProvider) {
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
						this.placeLeafBlock(world, stateProvider, sx + dx, sy + dy, sz + dz, sbb, random);
						this.placeLeafBlock(world, stateProvider, sx + dx, sy + dy, sz - dz, sbb, random);
						this.placeLeafBlock(world, stateProvider, sx - dx, sy + dy, sz + dz, sbb, random);
						this.placeLeafBlock(world, stateProvider, sx - dx, sy + dy, sz - dz, sbb, random);
						this.placeLeafBlock(world, stateProvider, sx + dx, sy - dy, sz + dz, sbb, random);
						this.placeLeafBlock(world, stateProvider, sx + dx, sy - dy, sz - dz, sbb, random);
						this.placeLeafBlock(world, stateProvider, sx - dx, sy - dy, sz + dz, sbb, random);
						this.placeLeafBlock(world, stateProvider, sx - dx, sy - dy, sz - dz, sbb, random);
					}
				}
			}
		}
	}

	/**
	 * Puts a block only if leaves can go there.
	 */
	protected void placeLeafBlock(WorldGenLevel world, BlockStateProvider blockID, int x, int y, int z, BoundingBox sbb, RandomSource random) {
		BlockPos pos = this.getWorldPos(x, y, z);

		if (sbb.isInside(pos))
			if (FeatureLogic.worldGenReplaceable(world.getBlockState(pos)))
				world.setBlock(pos, blockID.getState(random, pos), 2);
	}

	/**
	 * This is like the small branch component, but we're just drawing it directly into the world
	 */
	protected void drawSmallBranch(WorldGenLevel world, BoundingBox sbb, BlockPos sourcePos, double branchLength, double branchAngle, double branchTilt, RandomSource random, BlockStateProvider woodProvider, BlockStateProvider leafProvider) {
		// draw a line
		BlockPos branchDest = FeatureLogic.translate(sourcePos, branchLength, branchAngle, branchTilt);

		this.drawBresehnam(world, sbb, sourcePos, branchDest, woodProvider, random);

		// leaf blob at the end
		this.makeLeafBlob(world, sbb, branchDest.getX() - this.boundingBox.minX(), branchDest.getY() - this.boundingBox.minY(), branchDest.getZ() - this.boundingBox.minZ(), 2, random, leafProvider);
	}

	protected static BoundingBox branchBoundingBox(BlockPos src, BlockPos dest) {
		return new BoundingBox(Math.min(src.getX(), dest.getX()), Math.min(src.getY(), dest.getY()), Math.min(src.getZ(), dest.getZ()), Math.max(src.getX(), dest.getX()), Math.max(src.getY(), dest.getY()), Math.max(src.getZ(), dest.getZ())).inflatedBy(3);
	}
}
