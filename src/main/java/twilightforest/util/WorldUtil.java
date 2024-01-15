package twilightforest.util;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.placements.BiomeGridLandmarkPlacement;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.Map;
import java.util.Set;

public final class WorldUtil {
	private WorldUtil() {}

	/**
	 * Inclusive of edges
	 */
	public static Iterable<BlockPos> getAllAround(BlockPos center, int range) {
		return BlockPos.betweenClosed(center.offset(-range, -range, -range), center.offset(range, range, range));
	}

	/**
	 * Floors both corners of the bounding box to integers
	 * Inclusive of edges
	 */
	public static Iterable<BlockPos> getAllInBB(AABB bb) {
		return BlockPos.betweenClosed((int) bb.minX, (int) bb.minY, (int) bb.minZ, (int) bb.maxX, (int) bb.maxY, (int) bb.maxZ);
	}

	public static BlockPos randomOffset(RandomSource random, BlockPos pos, int range) {
		return randomOffset(random, pos, range, range, range);
	}

	public static BlockPos randomOffset(RandomSource random, BlockPos pos, int rx, int ry, int rz) {
		int dx = random.nextInt(rx * 2 + 1) - rx;
		int dy = random.nextInt(ry * 2 + 1) - ry;
		int dz = random.nextInt(rz * 2 + 1) - rz;
		return pos.offset(dx, dy, dz);
	}

	@Nullable
	public static ChunkGenerator getChunkGenerator(LevelAccessor level) {
		if (level.getChunkSource() instanceof ServerChunkCache chunkSource)
			return chunkSource.chunkMap.generator();

		return null;
	}

	public static int getSeaLevel(ChunkGenerator generator) {
		if (generator instanceof ChunkGenerator) {
			return generator.getSeaLevel();
		} else return TFGenerationSettings.SEALEVEL;
	}

	public static int getBaseHeight(LevelAccessor level, int x, int z, Heightmap.Types type) {
		if (level.getChunkSource() instanceof ServerChunkCache chunkSource) {
			return chunkSource.chunkMap.generator().getBaseHeight(x, z, type, level, chunkSource.randomState());
		} else {
			return level.getHeight(type, x, z);
		}
	}

	@Nullable
	public static Pair<BlockPos, Holder<Structure>> findNearestMapLandmark(ServerLevel level, HolderSet<Structure> targetStructures, BlockPos pos, int chunkSearchRadius, @SuppressWarnings("unused") boolean skipKnownStructures) {
		TwilightForestMod.LOGGER.info("findNearestMapLandmark: " + targetStructures);
		ChunkGeneratorStructureState state = level.getChunkSource().getGeneratorState();

		Map<BiomeGridLandmarkPlacement, Set<Holder<Structure>>> seekStructures = new Object2ObjectArrayMap<>();

		for (Holder<Structure> holder : targetStructures) {
			for (StructurePlacement structureplacement : state.getPlacementsForStructure(holder)) {
				if (structureplacement instanceof BiomeGridLandmarkPlacement landmarkPlacement) {
					seekStructures.computeIfAbsent(landmarkPlacement, v -> new ObjectArraySet<>()).add(holder);
				}
			}
		}

		if (seekStructures.isEmpty()) return null;

		double distance = Double.MAX_VALUE;

		@Nullable Pair<BlockPos, Holder<Structure>> nearest = null;

		for (BlockPos landmarkCenterPosition : LegacyLandmarkPlacements.landmarkCenterScanner(pos, chunkSearchRadius)) {
			for (Map.Entry<BiomeGridLandmarkPlacement, Set<Holder<Structure>>> landmarkPlacement : seekStructures.entrySet()) {
				if (!landmarkPlacement.getKey().isStructureChunk(state, landmarkCenterPosition.getX() >> 4, landmarkCenterPosition.getZ() >> 4)) continue;

				for (Holder<Structure> targetStructure : targetStructures) {
					if (landmarkPlacement.getValue().contains(targetStructure)) {
                        Holder<Biome> biome = level.getBiome(landmarkCenterPosition);

						if (targetStructure.value().biomes().contains(biome)) {
							final double newDistance = landmarkCenterPosition.distToLowCornerSqr(pos.getX(), 0, pos.getZ());

							if (newDistance < distance) {
								nearest = new Pair<>(landmarkCenterPosition, targetStructure);
								distance = newDistance;
							}
						}
					}
				}
			}
		}

		return nearest;
	}
}
