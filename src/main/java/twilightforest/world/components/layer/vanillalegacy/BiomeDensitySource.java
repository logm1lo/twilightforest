package twilightforest.world.components.layer.vanillalegacy;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.LinearCongruentialGenerator;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFDimensionSettings;
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

    @NotNull
    public Holder<Biome> getBiomeColumnKey(int biomeX, int biomeZ) {
        return this.biomeList.get(this.genBiomes.get().getBiome(biomeX, biomeZ)).getMainBiome();
    }

    public Holder<Biome> getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biomeList.get(this.genBiomes.get().getBiome(biomeX, biomeZ)).getBiome(biomeY);
    }

    public double getBiomeDepth(int biomeX, int biomeZ, DensityFunction.FunctionContext context) {
        return this.getBiomeDepth(this.genBiomes.get().getBiome(biomeX, biomeZ), context);
    }

    @SuppressWarnings("OptionalIsPresent")
    public double getBiomeDepth(ResourceKey<Biome> biome, DensityFunction.FunctionContext context) {
        Optional<TerrainColumn> terrainColumn = this.getTerrainColumn(biome);
        if (terrainColumn.isEmpty()) return 0;
        return terrainColumn.get().depth(context);
    }

    public Optional<TerrainColumn> getTerrainColumn(int biomeX, int biomeZ) {
        return this.getTerrainColumn(this.genBiomes.get().getBiome(biomeX, biomeZ));
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

    public static final class DensityData {
        public final double depth;
        public final double scale;

        public DensityData(double depth, double scale) {
            this.depth = depth;
            this.scale = scale;
        }
    }

    // Thanks k.jpg!

    private static final double BLEND_RADIUS = 8;
    private static final int BLEND_RADIUS_INT = Mth.floor(BLEND_RADIUS + 1);
    private static final int BIOME_QUART_Y = 64 >> QuartPos.BITS;

    private static final int BLOCK_XYZ_OFFSET = QuartPos.SIZE / 2;
    private static final int FIDDLE_HASH_BIT_START = 24;
    private static final int FIDDLE_HASH_BIT_COUNT = 10;
    private static final int FIDDLE_HASH_BIT_SHIFTED = 1 << FIDDLE_HASH_BIT_COUNT;
    private static final int FIDDLE_HASH_BIT_MASK = FIDDLE_HASH_BIT_SHIFTED - 1;
    private static final double FIDDLE_MAGNITUDE = 1.0; // 0.9 in net.minecraft.world.level.biome.BiomeManager

    public DensityData sampleTerrain(int blockX, int blockZ, DensityFunction.FunctionContext context) {
        double totalScale = 0.0;
        double totalMappedDepth = 0.0;
        double totalContribution = 0.0;

        long biomeZoomSeed = TFDimensionSettings.seed;

        int blockXWithOffset = blockX - BLOCK_XYZ_OFFSET;
        int blockZWithOffset = blockZ - BLOCK_XYZ_OFFSET;

        int xQuartStart = (blockXWithOffset - BLEND_RADIUS_INT) >> QuartPos.BITS;
        int zQuartStart = (blockZWithOffset - BLEND_RADIUS_INT) >> QuartPos.BITS;
        int xQuartEnd = (blockXWithOffset + BLEND_RADIUS_INT) >> QuartPos.BITS;
        int zQuartEnd = (blockZWithOffset + BLEND_RADIUS_INT) >> QuartPos.BITS;
        int xCount = xQuartEnd - xQuartStart + 1;
        int zCount = zQuartEnd - zQuartStart + 1;

        double xQuartDelta = (blockXWithOffset - (xQuartStart << QuartPos.BITS)) * (1.0 / QuartPos.SIZE);
        double zQuartDelta = (blockZWithOffset - (zQuartStart << QuartPos.BITS)) * (1.0 / QuartPos.SIZE);

        for (int cz = 0, cx = 0;;) {
            double fiddledDistanceSquared = getFiddledDistance(biomeZoomSeed, cx + xQuartStart, BIOME_QUART_Y, cz + zQuartStart, xQuartDelta - cx, 0, zQuartDelta - cz);

            considerColumn:
            if (fiddledDistanceSquared < BLEND_RADIUS * BLEND_RADIUS) {
                double falloff = BLEND_RADIUS * BLEND_RADIUS - fiddledDistanceSquared;
                falloff *= falloff * falloff;

                Optional<TerrainColumn> terrainColumn = this.getTerrainColumn(cx + xQuartStart, cz + zQuartStart);
                if (terrainColumn.isEmpty()) break considerColumn;

                double neighborDepth = terrainColumn.get().depth(context);
                double neighborScale = terrainColumn.get().scale(context);

                falloff *= Math.exp(-neighborDepth);

                totalMappedDepth += neighborDepth * falloff;
                totalScale += neighborScale * falloff;
                totalContribution += falloff;
            }

            cz++;
            if (cz < zCount) continue;
            cz = 0;
            cx++;
            if (cx >= xCount) break;
        }

        double depthNormalized = totalMappedDepth / totalContribution;
        double scaleNormalized = totalScale / totalContribution;

        return new DensityData(depthNormalized, scaleNormalized);
    }

    private static double getFiddledDistance(long seed, int quartX, int quartY, int quartZ, double dx, double dy, double dz) {
        long hash = LinearCongruentialGenerator.next(seed, quartX);
        hash = LinearCongruentialGenerator.next(hash, quartY);
        hash = LinearCongruentialGenerator.next(hash, quartZ);
        hash = LinearCongruentialGenerator.next(hash, quartX);
        hash = LinearCongruentialGenerator.next(hash, quartY);
        hash = LinearCongruentialGenerator.next(hash, quartZ);
        double jz = getFiddle(hash);
        hash = LinearCongruentialGenerator.next(hash, seed);
        double jy = getFiddle(hash);
        hash = LinearCongruentialGenerator.next(hash, seed);
        double jx = getFiddle(hash);
        return Mth.square(dz + jz) + Mth.square(dy + jy) + Mth.square(dx + jx);
    }

    private static double getFiddle(long hash) {
        long hashBits = (hash >> FIDDLE_HASH_BIT_START) & FIDDLE_HASH_BIT_MASK;
        return hashBits * (FIDDLE_MAGNITUDE / FIDDLE_HASH_BIT_SHIFTED) - 0.5 * FIDDLE_MAGNITUDE;
    }
}
