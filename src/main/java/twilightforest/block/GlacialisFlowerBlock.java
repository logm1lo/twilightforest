package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFMobEffects;

public class GlacialisFlowerBlock extends FlowerBlock implements SnowLoggable {

	public GlacialisFlowerBlock(Properties properties) {
		super(TFMobEffects.FROSTY, 12, properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(SNOW_LAYERS, 0));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return Shapes.join(SHAPE, SNOW_SHAPE_BY_LAYER[state.getValue(SNOW_LAYERS)], BooleanOp.OR);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SNOW_SHAPE_BY_LAYER[Math.max(state.getValue(SNOW_LAYERS) - 1, 0)];
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(SNOW_LAYERS) > 0 && level.getBrightness(LightLayer.BLOCK, pos) > 11) {
			level.setBlockAndUpdate(pos, state.setValue(SNOW_LAYERS, 0));
		}
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
		return switch (type) {
			case LAND, AIR -> state.getValue(SNOW_LAYERS) < 5;
			case WATER -> false;
		};
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		if (!player.isSecondaryUseActive() && state.getValue(SNOW_LAYERS) > 0) {
			this.handleBreakingLogic(level, pos, state, player, null);
			return false;
		}
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}

	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
		return state.getValue(SNOW_LAYERS) > 0 && player.isSecondaryUseActive() ? Blocks.SNOW.defaultBlockState().getDestroyProgress(player, getter, pos) : super.getDestroyProgress(state, player, getter, pos);
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos pos) {
		return state.getValue(SNOW_LAYERS) == 8 ? 0.2F : 1.0F;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
		return state.is(Blocks.SNOW_BLOCK) || super.mayPlaceOn(state, getter, pos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(SNOW_LAYERS);
	}
}
