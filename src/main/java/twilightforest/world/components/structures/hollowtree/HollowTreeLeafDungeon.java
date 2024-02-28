package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.FeatureLogic;

public class HollowTreeLeafDungeon extends StructurePiece {
	private final int radius; // radius

	/**
	 * Make a blob of leaves
	 *
	 * @param index
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 */
	protected HollowTreeLeafDungeon(int index, int x, int y, int z, int radius) {
		super(TFStructurePieceTypes.TFHTLD.value(), index, new BoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));

		this.setOrientation(Direction.SOUTH);

		this.radius = radius;
	}

	/**
	 * Load from NBT
	 */
	public HollowTreeLeafDungeon(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFHTLD.value(), tag);

		this.radius = tag.getInt("leafRadius");
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putInt("leafRadius", this.radius);
	}

	/**
	 * Draw a giant blob of whatevs (okay, it's going to be leaves).
	 */
	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		// leaves on the outside
		this.drawBlockBlob(level, writeableBounds, this.radius, this.radius, this.radius, 4, TFBlocks.TWILIGHT_OAK_LEAVES.value().defaultBlockState(), true);
		// then wood
		this.drawBlockBlob(level, writeableBounds, this.radius, this.radius, this.radius, 3, TFBlocks.TWILIGHT_OAK_WOOD.value().defaultBlockState(), false);
		// then air
		this.drawBlockBlob(level, writeableBounds, this.radius, this.radius, this.radius, 2, Blocks.AIR.defaultBlockState(), false);

		// then treasure chest
		// which direction is this chest in?
		this.placeTreasureAtCurrentPosition(level, this.radius + 2, this.radius - 1, this.radius, TFLootTables.TREE_CACHE, writeableBounds);

		// then spawner
		this.placeSpawnerAtCurrentPosition(level, random, this.radius, this.radius, this.radius, TFEntities.SWARM_SPIDER.value(), writeableBounds);
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 *
	 * @param treasureType
	 */
	protected void placeTreasureAtCurrentPosition(WorldGenLevel world, int x, int y, int z, TFLootTables treasureType, BoundingBox sbb) {
		BlockPos pos = this.getWorldPos(x, y, z);

		if (sbb.isInside(pos) && !world.getBlockState(pos).is(Blocks.CHEST)) {
			treasureType.generateChest(world, pos, Direction.NORTH, false);
		}
	}

	/**
	 * Place a monster spawner at the specified coordinates
	 *
	 * @param monsterID
	 */
	protected void placeSpawnerAtCurrentPosition(WorldGenLevel world, RandomSource rand, int x, int y, int z, EntityType<? extends Entity> monsterID, BoundingBox sbb) {
		BlockPos pos = this.getWorldPos(x, y, z);

		if (sbb.isInside(pos) && !world.getBlockState(pos).is(Blocks.SPAWNER)) {
			world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

			if (world.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner)
				spawner.setEntityId(monsterID, rand);
		}
	}

	private void drawBlockBlob(WorldGenLevel world, BoundingBox sbb, int sx, int sy, int sz, int blobRadius, BlockState blockID, boolean isLeaves) {
		// then trace out a quadrant
		for (byte dx = 0; dx <= blobRadius; dx++) {
			for (byte dy = 0; dy <= blobRadius; dy++) {
				for (byte dz = 0; dz <= blobRadius; dz++) {
					// determine how far we are from the center.
					byte dist;

					if (dx >= dy && dx >= dz) {
						dist = (byte) (dx + (byte)((Math.max(dy, dz) * 0.5) + (Math.min(dy, dz) * 0.25)));
					} else if (dy >= dx && dy >= dz) {
						dist = (byte) (dy + (byte)((Math.max(dx, dz) * 0.5) + (Math.min(dx, dz) * 0.25)));
					} else {
						dist = (byte) (dz + (byte)((Math.max(dx, dy) * 0.5) + (Math.min(dx, dy) * 0.25)));
					}


					// if we're inside the blob, fill it
					if (dist <= blobRadius) {
						// do eight at a time for easiness!
						if (isLeaves) {
							this.placeLeafBlock(world, blockID, sx + dx, sy + dy, sz + dz, sbb);
							this.placeLeafBlock(world, blockID, sx + dx, sy + dy, sz - dz, sbb);
							this.placeLeafBlock(world, blockID, sx - dx, sy + dy, sz + dz, sbb);
							this.placeLeafBlock(world, blockID, sx - dx, sy + dy, sz - dz, sbb);
							this.placeLeafBlock(world, blockID, sx + dx, sy - dy, sz + dz, sbb);
							this.placeLeafBlock(world, blockID, sx + dx, sy - dy, sz - dz, sbb);
							this.placeLeafBlock(world, blockID, sx - dx, sy - dy, sz + dz, sbb);
							this.placeLeafBlock(world, blockID, sx - dx, sy - dy, sz - dz, sbb);
						} else {
							this.placeBlock(world, blockID, sx + dx, sy + dy, sz + dz, sbb);
							this.placeBlock(world, blockID, sx + dx, sy + dy, sz - dz, sbb);
							this.placeBlock(world, blockID, sx - dx, sy + dy, sz + dz, sbb);
							this.placeBlock(world, blockID, sx - dx, sy + dy, sz - dz, sbb);
							this.placeBlock(world, blockID, sx + dx, sy - dy, sz + dz, sbb);
							this.placeBlock(world, blockID, sx + dx, sy - dy, sz - dz, sbb);
							this.placeBlock(world, blockID, sx - dx, sy - dy, sz + dz, sbb);
							this.placeBlock(world, blockID, sx - dx, sy - dy, sz - dz, sbb);
						}
					}
				}
			}
		}
	}

	/**
	 * Puts a block only if leaves can go there.
	 */
	protected void placeLeafBlock(WorldGenLevel world, BlockState blockID, int x, int y, int z, BoundingBox sbb) {
		int offX = this.getWorldX(x, z);
		int offY = this.getWorldY(y);
		int offZ = this.getWorldZ(x, z);

		if (sbb.isInside(offX, offY, offZ)) {
			BlockPos pos = new BlockPos(offX, offY, offZ);
			if (FeatureLogic.worldGenReplaceable(world.getBlockState(pos))) {
				world.setBlock(pos, blockID, 2);
			}
		}
	}
}
