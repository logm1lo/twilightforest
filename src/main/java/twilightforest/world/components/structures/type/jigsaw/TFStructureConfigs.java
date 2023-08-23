package twilightforest.world.components.structures.type.jigsaw;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.init.pools.LichTowerPools;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ControlledSpawns;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.StructureHints;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TFStructureConfigs {

    public static ProgressionJigsawStructure buildLichTowerConfig(BootstapContext<Structure> context) {
        return new ProgressionJigsawStructure(
                ControlledSpawns.ControlledSpawningConfig.firstIndexMonsters(
                        new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 10, 1, 2),
                        new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 1, 1, 1),
                        new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 1, 2),
                        new MobSpawnSettings.SpawnerData(TFEntities.DEATH_TOME.get(), 10, 2, 3),
                        new MobSpawnSettings.SpawnerData(EntityType.WITCH, 1, 1, 1)
                ),
                new AdvancementLockedStructure.AdvancementLockConfig(List.of(TwilightForestMod.prefix("progress_naga"))),
                new StructureHints.HintConfig(StructureHints.HintConfig.book("lichtower", 4), TFEntities.KOBOLD.get()),
                new DecorationClearance.DecorationConfig(1, false, true, true),
                new Structure.StructureSettings(
                        context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_LICH_TOWER_BIOMES),
                        Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN
                ),
                context.lookup(Registries.TEMPLATE_POOL).getOrThrow(LichTowerPools.VESTIBULE),
                Optional.empty(),
                7,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                200
        );
    }
}
