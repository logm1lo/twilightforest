package twilightforest.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.world.components.chunkgenerators.AbsoluteDifferenceFunction;
import twilightforest.world.components.chunkgenerators.TerrainDensityRouter;
import twilightforest.world.components.chunkgenerators.FocusedDensityFunction;
import twilightforest.world.components.chunkgenerators.HollowHillFunction;
import twilightforest.world.components.layer.vanillalegacy.BiomeDensitySource;

public class TFDensityFunctions {
    public static final DeferredRegister<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, TwilightForestMod.ID);

    @SuppressWarnings("unused")
    public static final DeferredHolder<Codec<? extends DensityFunction>, Codec<TerrainDensityRouter>> BIOME_DRIVEN = register("biome_driven", TerrainDensityRouter.CODEC);
    @SuppressWarnings("unused")
    public static final DeferredHolder<Codec<? extends DensityFunction>, Codec<FocusedDensityFunction>> FOCUSED = register("focused", FocusedDensityFunction.CODEC);
    @SuppressWarnings("unused")
    public static final DeferredHolder<Codec<? extends DensityFunction>, Codec<HollowHillFunction>> HOLLOW_HILL = register("hollow_hill", HollowHillFunction.CODEC);
    @SuppressWarnings("unused")
    public static final DeferredHolder<Codec<? extends DensityFunction>, Codec<AbsoluteDifferenceFunction.Min>> COORD_MIN = register("coord_min", AbsoluteDifferenceFunction.Min.CODEC);
    @SuppressWarnings("unused")
    public static final DeferredHolder<Codec<? extends DensityFunction>, Codec<AbsoluteDifferenceFunction.Max>> COORD_MAX = register("coord_max", AbsoluteDifferenceFunction.Max.CODEC);

    public static final ResourceKey<DensityFunction> TWILIGHT_TERRAIN = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("twilight_terrain"));
    public static final ResourceKey<DensityFunction> SKYLIGHT_TERRAIN = ResourceKey.create(Registries.DENSITY_FUNCTION, TwilightForestMod.prefix("skylight_terrain"));

    private static <T extends DensityFunction> DeferredHolder<Codec<? extends DensityFunction>, Codec<T>> register(String name, KeyDispatchDataCodec<T> keyCodec) {
        return DENSITY_FUNCTION_TYPES.register(name, keyCodec::codec);
    }

    public static void bootstrap(BootstapContext<DensityFunction> context) {
        Holder.Reference<BiomeDensitySource> biomeGrid = context.lookup(TFRegistries.Keys.BIOME_TERRAIN_DATA).getOrThrow(BiomeLayerStack.BIOME_GRID);
        Holder.Reference<NormalNoise.NoiseParameters> surfaceParams = context.lookup(Registries.NOISE).getOrThrow(Noises.SURFACE);
        Holder.Reference<NormalNoise.NoiseParameters> ridgeParams = context.lookup(Registries.NOISE).getOrThrow(Noises.RIDGE);

        DensityFunction routedBiomeWarp = DensityFunctions.mul(
                DensityFunctions.constant(1/6f),
                DensityFunctions.add(
                        new TerrainDensityRouter(
                                biomeGrid,
                                new DensityFunction.NoiseHolder(surfaceParams),
                                -31,
                                64,
                                1,
                                DensityFunctions.constant(8),
                                DensityFunctions.constant(-1.25)
                        ),
                        DensityFunctions.yClampedGradient(-31, 256, 31, -256)
                )
        );

        // Debug: For a flat substitute of TerrainDensityRouter
        // routedBiomeWarp = DensityFunctions.yClampedGradient(-31, 32, 2, -2);

        DensityFunction wideNoise = mulAddHalf(DensityFunctions.noise(ridgeParams, 1, 0));

        DensityFunction thinNoise = mulAddHalf(DensityFunctions.noise(ridgeParams, 4, 0));

        DensityFunction noiseInterpolator = mulAddHalf(DensityFunctions.noise(surfaceParams, 1, 1.0/16.0));

        DensityFunction jitteredNoise = DensityFunctions.lerp(
                noiseInterpolator.clamp(0, 1),
                wideNoise,
                thinNoise
        );

        DensityFunction finalDensity = DensityFunctions.add(
                routedBiomeWarp,
                DensityFunctions.cache2d(DensityFunctions.interpolated(DensityFunctions.max(
                        DensityFunctions.zero(),
                        jitteredNoise
                )))
        );

        context.register(TWILIGHT_TERRAIN, finalDensity.clamp(-0.1, 1));

        context.register(SKYLIGHT_TERRAIN, DensityFunctions.mul(
                DensityFunctions.constant(0.1),
                DensityFunctions.add(
                        DensityFunctions.constant(-1),
                        noiseInterpolator
                ).clamp(-1, 0)
        ).clamp(-0.1, 1));
    }

    @NotNull
    private static DensityFunction mulAddHalf(DensityFunction input) {
        // mulAddHalf(x) = x * 0.5 + 0.5
        // Useful for squeezing function range [-1,1] into [0,1]
        return DensityFunctions.add(
                DensityFunctions.constant(0.5),
                DensityFunctions.mul(
                        DensityFunctions.constant(0.5),
                        input
                )
        );
    }
}
