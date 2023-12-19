package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.data.custom.stalactites.entry.Stalactite;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.RectangleLatticeIterator;
import twilightforest.world.components.feature.BlockSpikeFeature;

import java.util.Map;

public class YetiCaveComponent extends HollowHillComponent {
	private static final Stalactite BLUE_ICE_SPIKE = new Stalactite(Map.of(Blocks.BLUE_ICE, 1), 1.0F, 8, 1);
	private static final Stalactite PACKED_ICE_SPIKE = new Stalactite(Map.of(Blocks.PACKED_ICE, 1), 0.5F, 9, 1);
	private static final Stalactite ICE_SPIKE = new Stalactite(Map.of(Blocks.ICE, 1), 0.6F, 10, 1);

	public YetiCaveComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFYeti.get(), nbt);
	}

	public YetiCaveComponent(int i, int x, int y, int z, RectangleLatticeIterator.TriangularLatticeConfig spikePlacement) {
		super(TFStructurePieceTypes.TFYeti.get(), i, 2, x, y, z, spikePlacement);
	}

	/**
	 * Add in all the blocks we're adding.
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox writeableBounds, ChunkPos chunkPosIn, BlockPos blockPos) {
		// fill in features

//		// ore or glowing stalactites! (smaller, less plentiful)
//		for (int i = 0; i < sn; i++)
//		{
//			int[] dest = getCoordsInHill2D(rand);
//			generateOreStalactite(world, dest[0], 1, dest[1], writeableBounds);
//		}

		int maxRadius = 24;

		BlockPos center = this.getLocatorPosition();
		for (BlockPos.MutableBlockPos dest : this.spikePlacement.boundedGrid(writeableBounds, 0)) {
			int xDist = Math.abs(dest.getX() - center.getX());
			int zDist = Math.abs(dest.getZ() - center.getZ());

			if (xDist <= maxRadius && zDist <= maxRadius) {
				BlockPos ceiling = dest.atY(15 - Math.min(xDist, zDist) / 6);

				// TODO Make configurable
				Stalactite spike = switch (rand.nextInt(4)) {
					case 3 -> BlockSpikeFeature.STONE_STALACTITE;
					case 2 -> ICE_SPIKE;
					case 1 -> PACKED_ICE_SPIKE;
					default -> BLUE_ICE_SPIKE;
				};

				this.generateBlockSpike(world, spike, ceiling.getX(), ceiling.getY(), ceiling.getZ(), writeableBounds, true);
			}
		}

		// spawn alpha yeti
		final BlockState yetiSpawner = TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get().defaultBlockState();
		this.setBlockStateRotated(world, yetiSpawner, this.radius, 1, this.radius, Rotation.NONE, writeableBounds);
	}
}