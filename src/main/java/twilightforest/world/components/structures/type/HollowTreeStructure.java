package twilightforest.world.components.structures.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.BiomeTagGenerator;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.HollowTreeComponent;
import twilightforest.world.components.structures.util.DecorationClearance;
import twilightforest.world.components.structures.util.LandmarkStructure;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HollowTreeStructure extends LandmarkStructure {
	public static final Codec<HollowTreeStructure> CODEC = RecordCodecBuilder.create(instance -> landmarkCodec(instance).apply(instance, HollowTreeStructure::new));


	public HollowTreeStructure(DecorationConfig decorationConfig, StructureSettings settings) {
		super(decorationConfig, settings);
	}

	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return new HollowTreeComponent(0, x, y, z);
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.HOLLOW_TREE.get();
	}

	public static HollowTreeStructure buildStructureConfig(BootstapContext<Structure> context) {
		return new HollowTreeStructure(
				new DecorationClearance.DecorationConfig(2, false, true, false),
				new Structure.StructureSettings(
						context.lookup(Registries.BIOME).getOrThrow(BiomeTagGenerator.VALID_HOLLOW_TREE_BIOMES),
						Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, category -> new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create()))), // Landmarks have Controlled Mob spawning
						GenerationStep.Decoration.SURFACE_STRUCTURES,
						TerrainAdjustment.NONE
				)
		);
	}
}
