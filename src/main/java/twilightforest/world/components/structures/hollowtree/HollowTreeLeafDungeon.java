package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.FeatureLogic;

public class HollowTreeLeafDungeon extends StructurePiece {
	private final int radius;

	private final BlockStateProvider wood;
	private final BlockStateProvider leaves;
	private final BlockStateProvider inside;
	private final BlockStateProvider lootContainer;
	private final ResourceLocation lootTable;
	private final Holder<EntityType<?>> monster;

	/**
	 * Make a blob of leaves
	 */
	protected HollowTreeLeafDungeon(int index, int x, int y, int z, int radius, BlockStateProvider wood, BlockStateProvider leaves, BlockStateProvider inside, BlockStateProvider lootContainer, ResourceLocation lootTable, Holder<EntityType<?>> monster) {
		super(TFStructurePieceTypes.TFHTLD.value(), index, new BoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));

		this.setOrientation(Direction.SOUTH);

		this.radius = radius;

		this.wood = wood;
		this.leaves = leaves;
		this.inside = inside;
		this.lootContainer = lootContainer;
		this.lootTable = lootTable;
		this.monster = monster;
	}

	/**
	 * Load from NBT
	 */
	public HollowTreeLeafDungeon(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFHTLD.value(), tag);

		this.radius = tag.getInt("leafRadius");

		// FIXME codec
		this.wood = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_WOOD.value().defaultBlockState());
		this.leaves = BlockStateProvider.simple(TFBlocks.TWILIGHT_OAK_LEAVES.value().defaultBlockState());
		this.inside = BlockStateProvider.simple(Blocks.AIR.defaultBlockState());
		this.lootContainer = BlockStateProvider.simple(Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH));
		this.lootTable = TFLootTables.TREE_CACHE.lootTable;
		this.monster = TFEntities.SWARM_SPIDER;
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
		this.drawBlockBlob(level, writeableBounds, this.radius, this.radius, this.radius, 4, true, random, this.leaves);
		// then wood
		this.drawBlockBlob(level, writeableBounds, this.radius, this.radius, this.radius, 3, false, random, this.wood);
		// then air
		this.drawBlockBlob(level, writeableBounds, this.radius, this.radius, this.radius, 2, false, random, this.inside);

		// then treasure chest
		// which direction is this chest in?
		this.placeTreasureAtCurrentPosition(level, this.radius + 2, this.radius - 1, this.radius, writeableBounds, random, this.lootContainer, this.lootTable);

		// then spawner
		this.placeSpawnerAtCurrentPosition(level, random, this.radius, this.radius, this.radius, this.monster.value(), writeableBounds);
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 */
	protected void placeTreasureAtCurrentPosition(WorldGenLevel world, int x, int y, int z, BoundingBox sbb, RandomSource random, BlockStateProvider stateProvider, ResourceLocation lootTable) {
		BlockPos pos = this.getWorldPos(x, y, z);
		BlockState state = stateProvider.getState(random, pos);

		if (sbb.isInside(pos) && !world.getBlockState(pos).is(state.getBlock())) {
			world.setBlock(pos, state, 2);

			if (world.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity lootContainer)
				lootContainer.setLootTable(lootTable, random.nextLong());
		}
	}

	/**
	 * Place a monster spawner at the specified coordinates
	 */
	protected void placeSpawnerAtCurrentPosition(WorldGenLevel world, RandomSource rand, int x, int y, int z, EntityType<? extends Entity> monsterID, BoundingBox sbb) {
		BlockPos pos = this.getWorldPos(x, y, z);

		if (sbb.isInside(pos) && !world.getBlockState(pos).is(Blocks.SPAWNER)) {
			world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);

			if (world.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner)
				spawner.setEntityId(monsterID, rand);
		}
	}

	private void drawBlockBlob(WorldGenLevel world, BoundingBox sbb, int sx, int sy, int sz, int blobRadius, boolean isLeaves, RandomSource random, BlockStateProvider stateProvider) {
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
							this.placeLeafBlock(world, stateProvider, sx + dx, sy + dy, sz + dz, sbb, random);
							this.placeLeafBlock(world, stateProvider, sx + dx, sy + dy, sz - dz, sbb, random);
							this.placeLeafBlock(world, stateProvider, sx - dx, sy + dy, sz + dz, sbb, random);
							this.placeLeafBlock(world, stateProvider, sx - dx, sy + dy, sz - dz, sbb, random);
							this.placeLeafBlock(world, stateProvider, sx + dx, sy - dy, sz + dz, sbb, random);
							this.placeLeafBlock(world, stateProvider, sx + dx, sy - dy, sz - dz, sbb, random);
							this.placeLeafBlock(world, stateProvider, sx - dx, sy - dy, sz + dz, sbb, random);
							this.placeLeafBlock(world, stateProvider, sx - dx, sy - dy, sz - dz, sbb, random);
						} else {
							this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy + dy, sz + dz, sbb);
							this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy + dy, sz - dz, sbb);
							this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy + dy, sz + dz, sbb);
							this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy + dy, sz - dz, sbb);
							this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy - dy, sz + dz, sbb);
							this.placeProvidedBlock(world, stateProvider, random, sx + dx, sy - dy, sz - dz, sbb);
							this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy - dy, sz + dz, sbb);
							this.placeProvidedBlock(world, stateProvider, random, sx - dx, sy - dy, sz - dz, sbb);
						}
					}
				}
			}
		}
	}

	private void placeProvidedBlock(WorldGenLevel world, BlockStateProvider filler, RandomSource random, int sx, int sy, int sz, BoundingBox sbb) {
		this.placeBlock(world, filler.getState(random, this.getWorldPos(sx, sy, sz)), sx, sy, sz, sbb);
	}

	/**
	 * Puts a block only if leaves can go there.
	 */
	protected void placeLeafBlock(WorldGenLevel world, BlockStateProvider stateProvider, int x, int y, int z, BoundingBox sbb, RandomSource random) {
		int offX = this.getWorldX(x, z);
		int offY = this.getWorldY(y);
		int offZ = this.getWorldZ(x, z);

		if (sbb.isInside(offX, offY, offZ)) {
			BlockPos pos = new BlockPos(offX, offY, offZ);
			if (FeatureLogic.worldGenReplaceable(world.getBlockState(pos))) {
				world.setBlock(pos, stateProvider.getState(random, pos), 2);
			}
		}
	}
}
