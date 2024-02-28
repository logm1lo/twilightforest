package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.FeatureLogic;

public class HollowTreeTrunk extends StructurePiece {
	private final int height;
	private final int radius;

	public static HollowTreeTrunk atCoords(RandomSource rand, int x, int y, int z) {
		int height = rand.nextInt(64) + 32;
		int radius =  rand.nextInt(4) + 1;

		BoundingBox boundingBox = new BoundingBox(x, y, z, (x + radius * 2) + 2, y + height, (z + radius * 2) + 2);

		return new HollowTreeTrunk(height, radius, boundingBox);
	}

	protected HollowTreeTrunk(int height, int radius, BoundingBox pBoundingBox) {
		super(TFStructurePieceTypes.TFHTTr.value(), 0, pBoundingBox);

		this.setOrientation(Direction.SOUTH);

		this.height = height;
		this.radius = radius;
	}

	/**
	 * Load from NBT
	 */
	public HollowTreeTrunk(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFHTTr.value(), tag);

		this.height = tag.getInt("trunkHeight");
		this.radius = tag.getInt("trunkRadius");
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putInt("trunkHeight", this.height);
		tag.putInt("trunkRadius", this.radius);
	}

	/**
	 * Add on the various bits and doo-dads we need to succeed
	 */
	@Override
	public void addChildren(StructurePiece piece, StructurePieceAccessor list, RandomSource rand) {
		int index = getGenDepth();

		// 3-5 couple branches on the way up...
		int numBranches = rand.nextInt(3) + 3;
		for (int i = 0; i <= numBranches; i++) {
			int branchHeight = (int)(this.height * rand.nextDouble() * 0.9) + (this.height / 10);
			double branchRotation = rand.nextDouble();

			makeSmallBranch(list, rand, index + i + 1, branchHeight, 4, branchRotation, 0.35D, true);
		}

		// build the crown
		buildFullCrown(list, rand, index + numBranches + 1);

		// roots
		// 3-5 roots at the bottom
		buildBranchRing(list, rand, index, 3, 2, 6, 0, 0.75D, 0, 3, 5, 3, false);


		// several more taproots
		buildBranchRing(list, rand, index, 1, 2, 8, 0, 0.9D, 0, 3, 5, 3, false);
	}

	/**
	 * Build the crown of the tree
	 */
	protected void buildFullCrown(StructurePieceAccessor list, RandomSource rand, int index) {
		int crownRadius = this.radius * 4 + 4;
		int bvar = this.radius + 2;

		// okay, let's do 3-5 main branches starting at the bottom of the crown
		index += buildBranchRing(list, rand, index, this.height - crownRadius, 0, crownRadius, 0, 0.35D, 0, bvar, bvar + 2, 2, true);

		// then, let's do 3-5 medium branches at the crown middle
		index += buildBranchRing(list, rand, index, this.height - (crownRadius / 2), 0, crownRadius, 0, 0.28D, 0, bvar, bvar + 2, 1, true);

		// finally, let's do 2-4 main branches at the crown top
		index += buildBranchRing(list, rand, index, this.height, 0, crownRadius, 0, 0.15D, 0, 2, 4, 2, true);

		// and extra finally, let's do 3-6 medium branches going straight up
		index += buildBranchRing(list, rand, index, this.height, 0, (crownRadius / 2), 0, 0.05D, 0, bvar, bvar + 2, 1, true);
	}

	/**
	 * Build a ring of branches around the tree
	 * size 0 = small, 1 = med, 2 = large, 3 = root
	 */
	protected int buildBranchRing(StructurePieceAccessor list, RandomSource rand, int index, int branchHeight, int heightVar, int length, int lengthVar, double tilt, double tiltVar, int minBranches, int maxBranches, int size, boolean leafy) {
		//let's do this!
		int numBranches = rand.nextInt(maxBranches - minBranches + 1) + minBranches;
		double branchRotation = 1.0 / numBranches;
		double branchOffset = rand.nextDouble();

		for (int i = 0; i <= numBranches; i++) {
			int dHeight;
			if (heightVar > 0) {
				dHeight = branchHeight - heightVar + rand.nextInt(2 * heightVar);
			} else {
				dHeight = branchHeight;
			}

			if (size == 2) {
				makeLargeBranch(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			} else if (size == 1) {
				makeMedBranch(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			} else if (size == 3) {
				makeRoot(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt);
			} else {
				makeSmallBranch(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			}
		}

		return numBranches;
	}


	public void makeSmallBranch(StructurePieceAccessor list, RandomSource rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle, boolean leafy) {
		BlockPos bSrc = getBranchSrc(branchHeight, branchRotation);
		HollowTreeSmallBranch branch = new HollowTreeSmallBranch(index, bSrc, branchLength, branchRotation, branchAngle, leafy);
		list.addPiece(branch);
		branch.addChildren(this, list, rand);
	}

	public void makeMedBranch(StructurePieceAccessor list, RandomSource rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle, boolean leafy) {
		BlockPos bSrc = getBranchSrc(branchHeight, branchRotation);
		HollowTreeMedBranch branch = new HollowTreeMedBranch(index, bSrc, branchLength, branchRotation, branchAngle, leafy);
		list.addPiece(branch);
		branch.addChildren(this, list, rand);
	}

	public void makeLargeBranch(StructurePieceAccessor list, RandomSource rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle, boolean leafy) {
		BlockPos bSrc = getBranchSrc(branchHeight, branchRotation);
		HollowTreeLargeBranch branch = new HollowTreeLargeBranch(index, bSrc, branchLength, branchRotation, branchAngle, leafy, rand);
		list.addPiece(branch);
		branch.addChildren(this, list, rand);
	}


	public void makeRoot(StructurePieceAccessor list, RandomSource rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle) {
		BlockPos bSrc = getBranchSrc(branchHeight, branchRotation);
		HollowTreeRoot branch = new HollowTreeRoot(index, bSrc, branchLength, branchRotation, branchAngle, false);
		list.addPiece(branch);
		branch.addChildren(this, list, rand);
	}

	/**
	 * Where should we start this branch?
	 */
	private BlockPos getBranchSrc(int branchHeight, double branchRotation) {
		int sx = this.boundingBox.minX() + this.radius + 1;
		int sy = this.boundingBox.minY() + branchHeight;
		int sz = this.boundingBox.minZ() + this.radius + 1;
		return FeatureLogic.translate(new BlockPos(sx, sy, sz), this.radius, branchRotation, 0.5);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		int hollow = this.radius / 2;

		for (int dx = 0; dx <= 2 * this.radius; dx++) {
			for (int dz = 0; dz <= 2 * this.radius; dz++) {
				// determine how far we are from the center.
				int ax = Math.abs(dx - this.radius);
				int az = Math.abs(dz - this.radius);
				int dist = (int)(Math.max(ax, az) + (Math.min(ax, az) * 0.5));

				for (int dy = 0; dy <= this.height; dy++) {
					// fill the body of the trunk
					if (dist <= this.radius && dist > hollow) {
						this.placeBlock(level, TFBlocks.TWILIGHT_OAK_LOG.value().defaultBlockState(), dx + 1, dy, dz + 1, writeableBounds); // offset, since our BB is slightly larger than the trunk
					}
				}

				// fill to ground
				if (dist <= this.radius) {
					this.fillColumnDown(level, TFBlocks.TWILIGHT_OAK_LOG.value().defaultBlockState(), dx + 1, -1, dz + 1, writeableBounds);
				}

				// add vines
				if (dist == hollow && dx == hollow + this.radius) {
					this.fillColumnDown(level, Blocks.VINE.defaultBlockState(), dx + 1, this.height, dz + 1, writeableBounds);
				}
			}
		}

		// fireflies & cicadas
		int numInsects = random.nextInt(3 * this.radius) + random.nextInt(3 * this.radius) + 10;
		for (int i = 0; i <= numInsects; i++) {
			int fHeight = (int)(this.height * random.nextDouble() * 0.9) + (this.height / 10);
			double fAngle = random.nextDouble();
			addInsect(level, random.nextBoolean() ? TFBlocks.FIREFLY.value() : TFBlocks.CICADA.value(), fHeight, fAngle, writeableBounds);
		}
	}

	/**
	 * Add a random insect
	 */
	protected void addInsect(WorldGenLevel world, Block bug, int fHeight, double fAngle, BoundingBox sbb) {
		BlockPos bugSpot = FeatureLogic.translate(new BlockPos(this.radius + 1, fHeight, this.radius + 1), this.radius + 1, fAngle, 0.5);

		int ox = this.getWorldX(bugSpot.getX(), bugSpot.getZ());
		int oy = this.getWorldY(bugSpot.getY());
		int oz = this.getWorldZ(bugSpot.getX(), bugSpot.getZ());

		if (!sbb.isInside(ox, oy, oz)) return;

		BlockPos src = new BlockPos(ox, oy, oz);

		double fAngleWrapped = fAngle % 1.0;
		Direction facing = Direction.EAST;

		if (fAngleWrapped > 0.875 || fAngleWrapped <= 0.125) {
			facing = Direction.SOUTH;
		} else if (fAngleWrapped > 0.375 && fAngleWrapped <= 0.625) {
			facing = Direction.NORTH;
		} else if (fAngleWrapped > 0.625) {
			facing = Direction.WEST;
		}

		if (bug.defaultBlockState().setValue(DirectionalBlock.FACING, facing).canSurvive(world, src)) {
			world.setBlock(src, bug.defaultBlockState().setValue(DirectionalBlock.FACING, facing), 3);
		}
	}
}
