package twilightforest.world.components.structures.type.jigsaw;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureTypes;
import twilightforest.world.components.structures.util.*;

import java.util.Optional;

public class ProgressionJigsawStructure extends ControlledSpawningStructure {

	public static final Codec<ProgressionJigsawStructure> CODEC = RecordCodecBuilder.<ProgressionJigsawStructure>mapCodec(instance ->
			instance.group(
					ControlledSpawns.ControlledSpawningConfig.FLAT_CODEC.forGetter(ControlledSpawningStructure::getConfig),
					AdvancementLockedStructure.AdvancementLockConfig.CODEC.fieldOf("advancements_required").forGetter(s -> s.advancementLockConfig),
					StructureHints.HintConfig.FLAT_CODEC.forGetter(s -> s.hintConfig),
					DecorationConfig.FLAT_CODEC.forGetter(s -> s.decorationConfig),
					Structure.settingsCodec(instance),
					StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(o -> o.startPool),
					ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((p_227654_) -> p_227654_.startJigsawName),
					Codec.intRange(0, 15).fieldOf("size").forGetter((p_227652_) -> p_227652_.maxDepth),
					HeightProvider.CODEC.fieldOf("start_height").forGetter((p_227649_) -> p_227649_.startHeight),
					Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter((p_227644_) -> p_227644_.projectStartToHeightmap),
					Codec.intRange(1, 256).fieldOf("max_distance_from_center").forGetter((p_227642_) -> p_227642_.maxDistanceFromCenter)
			).apply(instance, ProgressionJigsawStructure::new)).codec();

	private final Holder<StructureTemplatePool> startPool;
	private final Optional<ResourceLocation> startJigsawName;
	private final int maxDepth;
	private final HeightProvider startHeight;
	private final Optional<Heightmap.Types> projectStartToHeightmap;
	private final int maxDistanceFromCenter;

	public ProgressionJigsawStructure(ControlledSpawns.ControlledSpawningConfig controlledSpawningConfig, AdvancementLockedStructure.AdvancementLockConfig advancementLockConfig, StructureHints.HintConfig hintConfig, DecorationConfig decorationConfig, Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsaw, int maxDepth, HeightProvider startHeight, Optional<Heightmap.Types> type, int maxDistanceFromCenter) {
		super(controlledSpawningConfig, advancementLockConfig, hintConfig, decorationConfig, settings);
		this.startPool = startPool;
		this.startJigsawName = startJigsaw;
		this.maxDepth = maxDepth;
		this.startHeight = startHeight;
		this.projectStartToHeightmap = type;
		this.maxDistanceFromCenter = maxDistanceFromCenter;
	}

	@Override
	public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext pContext) {
		ChunkPos chunkpos = pContext.chunkPos();
		int i = this.startHeight.sample(pContext.random(), new WorldGenerationContext(pContext.chunkGenerator(), pContext.heightAccessor()));
		BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), i, chunkpos.getMinBlockZ());
		return TFJigsawPlacement.addPieces(pContext, this.startPool, this.startJigsawName, this.maxDepth, blockpos, this.projectStartToHeightmap, this.maxDistanceFromCenter, this.controlledSpawningConfig);
	}

	@Override
	protected @Nullable StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z) {
		return null;
	}

	@Override
	public StructureType<?> type() {
		return TFStructureTypes.PROGRESSION_JIGSAW.get();
	}
}
