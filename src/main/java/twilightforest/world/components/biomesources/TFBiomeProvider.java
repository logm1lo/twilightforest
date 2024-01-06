package twilightforest.world.components.biomesources;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import twilightforest.TFRegistries;
import twilightforest.world.components.chunkgenerators.warp.TerrainColumn;
import twilightforest.world.components.layer.vanillalegacy.BiomeTerrainData;

import java.util.Optional;
import java.util.stream.Stream;

public class TFBiomeProvider extends BiomeSource {
	public static final Codec<TFBiomeProvider> TF_CODEC = RegistryFileCodec.create(TFRegistries.Keys.BIOME_TERRAIN_DATA, BiomeTerrainData.CODEC, false).xmap(TFBiomeProvider::new, TFBiomeProvider::getBiomeConfig).fieldOf("terrain_info").codec();

	private final Holder<BiomeTerrainData> biomeTerrainDataHolder;

    public TFBiomeProvider(Holder<BiomeTerrainData> biomeTerrainDataHolder) {
		super();

		this.biomeTerrainDataHolder = biomeTerrainDataHolder;
    }

	public Optional<TerrainColumn> getTerrainColumn(int x, int z) {
		return this.biomeTerrainDataHolder.value().getTerrainColumn(x, z);
	}

	private Holder<BiomeTerrainData> getBiomeConfig() {
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

	@Deprecated // TODO Move to a DensityFunction
	public float getBaseOffset() {
		return this.biomeTerrainDataHolder.value().getBaseOffset();
	}

	@Deprecated // TODO Move to a DensityFunction
	public float getBaseFactor() {
		return this.biomeTerrainDataHolder.value().getBaseFactor();
	}

	@Deprecated // TODO Move to a DensityFunction
	public float getBiomeDepth(int x, int z) {
		return this.biomeTerrainDataHolder.value().getBiomeDepth(x, z);
	}
}
