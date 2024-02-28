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

public class HollowTreeLargeBranch extends HollowTreeMedBranch {
	private static final int LEAF_DUNGEON_CHANCE = 8;
	public final boolean hasLeafDungeon;

	protected HollowTreeLargeBranch(int i, BlockPos src, double length, double angle, double tilt, boolean leafy, RandomSource rand) {
		super(TFStructurePieceTypes.TFHTLB.value(), i, src, FeatureLogic.translate(src, length, angle, tilt), length, angle, tilt, leafy);

		this.hasLeafDungeon = rand.nextInt(LEAF_DUNGEON_CHANCE) == 0;
	}

	public HollowTreeLargeBranch(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFHTLB.value(), tag);

		this.hasLeafDungeon = tag.getBoolean("has_leaf_dungeon");
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		super.addAdditionalSaveData(context, tag);

		tag.putBoolean("has_leaf_dungeon", this.hasLeafDungeon);
	}

	/**
	 * Add other structure components to this one if needed
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, RandomSource rand) {
		int index = this.getGenDepth();

		// go about halfway out and make a few medium branches.
		// the number of medium branches we can support depends on the length of the big branch
		// every other branch switches sides
		int numMedBranches = rand.nextInt((int)(this.length / 6)) + (int)(this.length / 8);

		for(int i = 0; i <= numMedBranches; i++) {
			double outVar = (rand.nextDouble() * 0.3) + 0.3;
			double angleVar = rand.nextDouble() * 0.225 * ((i & 1) == 0 ? 1.0 : -1.0);

			BlockPos bsrc = FeatureLogic.translate(this.src, this.length * outVar, this.angle, this.tilt);

			this.makeMedBranch(list, rand, index + 2 + i, bsrc, this.length * 0.6, this.angle + angleVar, this.tilt, this.leafy);
		}

//		// make 1-2 small ones near the base
//		int numSmallBranches = rand.nextInt(2) + 1;
//		for(int i = 0; i <= numSmallBranches; i++) {
//
//			double outVar = (rand.nextDouble() * 0.25) + 0.25;
//			double angleVar = rand.nextDouble() * 0.25 * ((i & 1) == 0 ? 1.0 : -1.0);
//			ChunkCoordinates bsrc = TFGenerator.translateCoords(src.getX(, src.getY(, src.getZ(, length * outVar, angle, tilt);
//
//			makeSmallBranch(list, rand, index + numMedBranches + 1 + i, bsrc.getX(, bsrc.getY(, bsrc.getZ(, Math.max(length * 0.3, 2), angle + angleVar, tilt, leafy);
//		}

		if (this.hasLeafDungeon) {
			this.makeLeafDungeon(list, rand, index + 1, this.dest.getX(), this.dest.getY(), this.dest.getZ());
		}
	}

	public void makeLeafDungeon(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z) {
		HollowTreeLeafDungeon dungeon = new HollowTreeLeafDungeon(index, x, y, z, 4);
		list.addPiece(dungeon);
		dungeon.addChildren(this, list, rand);
	}

	public void makeMedBranch(StructurePieceAccessor list, RandomSource rand, int index, BlockPos src, double branchLength, double branchRotation, double branchAngle, boolean leafy) {
		HollowTreeMedBranch branch = new HollowTreeMedBranch(index, src, branchLength, branchRotation, branchAngle, leafy);
		list.addPiece(branch);
		branch.addChildren(this, list, rand);
	}

//	public void makeSmallBranch(List list, Random rand, int index, int x, int y, int z, double branchLength, double branchRotation, double branchAngle, boolean leafy) {
//        ComponentTFHollowTreeSmallBranch branch = new ComponentTFHollowTreeSmallBranch(index, x, y, z, branchLength, branchRotation, branchAngle, leafy);
//        list.add(branch);
//        branch.buildComponent(this, list, rand);
//	}

	/**
	 * Draw this branch
	 */
	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		BlockPos rsrc = new BlockPos(this.src.getX() - this.boundingBox.minX(), this.src.getY() - this.boundingBox.minY(), this.src.getZ() - this.boundingBox.minZ());
		BlockPos rdest = new BlockPos(this.dest.getX() - this.boundingBox.minX(), this.dest.getY() - this.boundingBox.minY(), this.dest.getZ() - this.boundingBox.minZ());

		// main branch
		this.drawBresehnam(level, writeableBounds, rsrc.getX(), rsrc.getY(), rsrc.getZ(), rdest.getX(), rdest.getY(), rdest.getZ(), TFBlocks.TWILIGHT_OAK_WOOD.value().defaultBlockState());

		// reinforce it
		int reinforcements = 4;
		for(int i = 0; i <= reinforcements; i++) {
			int vx = (i & 2) == 0 ? 1 : 0;
			int vy = (i & 1) == 0 ? 1 : -1;
			int vz = (i & 2) == 0 ? 0 : 1;
			this.drawBresehnam(level, writeableBounds, rsrc.getX() + vx, rsrc.getY() + vy, rsrc.getZ() + vz, rdest.getX(), rdest.getY(), rdest.getZ(), TFBlocks.TWILIGHT_OAK_WOOD.value().defaultBlockState());
		}

		// make 1-2 small branches near the base
		//Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX() * 321534781) ^ (this.boundingBox.minZ() * 756839));
		int numSmallBranches = random.nextInt(2) + 1;
		for(int i = 0; i <= numSmallBranches; i++) {

			double outVar = (random.nextFloat() * 0.25F) + 0.25F;
			double angleVar = random.nextFloat() * 0.25F * ((i & 1) == 0 ? 1.0F : -1.0F);
			int sx = rsrc.getX();
			int sy = rsrc.getY();
			int sz = rsrc.getZ();
			BlockPos bsrc = FeatureLogic.translate(new BlockPos(sx, sy, sz), this.length * outVar, this.angle, this.tilt);

			this.drawSmallBranch(level, writeableBounds, bsrc.getX(), bsrc.getY(), bsrc.getZ(), Math.max(this.length * 0.3F, 2F), this.angle + angleVar, this.tilt, this.leafy);
		}

		if (this.leafy && !this.hasLeafDungeon) {
			// leaf blob at the end
			this.makeLeafBlob(level, writeableBounds, rdest.getX(), rdest.getY(), rdest.getZ(), 3);
		}
	}
}
