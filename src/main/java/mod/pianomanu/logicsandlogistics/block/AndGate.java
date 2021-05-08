package mod.pianomanu.logicsandlogistics.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

import static net.minecraft.state.properties.BlockStateProperties.LOCKED;

public class AndGate extends AbstractTwoInputsBlock {
    public AndGate(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LEFT_POWERED, false).setValue(RIGHT_POWERED, false).setValue(OUTPUT_VALUE, 0).setValue(LOCKED, false));
    }

    @Override
    public int getDelay(BlockState state) {
        return 2;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateContainer) {
        stateContainer.add(FACING, LEFT_POWERED, RIGHT_POWERED, OUTPUT_VALUE, LOCKED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockstate = super.getStateForPlacement(context);
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return blockstate.setValue(LOCKED, this.isLocked(context.getLevel(), context.getClickedPos(), blockstate)).setValue(LEFT_POWERED, getLeftSignal(world, pos, blockstate) > 0).setValue(RIGHT_POWERED, getRightSignal(world, pos, blockstate) > 0);
    }

    public int getSignal(BlockState state, IBlockReader blockReader, BlockPos pos, Direction direction) {
        if (!state.getValue(LEFT_POWERED) || !state.getValue(RIGHT_POWERED)) {
            return 0;
        } else {
            return state.getValue(FACING) == direction ? this.getOutputSignal(blockReader, pos, state) : 0;
        }
    }

    public int getInputSignal(World world, BlockPos pos, BlockState state) {
        int sigLeft = getLeftSignal(world, pos, state);
        int sigRight = getRightSignal(world, pos, state);
        if (sigLeft == 0 || sigRight == 0) {
            return 0;
        }
        return Math.max(sigLeft, sigRight);
    }

    public boolean shouldTurnOn(World world, BlockPos pos, BlockState state) {
        return this.getInputSignal(world, pos, state) > 0;
    }

    public int getOutputSignal(IBlockReader blockReader, BlockPos pos, BlockState state) {
        if (blockReader.getBlockState(pos).getBlock() instanceof AbstractTwoInputsBlock)
            return blockReader.getBlockState(pos).getValue(OUTPUT_VALUE);
        return 0;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if (!this.isLocked(world, pos, state)) {
            boolean leftPowered = getLeftSignal(world, pos, state) > 0;
            boolean rightPowered = getRightSignal(world, pos, state) > 0;
            if (leftPowered && !rightPowered) {
                world.setBlock(pos, state.setValue(LEFT_POWERED, true).setValue(RIGHT_POWERED, false).setValue(OUTPUT_VALUE, 0), 2);
            } else if (!leftPowered && rightPowered) {
                world.setBlock(pos, state.setValue(LEFT_POWERED, false).setValue(RIGHT_POWERED, true).setValue(OUTPUT_VALUE, 0), 2);
            } else if (!leftPowered) {
                world.setBlock(pos, state.setValue(LEFT_POWERED, false).setValue(RIGHT_POWERED, false).setValue(OUTPUT_VALUE, 0), 2);
            } else {
                world.setBlock(pos, state.setValue(LEFT_POWERED, true).setValue(RIGHT_POWERED, true).setValue(OUTPUT_VALUE, Math.max(getLeftSignal(world, pos, state), getRightSignal(world, pos, state))), 2);
            }
            world.getBlockTicks().scheduleTick(pos, this, this.getDelay(state), TickPriority.VERY_HIGH);
        }
    }
}
