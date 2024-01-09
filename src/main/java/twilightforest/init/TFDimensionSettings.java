package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.layer.vanillalegacy.BiomeDensitySource;
import twilightforest.world.components.layer.vanillalegacy.BiomeTerrainWarpRouter;
import twilightforest.world.registration.surface_rules.TFSurfaceRules;

import java.util.List;
import java.util.OptionalLong;

public class TFDimensionSettings {

	public static long seed; //Minecraft Overworld seed - used for seed ASM

	public static final ResourceKey<NoiseGeneratorSettings> TWILIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TwilightForestMod.prefix("twilight_noise_gen"));
	public static final ResourceKey<NoiseGeneratorSettings> SKYLIGHT_NOISE_GEN = ResourceKey.create(Registries.NOISE_SETTINGS, TwilightForestMod.prefix("skylight_noise_gen"));

	public static final ResourceKey<DimensionType> TWILIGHT_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, TwilightForestMod.prefix("twilight_forest_type"));

	public static final ResourceKey<LevelStem> TWILIGHT_LEVEL_STEM =  ResourceKey.create(Registries.LEVEL_STEM, TwilightForestMod.prefix("twilight_forest"));

	private static DimensionType twilightDimType() {
		return new DimensionType(
				OptionalLong.of(13000L), //fixed time
				true, //skylight
				false, //ceiling
				false, //ultrawarm
				true, //natural
				0.125D, //coordinate scale
				true, //bed works
				true, //respawn anchor works
				-32, // Minimum Y Level
				32 + 256, // Height + Min Y = Max Y
				32 + 256, // Logical Height
				BlockTags.INFINIBURN_OVERWORLD, //infiburn
				TwilightForestMod.prefix("renderer"), // DimensionRenderInfo
				0f, // Wish this could be set to -0.05 since it'll make the world truly blacked out if an area is not sky-lit (see: Dark Forests) Sadly this also messes up night vision so it gets 0
				new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 7)
		);
	}

	public static NoiseGeneratorSettings tfDefault(BootstapContext<NoiseGeneratorSettings> context) {
		Holder.Reference<BiomeDensitySource> biomeGrid = context.lookup(TFRegistries.Keys.BIOME_TERRAIN_DATA).getOrThrow(BiomeLayerStack.BIOME_GRID);
		Holder.Reference<NormalNoise.NoiseParameters> surfaceParams = context.lookup(Registries.NOISE).getOrThrow(Noises.SURFACE);
		Holder.Reference<NormalNoise.NoiseParameters> ridgeParams = context.lookup(Registries.NOISE).getOrThrow(Noises.RIDGE);

		DensityFunction heightOffsetNoise = DensityFunctions.cache2d(DensityFunctions.mul(
				DensityFunctions.noise(surfaceParams, 1, 0),
				DensityFunctions.constant(2)
		));

        DensityFunction routedBiomeWarpInterpolated = DensityFunctions.mul(
				DensityFunctions.constant(1/32f),
				DensityFunctions.interpolated(new BiomeTerrainWarpRouter(
						biomeGrid,
						-64,
						64,
						8,
						DensityFunctions.constant(2.5),
						heightOffsetNoise
				))
		);

		// Debug: For a flat substitute of BiomeTerrainWarpRouter
		// routedBiomeWarpInterpolated = DensityFunctions.yClampedGradient(-31, 32, 2, -2);

		DensityFunction wideNoise = DensityFunctions.add(
				DensityFunctions.constant(0.2),
				DensityFunctions.mul(
						DensityFunctions.constant(0.4),
						DensityFunctions.noise(surfaceParams, 0.5, 0)
				)
		);

		DensityFunction thinNoise = DensityFunctions.add(
				DensityFunctions.constant(0.2),
				DensityFunctions.mul(
						DensityFunctions.constant(0.4),
						DensityFunctions.noise(surfaceParams, 3, 0)
				)
		);

		DensityFunction noiseInterpolator = DensityFunctions.add(
				DensityFunctions.constant(0.5),
				DensityFunctions.mul(
						DensityFunctions.constant(0.5),
						DensityFunctions.noise(ridgeParams, 1.5, 0)
				)
		);

		DensityFunction jitteredNoise = DensityFunctions.lerp(
				noiseInterpolator,
				wideNoise,
				thinNoise
		);

		DensityFunction noisedBiomeNoise = DensityFunctions.add(
				routedBiomeWarpInterpolated,
				DensityFunctions.cache2d(DensityFunctions.max(
						DensityFunctions.zero(),
						jitteredNoise
				))
		);

		DensityFunction finalDensity = DensityFunctions.add(
				noisedBiomeNoise,
				DensityFunctions.yClampedGradient(-32, -1, 0.5, 0).square()
		).clamp(-0.2, 1);

		NoiseSettings tfNoise = NoiseSettings.create(
				-32, //TODO Deliberate over this. For now it'll be -32
				256,
				4,
				4
		);

		return new NoiseGeneratorSettings(
				tfNoise,
				Blocks.STONE.defaultBlockState(),
				Blocks.WATER.defaultBlockState(),
				new NoiseRouter(
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						finalDensity,
						finalDensity,
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero()
				),
				TFSurfaceRules.tfSurface(),
				List.of(),
				0,
				false,
				false,
				false,
				false
		);
	}

	public static NoiseGeneratorSettings skylight() {
		NoiseSettings skylightNoise = NoiseSettings.create(
				-32, //min height
				256, // height
				4, // size_horizontal
				4 // size_vertical
		);

		// Problem island at /tp 9389.60 90.00 11041.66
		return new NoiseGeneratorSettings(
				// https://misode.github.io/worldgen/noise-settings/
				// So far this looks great! We just need to raise the island levels to sea level. Otherwise is generates flat-flakey islands that really show the roots on dirt bottoms from trees
				skylightNoise,
				Blocks.STONE.defaultBlockState(),
				Blocks.WATER.defaultBlockState(),
				new NoiseRouter(
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero(),
						DensityFunctions.zero()
				),
				TFSurfaceRules.tfSurface(),
				List.of(),
				0,
				false,
				false,
				false,
				false
		);
	}

	public static void bootstrapNoise(BootstapContext<NoiseGeneratorSettings> context) {
		context.register(TWILIGHT_NOISE_GEN, tfDefault(context));
		context.register(SKYLIGHT_NOISE_GEN, skylight());
	}

	public static void bootstrapType(BootstapContext<DimensionType> context) {
		context.register(TWILIGHT_DIM_TYPE, twilightDimType());
	}

	public static void bootstrapStem(BootstapContext<LevelStem> context) {
		HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

		HolderGetter<BiomeDensitySource> biomeDataRegistry = context.lookup(TFRegistries.Keys.BIOME_TERRAIN_DATA);

		NoiseBasedChunkGenerator twilightChunkGenerator = new NoiseBasedChunkGenerator(
				new TFBiomeProvider(biomeDataRegistry.getOrThrow(BiomeLayerStack.BIOME_GRID)),
				noiseGenSettings.getOrThrow(TFDimensionSettings.TWILIGHT_NOISE_GEN)
		);

		LevelStem stem = new LevelStem(
				dimTypes.getOrThrow(TFDimensionSettings.TWILIGHT_DIM_TYPE),
				twilightChunkGenerator
		);

		context.register(TWILIGHT_LEVEL_STEM, stem);
	}
}
