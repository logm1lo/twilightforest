package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.monster.DeathTome;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.DirectionUtil;
import twilightforest.util.jigsaw.JigsawPlaceContext;
import twilightforest.util.jigsaw.JigsawRecord;
import twilightforest.util.jigsaw.JigsawUtil;
import twilightforest.world.components.structures.TwilightJigsawPiece;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public final class TowerRoom extends TwilightJigsawPiece implements PieceBeardifierModifier {
	private final int roomSize;
	private final boolean generateGround;
	private final int ladderIndex;
	private final String jigsawLadderTarget;
	private final int roofFallback;

	public TowerRoom(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
		super(TFStructurePieceTypes.TOWER_ROOM.get(), compoundTag, ctx, readSettings(compoundTag));

		TowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(TowerUtil.ROOM_SPAWNERS), true);

		this.roomSize = compoundTag.getInt("room_size");
		this.generateGround = compoundTag.getBoolean("gen_ground");
		this.ladderIndex = compoundTag.getInt("ladder_index");
		this.jigsawLadderTarget = this.shouldLadderUpwards() ? this.getSpareJigsaws().get(this.ladderIndex).target() : "";
		this.roofFallback = compoundTag.getInt("roof_index");
	}

	public TowerRoom(StructureTemplateManager structureManager, int genDepth, JigsawPlaceContext jigsawContext, ResourceLocation roomId, int roomSize, boolean generateGround, boolean canGenerateLadder) {
		super(TFStructurePieceTypes.TOWER_ROOM.get(), genDepth, structureManager, roomId, jigsawContext);

		TowerUtil.addDefaultProcessors(this.placeSettings.addProcessor(TowerUtil.ROOM_SPAWNERS), true);

		this.roomSize = roomSize;
		this.generateGround = generateGround;

		Set<String> ladderPlacements = canGenerateLadder ? TowerUtil.getLadderPlacementsForSize(this.roomSize) : Collections.emptySet();
		this.ladderIndex = canGenerateLadder ? this.pickFirstIndex(this.getSpareJigsaws(), ladderPlacements::contains) : -1;

		this.jigsawLadderTarget = this.shouldLadderUpwards() ? this.getSpareJigsaws().get(this.ladderIndex).target() : "";
		this.roofFallback = canGenerateLadder ? this.pickFirstIndex(this.getSpareJigsaws(), "twilightforest:lich_tower/roof"::equals) : -1;
	}

	private int pickFirstIndex(List<JigsawRecord> spareJigsaws, Predicate<String> filter) {
		for (int i = 0; i < spareJigsaws.size(); i++) {
			if (filter.test(spareJigsaws.get(i).target())) {
				return i;
			}
		}

		return -1;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putInt("room_size", this.roomSize);
		structureTag.putBoolean("gen_ground", this.generateGround);
		structureTag.putInt("ladder_index", this.ladderIndex);
		structureTag.putInt("roof_index", this.roofFallback);
	}

	@Override
	protected void processJigsaw(StructurePiece parent, StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, int jigsawIndex) {
		switch (connection.target()) {
			case "twilightforest:lich_tower/bridge" -> {
				boolean generateGround = this.generateGround && connection.pos().getY() < 5;
				if (this.roomSize < 1) {
					return;
				} else if (this.genDepth > 30 || random.nextInt(this.towerStackIndex() * 2 + 1) == 0) {
					TowerBridge.putCover(this, pieceAccessor, random, connection.pos(), connection.orientation(), this.structureManager, generateGround, this.genDepth + 1);
				} else {
					TowerBridge.tryRoomAndBridge(this, pieceAccessor, random, connection, this.structureManager, false, this.roomSize - random.nextInt(2), generateGround, this.genDepth + 1, false);
				}

				return;
			}
			case "twilightforest:lich_tower/roof" -> {
				if (!this.shouldLadderUpwards()) {
					this.putRoof(pieceAccessor, random, connection);
				}
				return;
			}
			case "twilightforest:lich_tower/beard" -> {
				if (this.generateGround || this.hasLadderBelowRoom()) {
					// Instead of placing a beard structure piece, this piece generates ground with the Beardifier!
					// Or there's a ladder entering the room from underneath
					return;
				}

				FrontAndTop orientationToMatch = getVerticalOrientation(connection, Direction.DOWN, this);

				for (ResourceLocation beardLocation : TowerUtil.shuffledBeards(random, this.roomSize)) {
					if (this.tryBeard(pieceAccessor, random, connection, beardLocation, orientationToMatch, false)) {
						return;
					}
				}

				ResourceLocation fallbackBeard = TowerUtil.getFallbackBeard(this.roomSize);
				this.tryBeard(pieceAccessor, random, connection, fallbackBeard, orientationToMatch, true);
			}
			case "twilightforest:lich_tower/decor" -> {
				TowerWingDecorPiece.addDecor(this, pieceAccessor, random, connection, this.genDepth + 1, this.structureManager, false);
			}
		}

		if (this.ladderIndex == jigsawIndex && this.jigsawLadderTarget.equals(connection.target())) {
			int ladderOffset = Integer.parseInt(this.jigsawLadderTarget.substring(this.jigsawLadderTarget.length() - 1));
			ResourceLocation roomId = TowerUtil.getRoomUpwards(random, this.roomSize, ladderOffset);
			if (roomId != null && (this.templateName.equals(roomId.toString()) || (parent instanceof TwilightTemplateStructurePiece twilightTemplate && twilightTemplate.getTemplateName().equals(roomId.toString())))) {
				// 1 chance at reroll if template is same as current or parent's
				roomId = TowerUtil.getRoomUpwards(random, this.roomSize, ladderOffset);
				// Otherwise if a repeat gets rolled -- how lucky!
			}
			BlockPos topPos = connection.pos().offset(0, this.boundingBox.getYSpan() - connection.pos().getY() - 1, 0);
			JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), topPos, connection.orientation(), this.structureManager, roomId, connection.target(), random);

			if (placeableJunction != null) {
				boolean canGenerateLadder = this.getSourceJigsaw().orientation().front().getAxis().isHorizontal() && random.nextBoolean();
				StructurePiece room = new TowerRoom(this.structureManager, this.genDepth + 1, placeableJunction, roomId, this.roomSize, false, canGenerateLadder);

				BoundingBox boundingBox = BoundingBoxUtils.cloneWithAdjustments(room.getBoundingBox(), 1, 0, 1, -1, 0, -1);
				if (pieceAccessor.findCollisionPiece(boundingBox) == null) {
					pieceAccessor.addPiece(room);
					room.addChildren(parent, pieceAccessor, random);

					return;
				}
			}

			Direction front = connection.orientation().front();
			if (front != Direction.UP) {
				TwilightForestMod.LOGGER.error("Jigsaw was facing {} inside of {}", front, this.templateName);
			}

			if (this.roofFallback >= 0) {
				//TwilightForestMod.LOGGER.warn("\nGenerating roof instead of {} over {}, for target {} at {}", roomId, this.templateName, connection.target(), this.templatePosition);
				//TwilightForestMod.LOGGER.warn("Connection priority was {} and index {}. Source priority was {}", connection.priority(), jigsawIndex, this.getSourceJigsaw().priority());
				//TwilightForestMod.LOGGER.warn("\nJigsaws in piece: {}", this.structureManager.getOrCreate(roomId).filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), Blocks.JIGSAW));

				// If the room above cannot generate, then place the roof instead
				this.putRoof(pieceAccessor, random, this.getSpareJigsaws().get(this.roofFallback));
			}
		}
	}

	private int towerStackIndex() {
		boolean hasRoomAbove = this.shouldLadderUpwards();
		boolean hasRoomBelow = this.hasLadderBelowRoom();
		return hasRoomAbove && !hasRoomBelow ? 0 : hasRoomAbove ? 1 : 2;
	}

	private boolean putRoof(StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection) {
		FrontAndTop orientationToMatch = getVerticalOrientation(connection, Direction.UP, this);
		BoundingBox roofExtension = BoundingBoxUtils.extrusionFrom(this.boundingBox.minX(), this.boundingBox.maxY() + 1, this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY() + 1, this.boundingBox.maxZ(), orientationToMatch.top().getOpposite(), 1);
		boolean doSideAttachment = connection.orientation().front().getAxis().isHorizontal() && pieceAccessor.findCollisionPiece(roofExtension) != null;

		for (ResourceLocation roofLocation : TowerUtil.shuffledRoofs(random, this.roomSize, doSideAttachment)) {
			if (tryRoof(pieceAccessor, random, connection, roofLocation, orientationToMatch, false, this, this.genDepth + 1, this.structureManager)) {
				return true;
			}
		}

		ResourceLocation fallbackRoof = TowerUtil.getFallbackRoof(this.roomSize, doSideAttachment);
		tryRoof(pieceAccessor, random, connection, fallbackRoof, orientationToMatch, true, this, this.genDepth + 1, this.structureManager);
		return false;
	}

	@NotNull
	public static FrontAndTop getVerticalOrientation(JigsawRecord connection, Direction vertical, TwilightJigsawPiece towerRoom) {
		JigsawRecord sourceJigsaw = towerRoom.getSourceJigsaw();
		Direction sourceDirection = JigsawUtil.getAbsoluteHorizontal(sourceJigsaw != null ? sourceJigsaw.orientation() : connection.orientation());

		return FrontAndTop.fromFrontAndTop(vertical, sourceDirection.getOpposite());
	}

	public static boolean tryRoof(StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, @Nullable ResourceLocation roofLocation, FrontAndTop orientationToMatch, boolean allowClipping, TwilightJigsawPiece parent, int newDepth, StructureTemplateManager structureManager) {
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(parent.templatePosition(), connection.pos(), orientationToMatch, structureManager, roofLocation, "twilightforest:lich_tower/roof", random);

		if (placeableJunction != null) {
			TowerRoof roofPiece = new TowerRoof(newDepth, structureManager, roofLocation, placeableJunction);

			if (allowClipping || pieceAccessor.findCollisionPiece(roofPiece.generationCollisionBox()) == null) {
				pieceAccessor.addPiece(roofPiece);
				roofPiece.addChildren(parent, pieceAccessor, random);

				return true;
			}
		}
		return false;
	}

	private boolean tryBeard(StructurePieceAccessor pieceAccessor, RandomSource random, JigsawRecord connection, @Nullable ResourceLocation beardLocation, FrontAndTop orientationToMatch, boolean allowClipping) {
		JigsawPlaceContext placeableJunction = JigsawPlaceContext.pickPlaceableJunction(this.templatePosition(), connection.pos(), orientationToMatch, this.structureManager, beardLocation, "twilightforest:lich_tower/beard", random);

		if (placeableJunction != null) {
			TowerBeard beardPiece = new TowerBeard(this.genDepth + 1, this.structureManager, beardLocation, placeableJunction);

			if (allowClipping || pieceAccessor.findCollisionPiece(beardPiece.generationCollisionBox()) == null) {
				pieceAccessor.addPiece(beardPiece);
				beardPiece.addChildren(this, pieceAccessor, random);

				return true;
			}
		}
		return false;
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureCenterPos) {
		super.postProcess(level, structureManager, chunkGen, random, chunkBounds, chunkPos, structureCenterPos);

		JigsawRecord sourceJigsaw = this.getSourceJigsaw();
		if (this.hasLadderBelowRoom()) {
			BlockPos placeAt = this.templatePosition.offset(sourceJigsaw.pos());
			if (chunkBounds.isInside(placeAt)) {
				BlockState ladderBlock = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, sourceJigsaw.orientation().top());
				level.setBlock(placeAt, ladderBlock, 3);
				level.setBlock(placeAt.above(), ladderBlock, 3);
				BlockState airBlock = Blocks.AIR.defaultBlockState();
				for (BlockPos pos : BlockPos.betweenClosed(placeAt.above(2), placeAt.above(5))) {
					level.setBlock(pos, airBlock, 3);
				}
			}
		}

		if (this.shouldLadderUpwards()) {
			JigsawRecord ladderJigsaw = this.getSpareJigsaws().get(this.ladderIndex);
			BlockPos ladderOffset = ladderJigsaw.pos();
			BlockPos startPos = this.templatePosition.offset(ladderOffset);
			if (chunkBounds.isInside(startPos)) {
				BlockPos endPos = startPos.above(this.boundingBox.getYSpan() - ladderOffset.getY() - 1);
				BlockState ladderBlock = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, ladderJigsaw.orientation().top());
				for (BlockPos placeAt : BlockPos.betweenClosed(startPos, endPos)) {
					level.setBlock(placeAt, ladderBlock, 3);
				}
			}
		}
	}

	private boolean hasLadderBelowRoom() {
		JigsawRecord sourceJigsaw = this.getSourceJigsaw();
		return sourceJigsaw != null && sourceJigsaw.orientation().front() == Direction.DOWN;
	}

	private boolean shouldLadderUpwards() {
		return this.ladderIndex >= 0;
	}

	@Override
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen) {
		String[] splitLabel = label.split(":");
		if (splitLabel.length == 2) {
			Direction direction = DirectionUtil.fromStringOrElse(splitLabel[1], Direction.NORTH);

			switch (splitLabel[0]) {
				case "lectern" -> {
					level.removeBlock(pos, false); // Clears block entity data left by Data Marker

					boolean putMimic = random.nextBoolean();
					Rotation rotation = this.placeSettings.getRotation();
					BlockState lectern = Blocks.LECTERN.defaultBlockState()
						.setValue(HorizontalDirectionalBlock.FACING, direction)
						.setValue(LecternBlock.HAS_BOOK, !putMimic)
						.rotate(rotation);

					level.setBlock(pos, lectern, 3);

					if (putMimic) {
						DeathTome tomeMimic = TFEntities.DEATH_TOME.value().create(level.getLevel());
						if (tomeMimic != null) {
							tomeMimic.setPersistenceRequired();
							tomeMimic.moveTo(pos, lectern.getValue(HorizontalDirectionalBlock.FACING).toYRot(), 0);
							tomeMimic.setOnLectern(true);
							tomeMimic.finalizeSpawn(level, level.getCurrentDifficultyAt(tomeMimic.blockPosition()), MobSpawnType.STRUCTURE, null);
							level.addFreshEntityWithPassengers(tomeMimic);
						}
					} else if (level.getBlockEntity(pos) instanceof LecternBlockEntity lecternBlockEntity) {
						lecternBlockEntity.setBook(new ItemStack(Items.WRITABLE_BOOK));
					}
				}
			}
		}
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return this.generateGround ? TerrainAdjustment.BEARD_THIN : TerrainAdjustment.NONE;
	}

	@Override
	public int getGroundLevelDelta() {
		return this.roomSize == 0 ? 2 : 1;
	}
}
