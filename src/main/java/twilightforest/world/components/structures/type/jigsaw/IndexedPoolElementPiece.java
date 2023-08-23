package twilightforest.world.components.structures.type.jigsaw;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.common.world.PieceBeardifierModifier;

public class IndexedPoolElementPiece extends PoolElementStructurePiece implements PieceBeardifierModifier {

	private final TerrainAdjustment adjustment;
	private final int spawnIndex;

	public IndexedPoolElementPiece(StructureTemplateManager manager, IndexedStructurePoolElement element, BlockPos pos, int groundLevelDelta, Rotation rotation, BoundingBox box) {
		super(manager, element, pos, groundLevelDelta, rotation, box);
		this.adjustment = element.adjustment;
		this.spawnIndex = element.spawnIndex;
	}

	public int getSpawnIndex() {
		return this.spawnIndex;
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.boundingBox;
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return this.adjustment;
	}
}
