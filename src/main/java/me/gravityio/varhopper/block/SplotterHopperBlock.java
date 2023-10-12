package me.gravityio.varhopper.block;

import me.gravityio.varhopper.ModBlockEntities;
import me.gravityio.varhopper.block.entity.SlopperHopperEntity;
import me.gravityio.varhopper.block.entity.SplotterHopperEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SplotterHopperBlock extends VanillaHopperBlock {
    protected static final VoxelShape NS_SHAPE = Util.make(() -> {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0.625, 0, 1, 0.6875, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0.6875, 0, 0.125, 1, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0.6875, 0, 1, 1, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0.6875, 0, 0.875, 1, 0.125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0.6875, 0.4375, 0.875, 1, 0.5625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0.6875, 0.875, 0.875, 1, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.25, 0.25, 0.75, 0.625, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.375, 0.25, 0, 0.625, 0.5, 0.25));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.375, 0.25, 0.75, 0.625, 0.5, 1));
        return shape;
    });

    protected static final VoxelShape EW_SHAPE = Util.make(() -> {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0.625, 0, 1, 0.6875, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0.6875, 0.875, 1, 1, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0.6875, 0, 1, 1, 0.125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0.6875, 0.125, 0.125, 1, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0.6875, 0.125, 0.5625, 1, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0.6875, 0.125, 1, 1, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.25, 0.25, 0.75, 0.625, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0.25, 0.375, 0.25, 0.5, 0.625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.75, 0.25, 0.375, 1, 0.5, 0.625));
        return shape;
    });

    protected static final VoxelShape NS_RAYCAST_SHAPE = Util.make(() -> {
        var north = Block.createCuboidShape(6, 8, 12, 10, 10, 16);
        var south = Block.createCuboidShape(6, 8, 0, 10, 10, 4);
        var ns = VoxelShapes.union(north, south);
        return VoxelShapes.union(ns, Hopper.INSIDE_SHAPE);
    });

    protected static final VoxelShape EW_RAYCAST_SHAPE = Util.make(() -> {
        var east = Block.createCuboidShape(12, 8, 6, 16, 10, 10);
        var west = Block.createCuboidShape(0, 8, 6, 4, 10, 10);
        var ew = VoxelShapes.union(east, west);
        return VoxelShapes.union(ew, Hopper.INSIDE_SHAPE);
    });

    public SplotterHopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SplotterHopperEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return null;
        }

        return BlockWithEntity.checkType(type, ModBlockEntities.SPLOTTER_HOPPER, (world1, pos, state1, splotterHopper) -> splotterHopper.tick(world1, pos, state1));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity item)) return;
        if (!(world.getBlockEntity(pos) instanceof SplotterHopperEntity splotterHopper)) {
            return;
        }
        var itemBox = entity.getBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ());
        var inputArea = splotterHopper.getInputAreaShape();
        if (!VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(itemBox), inputArea, BooleanBiFunction.AND)) {
            return;
        }
        splotterHopper.onItemCollision(state, world, pos, item);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing().rotateYClockwise();
        return this.getDefaultState().with(FACING, direction.getAxis() == Direction.Axis.Y ? Direction.DOWN : direction).with(ENABLED, true);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient || !(world.getBlockEntity(pos) instanceof SplotterHopperEntity splotterHopper)) {
            return ActionResult.SUCCESS;
        }

        player.openHandledScreen(splotterHopper);
        player.incrementStat(Stats.INSPECT_HOPPER);
        return ActionResult.CONSUME;
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return switch (state.get(FACING)) {
            case NORTH, SOUTH -> NS_RAYCAST_SHAPE;
            case EAST, WEST -> EW_RAYCAST_SHAPE;
            default -> VoxelShapes.empty();
        };
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH, SOUTH -> NS_SHAPE;
            case EAST, WEST -> EW_SHAPE;
            default -> VoxelShapes.empty();
        };
    }
}
