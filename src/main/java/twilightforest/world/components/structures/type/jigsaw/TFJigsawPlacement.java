package twilightforest.world.components.structures.type.jigsaw;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.logging.LogUtils;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;
import twilightforest.world.components.structures.util.ControlledSpawns;

import java.util.Deque;
import java.util.List;
import java.util.Optional;

public class TFJigsawPlacement {
	static final Logger LOGGER = LogUtils.getLogger();

	public static Optional<Structure.GenerationStub> addPieces(Structure.GenerationContext context, Holder<StructureTemplatePool> poolHolder, Optional<ResourceLocation> startPieceRL, int depth, BlockPos startPos, Optional<Heightmap.Types> types, int maxDist, ControlledSpawns.ControlledSpawningConfig config) {
		RegistryAccess registryaccess = context.registryAccess();
		ChunkGenerator chunkgenerator = context.chunkGenerator();
		StructureTemplateManager structuretemplatemanager = context.structureTemplateManager();
		LevelHeightAccessor levelheightaccessor = context.heightAccessor();
		WorldgenRandom worldgenrandom = context.random();
		Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registries.TEMPLATE_POOL);
		Rotation rotation = Rotation.getRandom(worldgenrandom);
		StructureTemplatePool structuretemplatepool = poolHolder.value();
		StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
		if (structurepoolelement == EmptyPoolElement.INSTANCE) {
			return Optional.empty();
		} else {
			BlockPos blockpos;
			if (startPieceRL.isPresent()) {
				ResourceLocation resourcelocation = startPieceRL.get();
				Optional<BlockPos> optional = getRandomNamedJigsaw(structurepoolelement, resourcelocation, startPos, rotation, structuretemplatemanager, worldgenrandom);
				if (optional.isEmpty()) {
					LOGGER.error("No starting jigsaw {} found in start pool {}", resourcelocation, poolHolder.unwrapKey().map((p_248484_) -> p_248484_.location().toString()).orElse("<unregistered>"));
					return Optional.empty();
				}

				blockpos = optional.get();
			} else {
				blockpos = startPos;
			}

			Vec3i vec3i = blockpos.subtract(startPos);
			BlockPos blockpos1 = startPos.subtract(vec3i);
			PoolElementStructurePiece poolelementstructurepiece;
			if (structurepoolelement instanceof IndexedStructurePoolElement spe) {
				poolelementstructurepiece = new IndexedPoolElementPiece(structuretemplatemanager, spe, blockpos1, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(structuretemplatemanager, blockpos1, rotation));
			} else {
			 poolelementstructurepiece = new PoolElementStructurePiece(structuretemplatemanager, structurepoolelement, blockpos1, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(structuretemplatemanager, blockpos1, rotation));
			}
			BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
			int i = (boundingbox.maxX() + boundingbox.minX()) / 2;
			int j = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
			int k;
			k = types.map(value -> startPos.getY() + chunkgenerator.getFirstFreeHeight(i, j, value, levelheightaccessor, context.randomState())).orElseGet(blockpos1::getY);

			int l = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
			poolelementstructurepiece.move(0, k - l, 0);
			int i1 = k + vec3i.getY();
			return Optional.of(new Structure.GenerationStub(new BlockPos(i, i1, j), (p_227237_) -> {
				List<PoolElementStructurePiece> list = Lists.newArrayList();
				list.add(poolelementstructurepiece);
				if (depth > 0) {
					AABB aabb = new AABB(i - maxDist, i1 - maxDist, j - maxDist, i + maxDist + 1, i1 + maxDist + 1, j + maxDist + 1);
					VoxelShape voxelshape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST);
					addPieces(context.randomState(), depth, chunkgenerator, structuretemplatemanager, levelheightaccessor, worldgenrandom, registry, poolelementstructurepiece, list, voxelshape);
					list.forEach(p_227237_::addPiece);
				}
			}));
		}
	}

	private static Optional<BlockPos> getRandomNamedJigsaw(StructurePoolElement element, ResourceLocation startingPiece, BlockPos pos, Rotation rotation, StructureTemplateManager templateManager, WorldgenRandom random) {
		List<StructureTemplate.StructureBlockInfo> list = element.getShuffledJigsawBlocks(templateManager, pos, rotation, random);
		Optional<BlockPos> optional = Optional.empty();

		for(StructureTemplate.StructureBlockInfo info : list) {
			ResourceLocation resourcelocation = ResourceLocation.tryParse(info.nbt().getString("name"));
			if (startingPiece.equals(resourcelocation)) {
				optional = Optional.of(info.pos());
				break;
			}
		}

		return optional;
	}

	private static void addPieces(RandomState state, int pMaxDepth, ChunkGenerator generator, StructureTemplateManager templateManager, LevelHeightAccessor accessor, RandomSource random, Registry<StructureTemplatePool> pools, PoolElementStructurePiece piece, List<PoolElementStructurePiece> pieces, VoxelShape shape) {
		TFJigsawPlacement.Placer placer = new TFJigsawPlacement.Placer(pools, pMaxDepth, generator, templateManager, pieces, random);
		placer.placing.addLast(new TFJigsawPlacement.PieceState(piece, new MutableObject<>(shape), 0));

		while(!placer.placing.isEmpty()) {
			TFJigsawPlacement.PieceState jigsawplacement$piecestate = placer.placing.removeFirst();
			placer.tryPlacingChildren(jigsawplacement$piecestate.piece, jigsawplacement$piecestate.free, jigsawplacement$piecestate.depth, accessor, state);
		}

	}

	record PieceState(PoolElementStructurePiece piece, MutableObject<VoxelShape> free, int depth) {
	}

	static final class Placer {
		private final Registry<StructureTemplatePool> pools;
		private final int maxDepth;
		private final ChunkGenerator chunkGenerator;
		private final StructureTemplateManager structureTemplateManager;
		private final List<? super PoolElementStructurePiece> pieces;
		private final RandomSource random;
		final Deque<TFJigsawPlacement.PieceState> placing = Queues.newArrayDeque();

		Placer(Registry<StructureTemplatePool> pools, int maxDepth, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, List<? super PoolElementStructurePiece> pieces, RandomSource random) {
			this.pools = pools;
			this.maxDepth = maxDepth;
			this.chunkGenerator = chunkGenerator;
			this.structureTemplateManager = structureTemplateManager;
			this.pieces = pieces;
			this.random = random;
		}

		void tryPlacingChildren(PoolElementStructurePiece piece, MutableObject<VoxelShape> free, int depth, LevelHeightAccessor accessor, RandomState state) {
			StructurePoolElement structurepoolelement = piece.getElement();
			BlockPos blockpos = piece.getPosition();
			Rotation rotation = piece.getRotation();
			StructureTemplatePool.Projection projection = structurepoolelement.getProjection();
			boolean flag = projection == StructureTemplatePool.Projection.RIGID;
			MutableObject<VoxelShape> mutableobject = new MutableObject<>();
			BoundingBox boundingbox = piece.getBoundingBox();
			int i = boundingbox.minY();

			label129:
			for(StructureTemplate.StructureBlockInfo info : structurepoolelement.getShuffledJigsawBlocks(this.structureTemplateManager, blockpos, rotation, this.random)) {
				Direction direction = JigsawBlock.getFrontFacing(info.state());
				BlockPos blockpos1 = info.pos();
				BlockPos blockpos2 = blockpos1.relative(direction);
				int j = blockpos1.getY() - i;
				int k = -1;
				ResourceKey<StructureTemplatePool> resourcekey = readPoolName(info);
				Optional<? extends Holder<StructureTemplatePool>> optional = this.pools.getHolder(resourcekey);
				if (optional.isEmpty()) {
					TFJigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", (Object)resourcekey.location());
				} else {
					Holder<StructureTemplatePool> holder = optional.get();
					if (holder.value().size() == 0 && !holder.is(Pools.EMPTY)) {
						TFJigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", (Object)resourcekey.location());
					} else {
						Holder<StructureTemplatePool> holder1 = holder.value().getFallback();
						if (holder1.value().size() == 0 && !holder1.is(Pools.EMPTY)) {
							TFJigsawPlacement.LOGGER.warn("Empty or non-existent fallback pool: {}", holder1.unwrapKey().map((p_255599_) -> p_255599_.location().toString()).orElse("<unregistered>"));
						} else {
							boolean flag1 = boundingbox.isInside(blockpos2);
							MutableObject<VoxelShape> mutableobject1;
							if (flag1) {
								mutableobject1 = mutableobject;
								if (mutableobject.getValue() == null) {
									mutableobject.setValue(Shapes.create(AABB.of(boundingbox)));
								}
							} else {
								mutableobject1 = free;
							}

							List<StructurePoolElement> list = Lists.newArrayList();
							if (depth != this.maxDepth) {
								list.addAll(holder.value().getShuffledTemplates(this.random));
							}

							list.addAll(holder1.value().getShuffledTemplates(this.random));

							for(StructurePoolElement structurepoolelement1 : list) {
								if (structurepoolelement1 == EmptyPoolElement.INSTANCE) {
									break;
								}

								for(Rotation rotation1 : Rotation.getShuffled(this.random)) {
									List<StructureTemplate.StructureBlockInfo> list1 = structurepoolelement1.getShuffledJigsawBlocks(this.structureTemplateManager, BlockPos.ZERO, rotation1, this.random);

									for(StructureTemplate.StructureBlockInfo info1 : list1) {
										if (JigsawBlock.canAttach(info, info1)) {
											BlockPos blockpos3 = info1.pos();
											BlockPos blockpos4 = blockpos2.subtract(blockpos3);
											BoundingBox boundingbox2 = structurepoolelement1.getBoundingBox(this.structureTemplateManager, blockpos4, rotation1);
											int i1 = boundingbox2.minY();
											StructureTemplatePool.Projection projection1 = structurepoolelement1.getProjection();
											boolean flag2 = projection1 == StructureTemplatePool.Projection.RIGID;
											int j1 = blockpos3.getY();
											int k1 = j - j1 + JigsawBlock.getFrontFacing(info.state()).getStepY();
											int l1;
											if (flag && flag2) {
												l1 = i + k1;
											} else {
												if (k == -1) {
													k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, accessor, state);
												}

												l1 = k - j1;
											}

											int i2 = l1 - i1;
											BoundingBox boundingbox3 = boundingbox2.moved(0, i2, 0);
											BlockPos blockpos5 = blockpos4.offset(0, i2, 0);

											if (!Shapes.joinIsNotEmpty(mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
												mutableobject1.setValue(Shapes.joinUnoptimized(mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3)), BooleanOp.ONLY_FIRST));
												int i3 = piece.getGroundLevelDelta();
												int k2;
												if (flag2) {
													k2 = i3 - k1;
												} else {
													k2 = structurepoolelement1.getGroundLevelDelta();
												}

												PoolElementStructurePiece poolelementstructurepiece;
												if (structurepoolelement1 instanceof IndexedStructurePoolElement spe) {
													poolelementstructurepiece = new IndexedPoolElementPiece(this.structureTemplateManager, spe, blockpos5, k2, rotation1, boundingbox3);
												} else {
													poolelementstructurepiece = new PoolElementStructurePiece(this.structureTemplateManager, structurepoolelement1, blockpos5, k2, rotation1, boundingbox3);
												}
												int l2;
												if (flag) {
													l2 = i + j;
												} else if (flag2) {
													l2 = l1 + j1;
												} else {
													if (k == -1) {
														k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, accessor, state);
													}

													l2 = k + k1 / 2;
												}

												piece.addJunction(new JigsawJunction(blockpos2.getX(), l2 - j + i3, blockpos2.getZ(), k1, projection1));
												poolelementstructurepiece.addJunction(new JigsawJunction(blockpos1.getX(), l2 - j1 + k2, blockpos1.getZ(), -k1, projection));
												this.pieces.add(poolelementstructurepiece);
												if (depth + 1 <= this.maxDepth) {
													this.placing.addLast(new TFJigsawPlacement.PieceState(poolelementstructurepiece, mutableobject1, depth + 1));
												}
												continue label129;
											}
										}
									}
								}
							}
						}
					}
				}
			}

		}

		private static ResourceKey<StructureTemplatePool> readPoolName(StructureTemplate.StructureBlockInfo info) {
			return ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(info.nbt().getString("pool")));
		}
	}
}
