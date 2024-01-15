package twilightforest.world.components.chunkgenerators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;
import twilightforest.TFRegistries;
import twilightforest.world.components.layer.vanillalegacy.BiomeDensitySource;

/**
 * A DensityFunction implementation that enables Biomes to influence terrain formulations, if in the noise chunk generator.
 *
 * @param biomeDensitySource A BiomeDensitySource containing TerrainColumns, providing per-biome scaling and depth behavior that allows biomes to distinguish their landscapes.
 * @param lowerDensityBound Lower clamp bound
 * @param upperDensityBound Upper clamp bound
 * @param baseFactor Density function (can be constant) for the height of the vertical y-gradient at a given X-Z position. A biome speeds or slows this vertical rate of change.
 * @param baseOffset Density function (can be constant) for the elevation of the vertical y-gradient at a given X-Z position. A biome moves it up and down.
 */
public record BiomeTerrainWarpRouter(Holder<BiomeDensitySource> biomeDensitySource, double lowerDensityBound, double upperDensityBound, double depthScalar, DensityFunction baseFactor, DensityFunction baseOffset) implements DensityFunction.SimpleFunction {
    public static final KeyDispatchDataCodec<BiomeTerrainWarpRouter> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.create(inst -> inst.group(
            RegistryFileCodec.create(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC, false).fieldOf("terrain_source").forGetter(BiomeTerrainWarpRouter::biomeDensitySource),
            Codec.doubleRange(-64, 0).fieldOf("lower_density_bound").forGetter(BiomeTerrainWarpRouter::lowerDensityBound),
            Codec.doubleRange(0, 64).fieldOf("upper_density_bound").forGetter(BiomeTerrainWarpRouter::upperDensityBound),
            Codec.doubleRange(0, 32).orElse(8.0).fieldOf("depth_scalar").forGetter(BiomeTerrainWarpRouter::depthScalar),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base_factor").forGetter(BiomeTerrainWarpRouter::baseFactor),
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("base_offset").forGetter(BiomeTerrainWarpRouter::baseOffset)
    ).apply(inst, BiomeTerrainWarpRouter::new)));

    @Override
    public double compute(FunctionContext context) {
        BiomeDensitySource.DensityData densityData = this.biomeDensitySource.value().sampleTerrain(context.blockX(), context.blockZ(), context);

        double gradientSizeVertical = this.baseFactor.compute(context) * densityData.scale;

        double yOffset = (this.baseOffset.compute(context) + densityData.depth) * this.depthScalar - context.blockY();

        double yShiftedHeight = yOffset / gradientSizeVertical;

        return Mth.clamp(yShiftedHeight, this.lowerDensityBound, this.upperDensityBound);
    }

    @Override
    public double minValue() {
        return this.lowerDensityBound;
    }

    @Override
    public double maxValue() {
        return this.upperDensityBound;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}
