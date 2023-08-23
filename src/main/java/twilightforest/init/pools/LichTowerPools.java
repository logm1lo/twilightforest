package twilightforest.init.pools;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import twilightforest.world.components.structures.type.jigsaw.IndexedStructurePoolElement;

public class LichTowerPools {

	public static final ResourceKey<StructureTemplatePool> VESTIBULE = Pools.createKey("twilightforest:lich_tower/vestibule");

	//TODO create processors for cracking stone bricks and turning stone to cobble
	public static void bootstrap(BootstapContext<StructureTemplatePool> context) {
		//pool format: fallback, pairs of pieces (use TFStructurePoolElement.singleSpawnIndexed, make sure to add the modid and processor if necessary) and weights, projection (rigid is what we're using here)
		Holder<StructureTemplatePool> emptyFallback = context.lookup(Registries.TEMPLATE_POOL).getOrThrow(Pools.EMPTY);
		context.register(VESTIBULE, new StructureTemplatePool(emptyFallback, ImmutableList.of(Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/vestibule", -1), 1)), StructureTemplatePool.Projection.RIGID));
		context.register(Pools.createKey("twilightforest:lich_tower/main_tower"), new StructureTemplatePool(emptyFallback, ImmutableList.of(Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/main_tower", 0), 1)), StructureTemplatePool.Projection.RIGID));
		context.register(Pools.createKey("twilightforest:lich_tower/boss_room"), new StructureTemplatePool(emptyFallback, ImmutableList.of(Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/boss_room", -1), 1)), StructureTemplatePool.Projection.RIGID));
		//spawners - all weighted equally
		context.register(Pools.createKey("twilightforest:lich_tower/spawners"), new StructureTemplatePool(emptyFallback, ImmutableList.of(
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/spawners/bridge", 0), 1),
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/spawners/stairs", 0), 1),
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/spawners/none", 0), 1)), StructureTemplatePool.Projection.RIGID));
		//doorways - actual doorways have the lowest weight to prevent overpopulation of side towers
		context.register(Pools.createKey("twilightforest:lich_tower/doorways"), new StructureTemplatePool(emptyFallback, ImmutableList.of(
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/doorways/bridge", 0), 1),
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/doorways/wall", 0), 3),
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/doorways/window1", 0), 2),
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/doorways/window2", 0), 2),
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/doorways/window3", 0), 2)), StructureTemplatePool.Projection.RIGID));

		context.register(Pools.createKey("twilightforest:lich_tower/doorways_lowered"), new StructureTemplatePool(emptyFallback, ImmutableList.of(
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/doorways/lowered_bridge", 0), 1),
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/doorways/lowered_wall", 0), 3),
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/doorways/lowered_window1", 0), 2),
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/doorways/lowered_window2", 0), 2),
				Pair.of(IndexedStructurePoolElement.singleSpawnIndexed("twilightforest:lich_tower/doorways/lowered_window3", 0), 2)), StructureTemplatePool.Projection.RIGID));

	}
}
