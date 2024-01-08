package twilightforest.world.components.biomesources;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import twilightforest.TFRegistries;
import twilightforest.world.components.layer.vanillalegacy.BiomeDensitySource;

import java.util.stream.Stream;

public class TFBiomeProvider extends BiomeSource {
	public static final Codec<TFBiomeProvider> TF_CODEC = RegistryFileCodec.create(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeDensitySource.CODEC, false).xmap(TFBiomeProvider::new, TFBiomeProvider::getBiomeConfig).fieldOf("terrain_data").codec();

	private final Holder<BiomeDensitySource> biomeTerrainDataHolder;

    public TFBiomeProvider(Holder<BiomeDensitySource> biomeTerrainDataHolder) {
		super();

		this.biomeTerrainDataHolder = biomeTerrainDataHolder;
    }

	private Holder<BiomeDensitySource> getBiomeConfig() {
		return this.biomeTerrainDataHolder;
	}

	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		return this.biomeTerrainDataHolder.value().collectPossibleBiomes();
	}

	@Override
	protected Codec<? extends BiomeSource> codec() {
		return TF_CODEC;
	}

	@Override
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
		return this.biomeTerrainDataHolder.value().getNoiseBiome(x, y, z);
	}

	@Deprecated
	public BiomeDensitySource getBiomeTerrain() {
		return this.biomeTerrainDataHolder.value();
	}
}
