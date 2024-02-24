package twilightforest.world.components.structures.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import twilightforest.init.TFStructurePlacementTypes;
import twilightforest.util.LegacyLandmarkPlacements;

import java.util.Optional;

public class AvoidLandmarkGridPlacement extends RandomSpreadStructurePlacement {

	public static final Codec<AvoidLandmarkGridPlacement> CODEC = ExtraCodecs.validate(RecordCodecBuilder.mapCodec(instance -> placementCodec(instance).and(instance.group(
							Codec.intRange(0, 4096).fieldOf("spacing").forGetter(AvoidLandmarkGridPlacement::spacing),
							Codec.intRange(0, 4096).fieldOf("separation").forGetter(AvoidLandmarkGridPlacement::separation),
							RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(AvoidLandmarkGridPlacement::spreadType))
					)
					.apply(instance, AvoidLandmarkGridPlacement::new)
	), AvoidLandmarkGridPlacement::validate).codec();

	private static DataResult<AvoidLandmarkGridPlacement> validate(AvoidLandmarkGridPlacement placement) {
		return placement.spacing() <= placement.separation() ? DataResult.error(() -> "Spacing has to be larger than separation") : DataResult.success(placement);
	}

	public AvoidLandmarkGridPlacement(int spacing, int separation, RandomSpreadType spreadType, int salt) {
		super(spacing, separation, spreadType, salt);
	}

	public AvoidLandmarkGridPlacement(Vec3i locateOffset, StructurePlacement.FrequencyReductionMethod reduction, float frequencyModifier, int salt, Optional<ExclusionZone> exclusionZone, int spacing, int separation, RandomSpreadType spreadType) {
		super(locateOffset, reduction, frequencyModifier, salt, exclusionZone, spacing, separation, spreadType);
	}

	@Override
	protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
		return super.isPlacementChunk(state, chunkX, chunkZ) && !LegacyLandmarkPlacements.chunkHasLandmarkCenter(chunkX, chunkZ);
	}

	@Override
	public StructurePlacementType<?> type() {
		return TFStructurePlacementTypes.AVOID_GRID_LANDMARK_PLACEMENT_TYPE.get();
	}
}
