package me.gravityio.varhopper.block;

import me.gravityio.varhopper.ModHelper;
import me.gravityio.varhopper.block.entity.AbstractHopperEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public abstract class AbstractHopperBlock<T extends AbstractHopperEntity> extends BlockWithEntity {
    public static final BooleanProperty LID_OPENED = ModProperties.LID_OPENED;
    public static final DirectionProperty FACING = Properties.HOPPER_FACING;
    public static final BooleanProperty ENABLED = Properties.ENABLED;
    public static VoxelShape LID = Block.createCuboidShape(2.0, 14.0, 2.0, 14.0, 15.0, 14.0);
    protected static final VoxelShape TOP_SHAPE = Block.createCuboidShape(0.0, 10.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(4.0, 4.0, 4.0, 12.0, 10.0, 12.0);
    protected static final VoxelShape OUTSIDE_SHAPE = VoxelShapes.union(MIDDLE_SHAPE, TOP_SHAPE);
    protected static final VoxelShape DEFAULT_SHAPE = VoxelShapes.combineAndSimplify(OUTSIDE_SHAPE, Hopper.INSIDE_SHAPE, BooleanBiFunction.ONLY_FIRST);
    protected static final VoxelShape DOWN_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 4.0, 10.0));
    protected static final VoxelShape EAST_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(12.0, 4.0, 6.0, 16.0, 8.0, 10.0));
    protected static final VoxelShape NORTH_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(6.0, 4.0, 0.0, 10.0, 8.0, 4.0));
    protected static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(6.0, 4.0, 12.0, 10.0, 8.0, 16.0));
    protected static final VoxelShape WEST_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(0.0, 4.0, 6.0, 4.0, 8.0, 10.0));
    protected static final VoxelShape DOWN_RAYCAST_SHAPE = Hopper.INSIDE_SHAPE;
    protected static final VoxelShape EAST_RAYCAST_SHAPE = VoxelShapes.union(Hopper.INSIDE_SHAPE, Block.createCuboidShape(12.0, 8.0, 6.0, 16.0, 10.0, 10.0));
    protected static final VoxelShape NORTH_RAYCAST_SHAPE = VoxelShapes.union(Hopper.INSIDE_SHAPE, Block.createCuboidShape(6.0, 8.0, 0.0, 10.0, 10.0, 4.0));
    protected static final VoxelShape SOUTH_RAYCAST_SHAPE = VoxelShapes.union(Hopper.INSIDE_SHAPE, Block.createCuboidShape(6.0, 8.0, 12.0, 10.0, 10.0, 16.0));
    protected static final VoxelShape WEST_RAYCAST_SHAPE = VoxelShapes.union(Hopper.INSIDE_SHAPE, Block.createCuboidShape(0.0, 8.0, 6.0, 4.0, 10.0, 10.0));

    public static boolean replaceWithOther(PlayerEntity player, World world, BlockPos pos, BlockState state) {
        var offhand = player.getOffHandStack();
        var replaceableHopper = ModHelper.getReplaceableHopper(offhand);
        if (replaceableHopper == null || state.getBlock() == replaceableHopper) return false;
        offhand.decrement(1);
        player.getInventory().offerOrDrop(state.getBlock().asItem().getDefaultStack());
        world.setBlockState(pos, replaceableHopper.getStateWithProperties(state));
        return true;
    }

    public static VoxelShape getOutlineShape(BlockState state) {
        var shape = switch (state.get(FACING)) {
            case DOWN -> DOWN_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> DEFAULT_SHAPE;
        };

        if (state.get(LID_OPENED)) {
            return shape;
        }

        return VoxelShapes.union(LID, shape);
    }

    public static VoxelShape getRaycastShape(BlockState state) {
        return switch (state.get(FACING)) {
            case DOWN -> DOWN_RAYCAST_SHAPE;
            case NORTH -> NORTH_RAYCAST_SHAPE;
            case SOUTH -> SOUTH_RAYCAST_SHAPE;
            case WEST -> WEST_RAYCAST_SHAPE;
            case EAST -> EAST_RAYCAST_SHAPE;
            default -> Hopper.INSIDE_SHAPE;
        };
    }

    protected AbstractHopperBlock(Settings settings) {
        super(settings);
    }


    protected abstract BlockEntityType<T> getEntityType();
    protected abstract boolean isReplaceable();

    protected void updateEnabled(World world, BlockPos pos, BlockState state, int flags) {
        boolean enabled = !world.isReceivingRedstonePower(pos);
        if (enabled == state.get(ENABLED)) return;

        world.setBlockState(pos, state.with(ENABLED, enabled), flags);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return this.getEntityType().instantiate(pos, state);
    }

    @Nullable
    @Override
    public <Y extends BlockEntity> BlockEntityTicker<Y> getTicker(World world, BlockState state, BlockEntityType<Y> type) {
        if (world.isClient) {
            return null;
        }

        return BlockWithEntity.checkType(
                type, this.getEntityType(),
                (tickWorld, tickPos, tickState, tickAbstractHopperEntity) ->
                        tickAbstractHopperEntity.tick(tickWorld, tickPos, tickState)
        );
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity item)) return;
        var abstractHopperEntity = this.getEntityType().get(world, pos);
        if (abstractHopperEntity == null) return;
        var itemBox = entity.getBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ());
        var inputArea = abstractHopperEntity.getInputAreaShape();
        if (!VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(itemBox), inputArea, BooleanBiFunction.AND)) {
            return;
        }
        abstractHopperEntity.onItemCollision(state, world, pos, item);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var abstractHopperEntity = this.getEntityType().get(world, pos);
        if (world.isClient || abstractHopperEntity == null) {
            return ActionResult.SUCCESS;
        }

        if (player.isSneaking()) {
            world.setBlockState(pos, state.with(LID_OPENED, !state.get(LID_OPENED)));
            return ActionResult.CONSUME;
        }

        if (!this.isReplaceable() || !replaceWithOther(player, world, pos, state)) {
            player.openHandledScreen(abstractHopperEntity);
            player.incrementStat(Stats.INSPECT_HOPPER);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AbstractHopperBlock.getOutlineShape(state);
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return AbstractHopperBlock.getRaycastShape(state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide().getOpposite();
        return this.getDefaultState().with(FACING, direction.getAxis() == Direction.Axis.Y ? Direction.DOWN : direction).with(ENABLED, true);
    }
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!itemStack.hasCustomName() || !(world.getBlockEntity(pos) instanceof AbstractHopperEntity hopper)) return;

        hopper.setCustomName(itemStack.getName());
    }
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.isOf(state.getBlock())) {
            return;
        }

        this.updateEnabled(world, pos, state, NOTIFY_LISTENERS);
    }
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AbstractHopperEntity hopper) {
            ItemScatterer.spawn(world, pos, hopper);
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        this.updateEnabled(world, pos, state, 4);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED, LID_OPENED);
    }
    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
