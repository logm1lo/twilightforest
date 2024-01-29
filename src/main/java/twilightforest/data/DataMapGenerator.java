package twilightforest.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.concurrent.CompletableFuture;

public class DataMapGenerator extends DataMapProvider {
	public DataMapGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void gather() {
		var compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);
		compostables.add(TFBlocks.FALLEN_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0), false);
		compostables.add(TFBlocks.CANOPY_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.CLOVER_PATCH.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.DARK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.FIDDLEHEAD.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.HEDGE.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MANGROVE_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MAYAPPLE.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MINING_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TWILIGHT_OAK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.RAINBOW_OAK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.ROOT_STRAND.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.SORTING_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.THORN_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TIME_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TRANSFORMATION_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.TWILIGHT_OAK_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.CANOPY_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.MANGROVE_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.DARKWOOD_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFBlocks.RAINBOW_OAK_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.3F), false);
		compostables.add(TFItems.TORCHBERRIES, new Compostable(0.3F), false);
		compostables.add(TFBlocks.BEANSTALK_LEAVES.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.MOSS_PATCH.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.ROOT_BLOCK.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.THORN_ROSE.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TROLLVIDR.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.HOLLOW_OAK_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TIME_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TRANSFORMATION_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.MINING_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.SORTING_SAPLING.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFBlocks.TORCHBERRY_PLANT.asItem().builtInRegistryHolder(), new Compostable(0.5F), false);
		compostables.add(TFItems.LIVEROOT, new Compostable(0.5F), false);
		compostables.add(TFBlocks.HUGE_MUSHGLOOM_STEM.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.HUGE_WATER_LILY.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.LIVEROOT_BLOCK.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.MUSHGLOOM.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.UBEROUS_SOIL.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.HUGE_STALK.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.UNRIPE_TROLLBER.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFBlocks.TROLLBER.asItem().builtInRegistryHolder(), new Compostable(0.65F), false);
		compostables.add(TFItems.MAZE_WAFER, new Compostable(0.65F), false);
		compostables.add(TFBlocks.HUGE_LILY_PAD.asItem().builtInRegistryHolder(), new Compostable(0.85F), false);
		compostables.add(TFBlocks.HUGE_MUSHGLOOM.asItem().builtInRegistryHolder(), new Compostable(0.85F), false);
		compostables.add(TFItems.EXPERIMENT_115, new Compostable(0.85F), false);
		compostables.add(TFItems.MAGIC_BEANS, new Compostable(0.85F), false);
	}
}
