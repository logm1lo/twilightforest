package twilightforest.world.components.layer.vanillalegacy;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import twilightforest.init.custom.BiomeLayerStack;
import twilightforest.world.components.chunkgenerators.warp.TerrainColumn;
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

public class BiomeTerrainData {
    public static final Codec<BiomeTerrainData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            TerrainColumn.CODEC.listOf().fieldOf("biome_landscape").xmap(l -> l.stream().collect(Collectors.toMap(TerrainColumn::getResourceKey, Function.identity())), m -> m.values().stream().sorted(Comparator.comparing(TerrainColumn::getResourceKey)).toList()).forGetter(o -> o.biomeList),
            Codec.floatRange(TerrainColumn.MINIMUM_DEPTH, TerrainColumn.MAXIMUM_DEPTH).fieldOf("base_offset").forGetter(o -> o.baseOffset),
            Codec.floatRange(TerrainColumn.MINIMUM_SCALE, TerrainColumn.MAXIMUM_SCALE).fieldOf("base_factor").forGetter(o -> o.baseFactor),
            BiomeLayerStack.HOLDER_CODEC.fieldOf("biome_layer_config").forGetter(BiomeTerrainData::getBiomeConfig)
    ).apply(instance, instance.stable(BiomeTerrainData::new)));

    private final Map<ResourceKey<Biome>, TerrainColumn> biomeList;
    private final float baseOffset;
    private final float baseFactor;

    private final Holder<BiomeLayerFactory> genBiomeConfig;
    private final Supplier<LazyArea> genBiomes;

    public BiomeTerrainData(List<TerrainColumn> list, float offset, float factor, Holder<BiomeLayerFactory> biomeLayerFactory) {
        this(list.stream().collect(Collectors.toMap(TerrainColumn::getResourceKey, Function.identity())), offset, factor, biomeLayerFactory);
    }

    public BiomeTerrainData(Map<ResourceKey<Biome>, TerrainColumn> list, float offset, float factor, Holder<BiomeLayerFactory> biomeLayerFactory) {
        super();

        //this.genBiomes = buildLayers((salt) -> new LazyAreaContext(25, salt));
        this.genBiomeConfig = biomeLayerFactory;
        this.genBiomes = Suppliers.memoize(() -> this.genBiomeConfig.value().build(salt -> new LazyAreaContext(25, salt)));

        this.baseOffset = offset;
        this.baseFactor = factor;

        this.biomeList = list;
    }

    private Holder<BiomeLayerFactory> getBiomeConfig() {
        return this.genBiomeConfig;
    }

    public Holder<Biome> getNoiseBiome(int x, int y, int z) {
        return this.biomeList.get(this.genBiomes.get().getBiome(x, z)).getBiome(y);
    }

    public float getBaseOffset() {
        return this.baseOffset;
    }

    public float getBaseFactor() {
        return this.baseFactor;
    }

    public float getBiomeDepth(int x, int z) {
        return this.getBiomeDepth(this.genBiomes.get().getBiome(x, z));
    }

    public float getBiomeDepth(ResourceKey<Biome> biome) {
        return this.getBiomeValue(biome, TerrainColumn::depth, 0f);
    }

    public Optional<TerrainColumn> getTerrainColumn(int x, int z) {
        return this.getTerrainColumn(this.genBiomes.get().getBiome(x, z));
    }

    public Optional<TerrainColumn> getTerrainColumn(ResourceKey<Biome> biome) {
        return this.biomeList.values().stream().filter(p -> p.is(biome)).findFirst();
    }

    public <T> T getBiomeValue(ResourceKey<Biome> biome, Function<TerrainColumn, T> function, T other) {
        return this.getTerrainColumn(biome).map(function).orElse(other);
    }

    public Stream<Holder<Biome>> collectPossibleBiomes() {
        return this.biomeList.values().stream().flatMap(TerrainColumn::getBiomes);
    }

    public LazyArea build() {
        return this.genBiomes.get();
    }
}
