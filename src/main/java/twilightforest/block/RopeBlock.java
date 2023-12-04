package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RopeBlock extends Block implements SimpleWaterloggedBlock {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty X = BooleanProperty.create("x");
    public static final BooleanProperty Y = BooleanProperty.create("y");
    public static final BooleanProperty Z = BooleanProperty.create("z");

    protected static final VoxelShape BASE_SHAPE = Block.box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D);

    protected static final VoxelShape WEST_SHAPE = Block.box(0.0D, 7.0D, 7.0D, 7.0D, 9.0D, 9.0D);
    protected static final VoxelShape EAST_SHAPE = Block.box(9.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D);
    protected static final VoxelShape DOWN_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 7.0D, 9.0D);
    protected static final VoxelShape UP_SHAPE = Block.box(7.0D, 9.0D, 7.0D, 9.0D, 16.0D, 9.0D);
    protected static final VoxelShape NORTH_SHAPE = Block.box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 7.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(7.0D, 7.0D, 9.0D, 9.0D, 9.0D, 16.0D);

    public RopeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(WATERLOGGED, false)
                .setValue(Y, true)
                .setValue(X, false)
                .setValue(Z, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, Y, X, Z);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_261479_, BlockGetter p_261942_, BlockPos p_261844_) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isPathfindable(BlockState p_262166_, BlockGetter p_262143_, BlockPos p_261968_, PathComputationType p_261513_) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        VoxelShape shape = BASE_SHAPE;

        boolean y = state.getValue(Y);
        boolean x = state.getValue(X);
        boolean z = state.getValue(Z);

        if (y) {
            shape = Shapes.or(shape, DOWN_SHAPE);
            shape = Shapes.or(shape, UP_SHAPE);
        }
        if (z) {
            shape = Shapes.or(shape, NORTH_SHAPE);
            shape = Shapes.or(shape, SOUTH_SHAPE);
        }
        if (x) {
            shape = Shapes.or(shape, WEST_SHAPE);
            shape = Shapes.or(shape, EAST_SHAPE);
        }

        return shape;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return context.getItemInHand().is(this.asItem());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        Direction direction = context.getClickedFace();
        return this.defaultBlockState()
                .setValue(WATERLOGGED, level.getFluidState(blockpos).getType() == Fluids.WATER)
                .setValue(X, direction.getAxis() == Direction.Axis.X)
                .setValue(Y, direction.getAxis() == Direction.Axis.Y)
                .setValue(Z, direction.getAxis() == Direction.Axis.Z);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState otherState, boolean isMoving) {
        if (!level.isClientSide) level.scheduleTick(pos, this, 1);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos) {
        if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        if (!level.isClientSide()) level.scheduleTick(pos, this, 1);
        return state;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean flag = true;
        BlockState newState = state;
        if (state.getValue(X)) {
            if (!this.checkConnection(level, pos, Direction.WEST) && !this.checkConnection(level, pos, Direction.EAST)) newState = newState.setValue(X, false);
            else flag = false;
        }
        if (state.getValue(Y)) {
            if (!this.checkConnection(level, pos, Direction.UP)) newState = newState.setValue(Y, false);
            else flag = false;
        }
        if (state.getValue(Z)) {
            if (!this.checkConnection(level, pos, Direction.NORTH) && !this.checkConnection(level, pos, Direction.SOUTH)) newState = newState.setValue(Z, false);
            else flag = false;
        }

        if (flag) {
            level.destroyBlock(pos, true);
        } else if (!state.equals(newState)) {
            level.setBlockAndUpdate(pos, newState);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(X)) {
            if (this.canConnectTo(level.getBlockState(pos.relative(Direction.WEST)), Direction.WEST, level, pos)) return true;
            if (this.canConnectTo(level.getBlockState(pos.relative(Direction.EAST)), Direction.EAST, level, pos)) return true;
        }
        if (state.getValue(Y)) {
            if (this.canConnectTo(level.getBlockState(pos.relative(Direction.UP)), Direction.UP, level, pos)) return true;
        }
        if (state.getValue(Z)) {
            if (this.canConnectTo(level.getBlockState(pos.relative(Direction.NORTH)), Direction.NORTH, level, pos)) return true;
            return this.canConnectTo(level.getBlockState(pos.relative(Direction.SOUTH)), Direction.SOUTH, level, pos);
        }
        return false;
    }

    protected boolean canConnectTo(BlockState state, Direction dir, LevelReader level, BlockPos pos) {
        return state.getBlock() instanceof LeavesBlock || (state.is(this) && hasAxis(state, dir.getAxis())) || state.isFaceSturdy(level, pos, dir.getOpposite(), SupportType.CENTER);
    }

    protected boolean checkConnection(LevelReader level, BlockPos pos, Direction dir) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        while (true) {
            if (!level.getBlockState(mutable).is(this)) return true;
            mutable.move(dir);
            if (!this.canConnectTo(level.getBlockState(mutable), dir, level, mutable)) return false;
        }
    }

    public static boolean hasAxis(BlockState state, Direction.Axis axis) {
        return switch(axis) {
            case X -> state.getValue(X);
            case Y -> state.getValue(Y);
            case Z -> state.getValue(Z);
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
