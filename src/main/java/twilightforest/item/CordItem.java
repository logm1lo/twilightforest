package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.CordBlock;

import javax.annotation.Nullable;

public class CordItem extends BlockItem {
    public CordItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState blockstate = level.getBlockState(blockpos);
        Block block = this.getBlock();

        if (!blockstate.is(block)) {
            return context;
        } else {
            Direction direction = this.getForward(context, blockstate);

            int i = 0;
            BlockPos.MutableBlockPos mutableBlockPos = blockpos.mutable();

            while(i < 7) {
                if (!level.isClientSide && !level.isInWorldBounds(mutableBlockPos)) {
                    Player player = context.getPlayer();
                    int j = level.getMaxBuildHeight();
                    if (player instanceof ServerPlayer && mutableBlockPos.getY() >= j) {
                        ((ServerPlayer)player).sendSystemMessage(Component.translatable("build.tooHigh", j - 1).withStyle(ChatFormatting.RED), true);
                    }
                    break;
                }

                blockstate = level.getBlockState(mutableBlockPos);
                if (!blockstate.is(this.getBlock())) {
                    if (blockstate.canBeReplaced(context)) return BlockPlaceContext.at(context, mutableBlockPos, direction);
                    break;
                } else {
                    if (!stateHasValue(blockstate, direction)) return BlockPlaceContext.at(context, mutableBlockPos, direction);
                }

                mutableBlockPos.move(direction);
                if (direction.getAxis().isHorizontal()) {
                    ++i;
                }
            }

            return null;
        }
    }

    protected boolean stateHasValue(BlockState state, Direction direction) {
        if (direction.getAxis() == Direction.Axis.X) {
            return state.getValue(CordBlock.X);
        } else if (direction.getAxis() == Direction.Axis.Z) {
            return state.getValue(CordBlock.Z);
        } else {
            return direction == Direction.UP ? state.getValue(CordBlock.UP) : state.getValue(CordBlock.DOWN);
        }
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);


        if (state.is(this.getBlock())) {
            Direction direction = context.getClickedFace();
            if (direction.getAxis() == Direction.Axis.X && !state.getValue(CordBlock.X)) {
                return state.setValue(CordBlock.X, true);
            } else if (direction.getAxis() == Direction.Axis.Y && !state.getValue(CordBlock.UP)) {
                return state.setValue(CordBlock.UP, true).setValue(CordBlock.DOWN, true);
            } else if (direction.getAxis() == Direction.Axis.Z && !state.getValue(CordBlock.Z)) {
                return state.setValue(CordBlock.Z, true);
            } else {
                //TODO
            }
        }

        return super.getPlacementState(context);
    }

    protected Direction getForward(BlockPlaceContext context, BlockState state) {
        if (context.isSecondaryUseActive()) {
            return context.isInside() ? context.getClickedFace().getOpposite() : context.getClickedFace();//TODO
        } else if (context.getClickedFace().getAxis() != Direction.Axis.Y) {
            return Direction.DOWN;
        } else {
            Direction tempDir = context.getHorizontalDirection();
            if (tempDir.getAxis() == Direction.Axis.X ? state.getValue(CordBlock.X) : state.getValue(CordBlock.Z)) return tempDir;
            else return Direction.DOWN;
        }
    }

    @Override
    protected boolean mustSurvive() {
        return false;
    }
}
