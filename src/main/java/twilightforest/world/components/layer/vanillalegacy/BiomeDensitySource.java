package twilightforest.world.components.layer.vanillalegacy;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.DensityFunction;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.world.components.chunkgenerators.TerrainColumn;
import twilightforest.world.components.layer.vanillalegacy.area.LazyArea;
import twilightforest.world.components.layer.vanillalegacy.context.LazyAreaContext;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BiomeDensitySource {
    public static final Codec<BiomeDensitySource> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            TerrainColumn.CODEC.listOf().fieldOf("biome_landscape").xmap(l -> l.stream().collect(Collectors.toMap(TerrainColumn::getResourceKey, Function.identity())), m -> m.values().stream().sorted(Comparator.comparing(TerrainColumn::getResourceKey)).toList()).forGetter(o -> o.biomeList),
            BiomeLayerStack.HOLDER_CODEC.fieldOf("biome_layer_config").forGetter(BiomeDensitySource::getBiomeConfig)
    ).apply(instance, instance.stable(BiomeDensitySource::new)));

    public static final float[] BIOME_WEIGHTS = Util.make(new float[25], (afloat) -> {
        for(int x = -2; x <= 2; ++x) {
            for(int z = -2; z <= 2; ++z) {
                float weight = 10.0F / Mth.sqrt((float)(x * x + z * z) + 0.2F);
                afloat[x + 2 + (z + 2) * 5] = weight;
            }
        }
    });

    private final Map<ResourceKey<Biome>, TerrainColumn> biomeList;

    private final Holder<BiomeLayerFactory> genBiomeConfig;
    private final Supplier<LazyArea> genBiomes;

    public BiomeDensitySource(List<TerrainColumn> list, Holder<BiomeLayerFactory> biomeLayerFactory) {
        this(list.stream().collect(Collectors.toMap(TerrainColumn::getResourceKey, Function.identity())), biomeLayerFactory);
    }

    public BiomeDensitySource(Map<ResourceKey<Biome>, TerrainColumn> list, Holder<BiomeLayerFactory> biomeLayerFactory) {
        super();

        this.genBiomeConfig = biomeLayerFactory;
        this.genBiomes = Suppliers.memoize(() -> this.genBiomeConfig.value().build(salt -> new LazyAreaContext(25, salt)));

        this.biomeList = list;
    }

    private Holder<BiomeLayerFactory> getBiomeConfig() {
        return this.genBiomeConfig;
    }

    public Holder<Biome> getNoiseBiome(int x, int y, int z) {
        return this.biomeList.get(this.genBiomes.get().getBiome(x, z)).getBiome(y);
    }

    public double getBiomeDepth(int x, int z, DensityFunction.FunctionContext context) {
        return this.getBiomeDepth(this.genBiomes.get().getBiome(x, z), context);
    }

    @SuppressWarnings("OptionalIsPresent")
    public double getBiomeDepth(ResourceKey<Biome> biome, DensityFunction.FunctionContext context) {
        Optional<TerrainColumn> terrainColumn = this.getTerrainColumn(biome);
        if (terrainColumn.isEmpty()) return 0;
        return terrainColumn.get().depth(context);
    }

    public Optional<TerrainColumn> getTerrainColumn(int x, int z) {
        return this.getTerrainColumn(this.genBiomes.get().getBiome(x, z));
    }

    public Optional<TerrainColumn> getTerrainColumn(ResourceKey<Biome> biome) {
        return this.biomeList.values().stream().filter(p -> p.is(biome)).findFirst();
    }

    public Stream<Holder<Biome>> collectPossibleBiomes() {
        return this.biomeList.values().stream().flatMap(TerrainColumn::getBiomes);
    }

    public LazyArea build() {
        return this.genBiomes.get();
    }

    public DensityData sampleTerrain(int biomeX, int biomeZ, DensityFunction.FunctionContext context) {
        double totalScale = 0.0F;
        double totalDepth = 0.0F;
        double totalContribution = 0.0F;
        double centerDepth = this.getBiomeDepth(biomeX, biomeZ, context);

        for (int offX = -2; offX <= 2; ++offX) {
            for (int offZ = -2; offZ <= 2; ++offZ) {
                Optional<TerrainColumn> terrainColumn = this.getTerrainColumn(biomeX + offX, biomeZ + offZ);

                if (terrainColumn.isEmpty()) continue;

                double neighborDepth = terrainColumn.get().depth(context);
                double neighborScale = terrainColumn.get().scale(context);

                // If the center column is lower than the given neighboring column, then diminish its height contribution
                double topographicContribution = neighborDepth > centerDepth ? 0.5F : 1.0F;
                double piecewiseInfluence = topographicContribution * BIOME_WEIGHTS[offX + 2 + (offZ + 2) * 5] / (neighborDepth + 2.0);
                totalDepth += neighborDepth * piecewiseInfluence;
                totalScale += neighborScale * piecewiseInfluence;
                totalContribution += piecewiseInfluence;
            }
        }

        double depthNormalized = totalDepth / totalContribution;
        double scaleNormalized = totalScale / totalContribution;

        return new DensityData(depthNormalized, scaleNormalized);
    }

    public static final class DensityData {
        public final double depth;
        public final double scale;

        public DensityData(double depth, double scale) {
            this.depth = depth;
            this.scale = scale;
        }
    }
}
