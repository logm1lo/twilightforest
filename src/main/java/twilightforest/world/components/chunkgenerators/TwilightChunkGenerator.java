package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFLandmark;
import twilightforest.util.LegacyLandmarkPlacements;
import twilightforest.util.Vec2i;
import twilightforest.world.components.structures.TFStructureComponent;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.world.components.structures.type.HollowHillStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class TwilightChunkGenerator extends ChunkGeneratorWrapper {
	public static final Codec<TwilightChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			ChunkGenerator.CODEC.fieldOf("wrapped_generator").forGetter(o -> o.delegate)
	).apply(instance, TwilightChunkGenerator::new));

	private final BlockState defaultBlock;

    public TwilightChunkGenerator(ChunkGenerator delegate) {
		super(delegate);

        if (delegate instanceof NoiseBasedChunkGenerator noiseGen && noiseGen.generatorSettings().isBound()) {
			this.defaultBlock = noiseGen.generatorSettings().value().defaultBlock();
        } else {
			this.defaultBlock = Blocks.STONE.defaultBlockState();
        }
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public void buildSurface(WorldGenRegion world, StructureManager manager, RandomState random, ChunkAccess chunk) {
		this.deformTerrainForFeature(world);

		super.buildSurface(world, manager, random, chunk);
	}

	@Override
	public void addDebugScreenInfo(List<String> p_223175_, RandomState p_223176_, BlockPos p_223177_) {
		//do we do anything with this? we need to implement it for some reason
	}

	protected final void deformTerrainForFeature(WorldGenRegion primer) {
		Vec2i featureRelativePos = new Vec2i();
		TFLandmark nearFeature = LegacyLandmarkPlacements.getNearestLandmark(primer.getCenter().x, primer.getCenter().z, primer, featureRelativePos);

		//Optional<StructureStart<?>> structureStart = TFGenerationSettings.locateTFStructureInRange(primer.getLevel(), nearFeature, chunk.getPos().getWorldPosition(), nearFeature.size + 1);

		if (!nearFeature.requiresTerraforming) {
			return;
		}

		final int relativeFeatureX = featureRelativePos.x;
		final int relativeFeatureZ = featureRelativePos.z;

		if (nearFeature == TFLandmark.HEDGE_MAZE || nearFeature == TFLandmark.NAGA_COURTYARD || nearFeature == TFLandmark.QUEST_GROVE) {
			for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
				for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
					int featureDX = xInChunk - relativeFeatureX;
					int featureDZ = zInChunk - relativeFeatureZ;
					flattenTerrainForFeature(primer, nearFeature.size, xInChunk, zInChunk, featureDX, this.getSeaLevel(), featureDZ);
				}
			}
		} else if (nearFeature == TFLandmark.YETI_CAVE) {
			for (int xInChunk = 0; xInChunk < 16; xInChunk++) {
				for (int zInChunk = 0; zInChunk < 16; zInChunk++) {
					int featureDX = xInChunk - relativeFeatureX;
					int featureDZ = zInChunk - relativeFeatureZ;

					this.deformTerrainForYetiLair(primer, nearFeature.size, xInChunk, zInChunk, featureDX, featureDZ);
				}
			}
		}

		// done!
	}

	private static void flattenTerrainForFeature(WorldGenRegion primer, int size, int x, int z, int dx, int dy, int dz) {

		float squishFactor = 0f;
		int mazeHeight = dy + 5;
		final int FEATURE_BOUNDARY = (size * 2 + 1) * 8 - 8;

		if (dx <= -FEATURE_BOUNDARY) {
			squishFactor = (-dx - FEATURE_BOUNDARY) / 8.0f;
		} else if (dx >= FEATURE_BOUNDARY) {
			squishFactor = (dx - FEATURE_BOUNDARY) / 8.0f;
		}

		if (dz <= -FEATURE_BOUNDARY) {
			squishFactor = Math.max(squishFactor, (-dz - FEATURE_BOUNDARY) / 8.0f);
		} else if (dz >= FEATURE_BOUNDARY) {
			squishFactor = Math.max(squishFactor, (dz - FEATURE_BOUNDARY) / 8.0f);
		}

		if (squishFactor > 0f) {
			// blend the old terrain height to arena height
			for (int y = 0; y <= 127; y++) {
				BlockPos old = primer.getCenter().getWorldPosition().offset(x, 0, z);
				Block currentTerrain = primer.getBlockState(old.atY(y)).getBlock();
				// we're still in ground
				if (currentTerrain != Blocks.STONE) {
					// we found the lowest chunk of earth
					mazeHeight += ((y - mazeHeight) * squishFactor);
					break;
				}
			}
		}

		// sets the ground level to the maze height, but dont move anything in rivers
		for (int y = 0; y < mazeHeight; y++) {
			BlockPos old2 = primer.getCenter().getWorldPosition().offset(x, 0, z);
			BlockState b = primer.getBlockState(old2.atY(y));
			BlockPos old1 = primer.getCenter().getWorldPosition().offset(x, 0, z);
			if(!primer.getBiome(old1.atY(y)).is(TFBiomes.STREAM)) {
				if (b.isAir() || b.liquid()) {
					BlockPos old = primer.getCenter().getWorldPosition().offset(x, 0, z);
					primer.setBlock(old.atY(y), Blocks.STONE.defaultBlockState(), 3);
				}
			}
		}

		for (int y = mazeHeight; y <= 127; y++) {
			BlockPos old2 = primer.getCenter().getWorldPosition().offset(x, 0, z);
			BlockState b = primer.getBlockState(old2.atY(y));
			BlockPos old1 = primer.getCenter().getWorldPosition().offset(x, 0, z);
			if(!primer.getBiome(old1.atY(y)).is(TFBiomes.STREAM)) {
				if (!b.isAir() && !b.liquid()) {
					BlockPos old = primer.getCenter().getWorldPosition().offset(x, 0, z);
					primer.setBlock(old.atY(y), Blocks.AIR.defaultBlockState(), 3);
				}
			}
		}
	}

	private void deformTerrainForYetiLair(WorldGenRegion primer, int size, int xInChunk, int zInChunk, int featureDX, int featureDZ) {
		float squishFactor = 0f;
		int topHeight = this.getSeaLevel() + 24;
		int outerBoundary = (size * 2 + 1) * 8 - 8;

		// outer boundary
		if (featureDX <= -outerBoundary) {
			squishFactor = (-featureDX - outerBoundary) / 8.0f;
		} else if (featureDX >= outerBoundary) {
			squishFactor = (featureDX - outerBoundary) / 8.0f;
		}

		if (featureDZ <= -outerBoundary) {
			squishFactor = Math.max(squishFactor, (-featureDZ - outerBoundary) / 8.0f);
		} else if (featureDZ >= outerBoundary) {
			squishFactor = Math.max(squishFactor, (featureDZ - outerBoundary) / 8.0f);
		}

		// inner boundary
		int caveBoundary = size * 2 * 8 - 8;
		int hollowCeiling;

		int offset = Math.min(Math.abs(featureDX), Math.abs(featureDZ));
		hollowCeiling = this.getSeaLevel() + 40 - offset * 4;

		// center square cave
		if (featureDX >= -caveBoundary && featureDZ >= -caveBoundary && featureDX <= caveBoundary && featureDZ <= caveBoundary) {
			hollowCeiling = this.getSeaLevel() + 16;
		}

		// slope ceiling slightly
		hollowCeiling -= offset / 6;

		// max out ceiling 8 blocks from roof
		hollowCeiling = Math.min(hollowCeiling, this.getSeaLevel() + 16);

		// floor, also with slight slope
		int hollowFloor = this.getSeaLevel() - 4 + offset / 6;

		BlockPos.MutableBlockPos movingPos = primer.getCenter().getWorldPosition().offset(xInChunk, 0, zInChunk).mutable();

		if (squishFactor > 0f) {
			// blend the old terrain height to arena height
			for (int y = primer.getMinBuildHeight(); y <= primer.getMaxBuildHeight(); y++) {
				if (!this.defaultBlock.equals(primer.getBlockState(movingPos.setY(y)))) {
					// we found the lowest chunk of earth
					topHeight += (y - topHeight) * squishFactor;
					hollowFloor += (y - hollowFloor) * squishFactor;
					break;
				}
			}
		}

		// carve the cave into the stone

		// add stone
		for (int y = primer.getMinBuildHeight(); y < topHeight; y++) {
			Block b = primer.getBlockState(movingPos.setY(y)).getBlock();
			if (b == Blocks.AIR || b == Blocks.WATER) {
				primer.setBlock(movingPos.setY(y), this.defaultBlock, 3);
			}
		}

		// hollow out inside
		for (int y = hollowFloor + 1; y < hollowCeiling; ++y) {
			primer.setBlock(movingPos.setY(y), Blocks.AIR.defaultBlockState(), 3);
		}

		// ice floor
		if (hollowFloor < hollowCeiling && hollowFloor < this.getSeaLevel() + 3) {
			primer.setBlock(movingPos.setY(hollowFloor), Blocks.PACKED_ICE.defaultBlockState(), 3);
		}
	}

	private static int getSpawnListIndexAt(StructureStart start, BlockPos pos) {
		int highestFoundIndex = -1;
		for (StructurePiece component : start.getPieces()) {
			if (component.getBoundingBox().isInside(pos)) {
				if (component instanceof TFStructureComponent tfComponent) {
					if (tfComponent.spawnListIndex > highestFoundIndex)
						highestFoundIndex = tfComponent.spawnListIndex;
				} else
					return 0;
			}
		}
		return highestFoundIndex;
	}

	@Nullable
	public static List<MobSpawnSettings.SpawnerData> gatherPotentialSpawns(@Nullable TwilightChunkGenerator key, StructureManager structureManager, MobCategory classification, BlockPos pos) {
		Iterable<Structure> structures = structureManager.registryAccess().registryOrThrow(Registries.STRUCTURE);
		if (key != null) {
			List<Structure> l = ControlledSpawnsCache.CONTROLLED_SPAWNS.get(key);
			if (l == null) {
				List<Structure> list = new ArrayList<>();
				for (Structure structure : structures)
					if (structure instanceof ControlledSpawns)
						list.add(structure);
				ControlledSpawnsCache.CONTROLLED_SPAWNS.put(key, list);
				structures = list;
			} else
				structures = l;
		}
		for (Structure structure : structures) {
			if (structure instanceof ControlledSpawns landmark) {
				StructureStart start = structureManager.getStructureAt(pos, structure);
				if (!start.isValid())
					continue;

				if (classification != MobCategory.MONSTER)
					return landmark.getSpawnableList(classification);

				if (start instanceof TFStructureStart s && s.isConquered())
					return null;

				// FIXME Make interface for this method?
				if (structure instanceof HollowHillStructure hollowHill && !hollowHill.canSpawnMob(pos, start.getBoundingBox()))
					return null;

				final int index = getSpawnListIndexAt(start, pos);
				if (index < 0)
					return null;
				return landmark.getSpawnableMonsterList(index);
			}
		}
		return null;
	}

	@Override
	public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureManager structureManager, MobCategory mobCategory, BlockPos pos) {
		List<MobSpawnSettings.SpawnerData> potentialStructureSpawns = gatherPotentialSpawns(this, structureManager, mobCategory, pos);
		if (potentialStructureSpawns != null)
			return WeightedRandomList.create(potentialStructureSpawns);

		return super.getMobsAt(biome, structureManager, mobCategory, pos);
	}
}
