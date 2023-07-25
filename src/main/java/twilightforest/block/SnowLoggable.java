package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public interface SnowLoggable {

	IntegerProperty SNOW_LAYERS = IntegerProperty.create("layers", 0, 8);
	VoxelShape[] SNOW_SHAPE_BY_LAYER = new VoxelShape[]{
			Shapes.empty(),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	default void handleBreakingLogic(Level level, BlockPos pos, BlockState state, Player player, @Nullable BlockState blockToConvertTo) {
		BlockHitResult rayTraceResult = this.clip(player);
		Vec3 hitVec = rayTraceResult.getType() == BlockHitResult.Type.BLOCK ? rayTraceResult.getLocation() : null;
		if (hitVec != null) {
			hitVec = hitVec.add(-pos.getX(), -pos.getY(), -pos.getZ());
		}
		double snowLevel = SNOW_SHAPE_BY_LAYER[state.getValue(SNOW_LAYERS)].bounds().maxY;
		if (hitVec != null && hitVec.y() <= snowLevel) {
			level.levelEvent(player, 2001, pos, Block.getId(Blocks.SNOW.defaultBlockState()));
			level.setBlockAndUpdate(pos, blockToConvertTo != null ? blockToConvertTo : state.setValue(SNOW_LAYERS, 0));
		} else {
			level.levelEvent(player, 2001, pos, Block.getId(state));
			Block.dropResources(state, level, pos, null, player, player.getMainHandItem());
			level.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, state.getValue(SNOW_LAYERS)));

		}
	}

	default BlockHitResult clip(Player entity) {
		Vec3 startPos = new Vec3(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
		Vec3 endPos = startPos.add(entity.getLookAngle().x() * 6.0D, entity.getLookAngle().y() * 6.0D, entity.getLookAngle().z() * (double) 6);
		ClipContext rayTraceContext = new ClipContext(startPos, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity);
		return entity.level().clip(rayTraceContext);
	}
}
