package mod.pianomanu.logicsandlogistics.block;

import mod.pianomanu.logicsandlogistics.util.BlockStatePropertiesLAL;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

/**
 * Some description will follow, when I have time
 *
 * @author PianoManu
 * @version 1.0 05/08/2021
 */
//@SuppressWarnings("all")
public abstract class AbstractTwoInputsBlock extends HorizontalBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final BooleanProperty LEFT_POWERED = BlockStatePropertiesLAL.LEFT_POWERED;
    public static final BooleanProperty RIGHT_POWERED = BlockStatePropertiesLAL.RIGHT_POWERED;
    public static final IntegerProperty OUTPUT_VALUE = BlockStatePropertiesLAL.OUTPUT_VALUE;

    protected AbstractTwoInputsBlock(Properties p_i48377_1_) {
        super(p_i48377_1_);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    public VoxelShape getShape(@Nullable BlockState p_220053_1_, @Nullable IBlockReader p_220053_2_, @Nullable BlockPos p_220053_3_, @Nullable ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    public boolean canSurvive(@Nullable BlockState state, @Nullable IWorldReader world, BlockPos pos) {
        return canSupportRigidBlock(Objects.requireNonNull(world), pos.below());
    }

    protected void checkTickOnNeighbor(World world, BlockPos pos, BlockState state) {
        if (!this.isLocked(world, pos, state)) {
            boolean isLeftPowered = state.getValue(LEFT_POWERED);
            boolean isRightPowered = state.getValue(RIGHT_POWERED);
            boolean shouldTurnOn = this.shouldTurnOn(world, pos, state);
            if (!world.getBlockTicks().willTickThisTick(pos, this)) {
                TickPriority tickpriority = TickPriority.HIGH;
                if (this.shouldPrioritize(world, pos, state)) {
                    tickpriority = TickPriority.EXTREMELY_HIGH;
                } else if (isLeftPowered || isRightPowered) {
                    tickpriority = TickPriority.VERY_HIGH;
                }

                world.getBlockTicks().scheduleTick(pos, this, this.getDelay(state), tickpriority);
            }

        }
    }

    public boolean shouldPrioritize(IBlockReader blockReader, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING).getOpposite();
        BlockState blockstate = blockReader.getBlockState(pos.relative(direction));
        return RedstoneDiodeBlock.isDiode(blockstate) && blockstate.getValue(FACING) != direction;
    }

    public boolean isLocked(IWorldReader world, BlockPos pos, BlockState state) {
        return false;
    }

    public abstract int getDelay(BlockState state);

    public abstract boolean shouldTurnOn(World world, BlockPos pos, BlockState state);

    public abstract int getInputSignal(World world, BlockPos pos, BlockState state);

    public int getLeftSignal(World world, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        Direction dirLeft = direction.getClockWise();
        BlockPos blockposLeft = pos.relative(dirLeft);
        int sigLeft = world.getSignal(blockposLeft, direction);
        if (isComparatorBlockWithCorrectDirectionAndPowered(world, blockposLeft, dirLeft)) {
            sigLeft = 1;
        }
        if (isRepeaterBlockWithCorrectDirectionAndPowered(world, blockposLeft, dirLeft)) {
            sigLeft = 15;
        }
        if (isTwoInputsBlockWithCorrectDirectionAndPowered(world, blockposLeft, dirLeft)) {
            sigLeft = world.getBlockState(blockposLeft).getValue(AbstractTwoInputsBlock.OUTPUT_VALUE);
        }
        BlockState blockstateLeft = world.getBlockState(blockposLeft);
        return Math.max(sigLeft, blockstateLeft.is(Blocks.REDSTONE_WIRE) ? blockstateLeft.getValue(RedstoneWireBlock.POWER) : 0);
    }

    public int getRightSignal(World world, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        Direction dirRight = direction.getCounterClockWise();
        BlockPos blockposRight = pos.relative(dirRight);
        int sigRight = world.getSignal(blockposRight, direction);
        if (isComparatorBlockWithCorrectDirectionAndPowered(world, blockposRight, dirRight)) {
            sigRight = 1;
        }
        if (isRepeaterBlockWithCorrectDirectionAndPowered(world, blockposRight, dirRight)) {
            sigRight = 15;
        }
        if (isTwoInputsBlockWithCorrectDirectionAndPowered(world, blockposRight, dirRight)) {
            sigRight = world.getBlockState(blockposRight).getValue(AbstractTwoInputsBlock.OUTPUT_VALUE);
        }
        BlockState blockstateRight = world.getBlockState(blockposRight);
        return Math.max(sigRight, blockstateRight.is(Blocks.REDSTONE_WIRE) ? blockstateRight.getValue(RedstoneWireBlock.POWER) : 0);
    }

    protected boolean isComparatorBlockWithCorrectDirectionAndPowered(World world, BlockPos pos, Direction direction) {
        BlockState comparatorState = world.getBlockState(pos);
        if (comparatorState.getBlock() instanceof ComparatorBlock)
            return comparatorState.getValue(ComparatorBlock.FACING) == direction && comparatorState.getValue(ComparatorBlock.POWERED);
        return false;
    }

    protected boolean isRepeaterBlockWithCorrectDirectionAndPowered(World world, BlockPos pos, Direction direction) {
        BlockState repeaterState = world.getBlockState(pos);
        if (repeaterState.getBlock() instanceof RepeaterBlock)
            return repeaterState.getValue(RepeaterBlock.FACING) == direction && repeaterState.getValue(RepeaterBlock.POWERED);
        return false;
    }

    protected boolean isTwoInputsBlockWithCorrectDirectionAndPowered(World world, BlockPos pos, Direction direction) {
        BlockState twoInputsBlockState = world.getBlockState(pos);
        if (twoInputsBlockState.getBlock() instanceof AbstractTwoInputsBlock)
            return twoInputsBlockState.getValue(AbstractTwoInputsBlock.OUTPUT_VALUE) > 0 && twoInputsBlockState.getValue(AbstractTwoInputsBlock.FACING) == direction;
        return false;
    }

    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, @Nullable World world, @Nullable BlockPos pos, @Nullable Block block, @Nullable BlockPos pos2, boolean bool) {
        if (state.canSurvive(Objects.requireNonNull(world), Objects.requireNonNull(pos))) {
            this.checkTickOnNeighbor(world, pos, state);
        } else {
            TileEntity tileentity = state.hasTileEntity() ? world.getBlockEntity(pos) : null;
            dropResources(state, world, pos, tileentity);
            world.removeBlock(pos, false);

            for (Direction direction : Direction.values()) {
                world.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public abstract void tick(@Nullable BlockState state, @Nullable ServerWorld world, @Nullable BlockPos pos, @Nullable Random rand);

    @SuppressWarnings("deprecation")
    public abstract int getSignal(@Nullable BlockState state, @Nullable IBlockReader blockReader, @Nullable BlockPos pos, @Nullable Direction direction);

    public int getOutputSignal(IBlockReader blockReader, BlockPos pos, BlockState state) {
        return state.getValue(OUTPUT_VALUE);
    }

    protected int getAlternateSignal(IWorldReader worldReader, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        Direction direction1 = direction.getClockWise();
        Direction direction2 = direction.getCounterClockWise();
        return Math.max(this.getAlternateSignalAt(worldReader, pos.relative(direction1), direction1), this.getAlternateSignalAt(worldReader, pos.relative(direction2), direction2));
    }

    protected int getAlternateSignalAt(IWorldReader worldReader, BlockPos pos, Direction direction) {
        BlockState blockstate = worldReader.getBlockState(pos);
        if (this.isAlternateInput(blockstate)) {
            if (blockstate.is(Blocks.REDSTONE_BLOCK)) {
                return 15;
            } else {
                return blockstate.is(Blocks.REDSTONE_WIRE) ? blockstate.getValue(RedstoneWireBlock.POWER) : worldReader.getDirectSignal(pos, direction);
            }
        } else {
            return 0;
        }
    }

    @SuppressWarnings("deprecation")
    public abstract boolean isSignalSource(@Nullable BlockState state);

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public void setPlacedBy(@Nullable World world, @Nullable BlockPos pos, @Nullable BlockState state, LivingEntity entity, @Nullable ItemStack stack) {
        if (this.shouldTurnOn(world, pos, state)) {
            Objects.requireNonNull(world).getBlockTicks().scheduleTick(Objects.requireNonNull(pos), this, 1);
        }

    }

    @SuppressWarnings("deprecation")
    public void onPlace(@Nullable BlockState state, @Nullable World world, @Nullable BlockPos pos, @Nullable BlockState state2, boolean flag) {
        this.updateNeighborsInFront(world, Objects.requireNonNull(pos), Objects.requireNonNull(state));
    }

    @SuppressWarnings("deprecation")
    public void onRemove(@Nullable BlockState state, @Nullable World world, @Nullable BlockPos pos, @Nullable BlockState state2, boolean flag) {
        if (!flag && !Objects.requireNonNull(state).is(Objects.requireNonNull(state2).getBlock())) {
            super.onRemove(state, Objects.requireNonNull(world), Objects.requireNonNull(pos), state2, flag);
            this.updateNeighborsInFront(world, pos, state);
        }
    }

    protected void updateNeighborsInFront(World world, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(world, pos, world.getBlockState(pos), java.util.EnumSet.of(direction.getOpposite()), false).isCanceled())
            return;
        world.neighborChanged(blockpos, this, pos);
        world.updateNeighborsAtExceptFromFacing(blockpos, this, direction);
    }

    protected boolean isAlternateInput(BlockState state) {
        return state.isSignalSource();
    }

    public static boolean isTwoInputBlock(BlockState state) {
        return state.getBlock() instanceof AbstractTwoInputsBlock;
    }
}
//========SOLI DEO GLORIA========//