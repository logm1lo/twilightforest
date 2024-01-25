package twilightforest.world.components.chunkblanketing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.init.TFBlocks;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.util.LegacyLandmarkPlacements;

import java.util.Set;
import java.util.function.Function;

@Deprecated // TODO: Move to Troll Clouds Structure
public record TrollCloudProcessor(HolderSet<Biome> biomesForApplication, int height, Holder<Structure> structureFor) implements ChunkBlanketProcessor {
    public static final Codec<TrollCloudProcessor> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            RegistryCodecs.homogeneousList(Registries.BIOME, true).fieldOf("biome_mask").forGetter(TrollCloudProcessor::biomesForApplication),
            Codec.INT.fieldOf("height").forGetter(TrollCloudProcessor::height),
            RegistryFixedCodec.create(Registries.STRUCTURE).fieldOf("structure").forGetter(TrollCloudProcessor::structureFor)
    ).apply(inst, TrollCloudProcessor::new));

    @Override
    public void processChunk(RandomSource random, Function<BlockPos, Holder<Biome>> biomeGetter, ChunkAccess chunkAccess) {
        BlockPos nearestCenterXZ = LegacyLandmarkPlacements.getNearestCenterXZ(chunkAccess.getPos().x, chunkAccess.getPos().z).subtract(chunkAccess.getPos().getWorldPosition());
        Set<Structure> structuresThroughChunk = chunkAccess.getAllReferences().keySet();

        if (structuresThroughChunk.contains(this.structureFor.value())) {
            deformTerrainForTrollCloud(chunkAccess, nearestCenterXZ.getX(), nearestCenterXZ.getZ(), this.height, random);
        }
    }

	private static void deformTerrainForTrollCloud(ChunkAccess chunkAccess, int hx, int hz, int cloudHeight, RandomSource random) {
		ChunkPos center = chunkAccess.getPos();
		int regionX = center.x + 8 >> 4;
		int regionZ = center.z + 8 >> 4;

		long seed = regionX * 3129871L ^ regionZ * 116129781L;
		seed = seed * seed * 42317861L + seed * 7L;

		int num0 = (int) (seed >> 12 & 3L);
		int num1 = (int) (seed >> 15 & 3L);
		int num2 = (int) (seed >> 18 & 3L);
		int num3 = (int) (seed >> 21 & 3L);
		int num4 = (int) (seed >> 9 & 3L);
		int num5 = (int) (seed >> 6 & 3L);
		int num6 = (int) (seed >> 3 & 3L);
		int num7 = (int) (seed & 3L);

		for (int bx = 0; bx < 4; bx++) {
			for (int bz = 0; bz < 4; bz++) {
				int dx = bx * 4 - hx - 2;
				int dz = bz * 4 - hz - 2;

				// generate several centers for other clouds

				int dx2 = dx + num0 * 5 - num1 * 4;
				int dz2 = dz + num2 * 4 - num3 * 5;
				int dx3 = dx + num4 * 5 - num5 * 4;
				int dz3 = dz + num6 * 4 - num7 * 5;

				// take the minimum distance to any center
				float dist0 = Mth.sqrt(dx * dx + dz * dz) / 4.0f;
				float dist2 = Mth.sqrt(dx2 * dx2 + dz2 * dz2) / 3.5f;
				float dist3 = Mth.sqrt(dx3 * dx3 + dz3 * dz3) / 4.5f;

				double dist = Math.min(dist0, Math.min(dist2, dist3));

				float pr = random.nextFloat();
				double cv = dist - 7F - pr * 3.0F;

				// randomize depth and height
				int y = cloudHeight;
				int depth = 4;

				if (pr < 0.1F) {
					y++;
				}
				if (pr > 0.6F) {
					depth++;
				}
				if (pr > 0.9F) {
					depth++;
				}

				// generate cloud
				for (int sx = 0; sx < 4; sx++) {
					for (int sz = 0; sz < 4; sz++) {
						int lx = bx * 4 + sx;
						int lz = bz * 4 + sz;

						BlockPos movingPos = center.getWorldPosition().offset(lx, 0, lz);

						if (dist < 7 || cv < 0.05F) {
							chunkAccess.setBlockState(movingPos.atY(y), TFBlocks.WISPY_CLOUD.get().defaultBlockState(), false);
							for (int d = 1; d < depth; d++) {
								chunkAccess.setBlockState(movingPos.atY(y - d), TFBlocks.FLUFFY_CLOUD.get().defaultBlockState(), false);
							}
							chunkAccess.setBlockState(movingPos.atY(y - depth), TFBlocks.WISPY_CLOUD.get().defaultBlockState(), false);
						} else if (dist < 8 || cv < 1F) {
							for (int d = 1; d < depth; d++) {
								chunkAccess.setBlockState(movingPos.atY(y - d), TFBlocks.FLUFFY_CLOUD.get().defaultBlockState(), false);
							}
						}
					}
				}
			}
		}
	}

    @Override
    public ChunkBlanketType getType() {
        return ChunkBlanketProcessors.CLOUDS.value();
    }
}
