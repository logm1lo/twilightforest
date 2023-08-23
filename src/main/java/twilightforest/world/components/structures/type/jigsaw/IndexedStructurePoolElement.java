package twilightforest.world.components.structures.type.jigsaw;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import twilightforest.init.TFStructureElementTypes;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class IndexedStructurePoolElement extends StructurePoolElement {
	private static final Codec<Either<ResourceLocation, StructureTemplate>> TEMPLATE_CODEC = Codec.of(IndexedStructurePoolElement::encodeTemplate, ResourceLocation.CODEC.map(Either::left));
	public static final Codec<IndexedStructurePoolElement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					TEMPLATE_CODEC.fieldOf("location").forGetter(o -> o.template),
					StructureProcessorType.LIST_CODEC.fieldOf("processors").forGetter(o -> o.processors),
					TerrainAdjustment.CODEC.fieldOf("adjustment").forGetter(o -> o.adjustment),
					Codec.INT.fieldOf("spawnIndex").forGetter(o -> o.spawnIndex))
			.apply(instance, IndexedStructurePoolElement::new));

	protected final Either<ResourceLocation, StructureTemplate> template;
	protected final Holder<StructureProcessorList> processors;
	protected final TerrainAdjustment adjustment;
	protected final int spawnIndex;

	private static <T> DataResult<T> encodeTemplate(Either<ResourceLocation, StructureTemplate> p_210425_, DynamicOps<T> p_210426_, T p_210427_) {
		Optional<ResourceLocation> optional = p_210425_.left();
		return optional.isEmpty() ? DataResult.error(() -> "Can not serialize a runtime pool element") : ResourceLocation.CODEC.encode(optional.get(), p_210426_, p_210427_);
	}

	protected IndexedStructurePoolElement(Either<ResourceLocation, StructureTemplate> template, Holder<StructureProcessorList> processors, TerrainAdjustment adjustment, int spawnIndex) {
		super(StructureTemplatePool.Projection.RIGID);
		this.template = template;
		this.processors = processors;
		this.adjustment = adjustment;
		this.spawnIndex = spawnIndex;
	}

	@Override
	public Vec3i getSize(StructureTemplateManager templateManager, Rotation rotation) {
		StructureTemplate structuretemplate = this.getTemplate(templateManager);
		return structuretemplate.getSize(rotation);
	}

	private StructureTemplate getTemplate(StructureTemplateManager templateManager) {
		return this.template.map(templateManager::getOrCreate, Function.identity());
	}

	public List<StructureTemplate.StructureBlockInfo> getDataMarkers(StructureTemplateManager templateManager, BlockPos pos, Rotation rotation, boolean useRelativePos) {
		StructureTemplate structuretemplate = this.getTemplate(templateManager);
		List<StructureTemplate.StructureBlockInfo> list = structuretemplate.filterBlocks(pos, (new StructurePlaceSettings()).setRotation(rotation), Blocks.STRUCTURE_BLOCK, useRelativePos);
		List<StructureTemplate.StructureBlockInfo> list1 = Lists.newArrayList();

		for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : list) {
			CompoundTag compoundtag = structuretemplate$structureblockinfo.nbt();
			if (compoundtag != null) {
				StructureMode structuremode = StructureMode.valueOf(compoundtag.getString("mode"));
				if (structuremode == StructureMode.DATA) {
					list1.add(structuretemplate$structureblockinfo);
				}
			}
		}

		return list1;
	}

	@Override
	public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager templateManager, BlockPos pos, Rotation rotation, RandomSource random) {
		StructureTemplate structuretemplate = this.getTemplate(templateManager);
		ObjectArrayList<StructureTemplate.StructureBlockInfo> objectarraylist = structuretemplate.filterBlocks(pos, (new StructurePlaceSettings()).setRotation(rotation), Blocks.JIGSAW, true);
		Util.shuffle(objectarraylist, random);
		return objectarraylist;
	}

	@Override
	public BoundingBox getBoundingBox(StructureTemplateManager templateManager, BlockPos pos, Rotation rotation) {
		StructureTemplate structuretemplate = this.getTemplate(templateManager);
		return structuretemplate.getBoundingBox((new StructurePlaceSettings()).setRotation(rotation), pos);
	}

	@Override
	public boolean place(StructureTemplateManager templateManager, WorldGenLevel level, StructureManager manager, ChunkGenerator generator, BlockPos pos, BlockPos newPos, Rotation rotation, BoundingBox box, RandomSource random, boolean keepJigsaws) {
		StructureTemplate structuretemplate = this.getTemplate(templateManager);
		StructurePlaceSettings structureplacesettings = this.getSettings(rotation, box, keepJigsaws);
		if (!structuretemplate.placeInWorld(level, pos, newPos, structureplacesettings, random, 18)) {
			return false;
		} else {
			for (StructureTemplate.StructureBlockInfo info : StructureTemplate.processBlockInfos(level, pos, newPos, structureplacesettings, this.getDataMarkers(templateManager, pos, rotation, false), this.getTemplate(templateManager))) {
				this.handleDataMarker(level, info, pos, rotation, random, box);
			}

			return true;
		}
	}

	//TODO
	@Override
	public StructurePoolElementType<?> getType() {
		return TFStructureElementTypes.INDEXED_ELEMENT.get();
	}

	//TODO
	@Override
	public void handleDataMarker(LevelAccessor level, StructureTemplate.StructureBlockInfo info, BlockPos pos, Rotation rotation, RandomSource random, BoundingBox box) {
		
	}

	protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox box, boolean keepJigsaws) {
		StructurePlaceSettings structureplacesettings = new StructurePlaceSettings();
		structureplacesettings.setBoundingBox(box);
		structureplacesettings.setRotation(rotation);
		structureplacesettings.setKnownShape(true);
		structureplacesettings.setIgnoreEntities(false);
		structureplacesettings.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
		structureplacesettings.setFinalizeEntities(true);
		if (!keepJigsaws) {
			structureplacesettings.addProcessor(JigsawReplacementProcessor.INSTANCE);
		}

		this.processors.value().list().forEach(structureplacesettings::addProcessor);
		this.getProjection().getProcessors().forEach(structureplacesettings::addProcessor);
		return structureplacesettings;
	}

	public static Function<StructureTemplatePool.Projection, IndexedStructurePoolElement> singleSpawnIndexed(String name, int spawnIndex) {
		return projection -> new IndexedStructurePoolElement(Either.left(new ResourceLocation(name)), Holder.direct(new StructureProcessorList(List.of())), TerrainAdjustment.NONE, spawnIndex);
	}

	public static Function<StructureTemplatePool.Projection, IndexedStructurePoolElement> singleAdjusted(String name, TerrainAdjustment adjustment, int spawnIndex) {
		return projection -> new IndexedStructurePoolElement(Either.left(new ResourceLocation(name)), Holder.direct(new StructureProcessorList(List.of())), adjustment, spawnIndex);
	}
}
