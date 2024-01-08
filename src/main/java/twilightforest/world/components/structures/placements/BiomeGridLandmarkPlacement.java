package twilightforest.world.components.structures.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import twilightforest.init.TFLandmark;
import twilightforest.init.TFStructurePlacementTypes;
import twilightforest.util.LegacyLandmarkPlacements;

import java.util.Optional;

public class BiomeGridLandmarkPlacement extends StructurePlacement {
    public static final Codec<BiomeGridLandmarkPlacement> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            TFLandmark.CODEC.fieldOf("landmark_set").forGetter(p -> p.landmark),
            Codec.BOOL.orElse(false).fieldOf("legacy_special_biome_bypass").withLifecycle(Lifecycle.deprecated(0)).forGetter(p -> p.legacySpecialBiomeBypass)
    ).apply(inst, BiomeGridLandmarkPlacement::new));

    private final TFLandmark landmark;
    @Deprecated
    private final boolean legacySpecialBiomeBypass;

    public static BiomeGridLandmarkPlacement forTag(TFLandmark landmark, boolean legacySpecialBiomeBypass) {
        return new BiomeGridLandmarkPlacement(landmark, legacySpecialBiomeBypass);
    }

    public BiomeGridLandmarkPlacement(TFLandmark landmark, boolean legacySpecialBiomeBypass) {
        super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1f, 0, Optional.empty()); // None of these params matter except for possibly flat-world or whatever

        this.landmark = landmark;
        this.legacySpecialBiomeBypass = legacySpecialBiomeBypass;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int chunkX, int chunkZ) {
        if (!LegacyLandmarkPlacements.chunkHasLandmarkCenter(chunkX, chunkZ))
            return false;

        // TODO Replace with using weighted list
        return this.legacySpecialBiomeBypass || LegacyLandmarkPlacements.pickVarietyLandmark(chunkX, chunkZ) == this.landmark;
    }

    @Override
    public StructurePlacementType<?> type() {
        return TFStructurePlacementTypes.GRID_LANDMARK_PLACEMENT_TYPE.get();
    }

    public TFLandmark getLandmark() {
        return this.landmark;
    }
}
