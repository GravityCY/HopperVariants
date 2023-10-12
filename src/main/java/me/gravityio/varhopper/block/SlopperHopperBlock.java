package me.gravityio.varhopper.block;

import me.gravityio.varhopper.ModBlockEntities;
import me.gravityio.varhopper.block.entity.SlopperHopperEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class SlopperHopperBlock extends VanillaHopperBlock {

    protected final VoxelShape DOWN_SHAPE = Stream.of(
            Block.createCuboidShape(0, 10, 0, 16, 11, 16),
            Block.createCuboidShape(0, 11, 0, 5, 16, 16),
            Block.createCuboidShape(11, 11, 0, 16, 16, 16),
            Block.createCuboidShape(5, 11, 0, 11, 16, 5),
            Block.createCuboidShape(5, 11, 11, 11, 16, 16),
            Block.createCuboidShape(4, 4, 4, 12, 10, 12),
            Block.createCuboidShape(6, 0, 6, 10, 4, 10)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    protected final VoxelShape NORTH_SHAPE = Stream.of(
            Block.createCuboidShape(0, 10, 0, 16, 11, 16),
            Block.createCuboidShape(0, 11, 0, 5, 16, 16),
            Block.createCuboidShape(11, 11, 0, 16, 16, 16),
            Block.createCuboidShape(5, 11, 0, 11, 16, 5),
            Block.createCuboidShape(5, 11, 11, 11, 16, 16),
            Block.createCuboidShape(4, 4, 4, 12, 10, 12),
            Block.createCuboidShape(6, 4, 0, 10, 8, 4)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    protected final VoxelShape EAST_SHAPE = Stream.of(
            Block.createCuboidShape(0, 10, 0, 16, 11, 16),
            Block.createCuboidShape(0, 11, 0, 16, 16, 5),
            Block.createCuboidShape(0, 11, 11, 16, 16, 16),
            Block.createCuboidShape(11, 11, 5, 16, 16, 11),
            Block.createCuboidShape(0, 11, 5, 5, 16, 11),
            Block.createCuboidShape(4, 4, 4, 12, 10, 12),
            Block.createCuboidShape(12, 4, 6, 16, 8, 10)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    protected final VoxelShape SOUTH_SHAPE = Stream.of(
            Block.createCuboidShape(0, 10, 0, 16, 11, 16),
            Block.createCuboidShape(11, 11, 0, 16, 16, 16),
            Block.createCuboidShape(0, 11, 0, 5, 16, 16),
            Block.createCuboidShape(5, 11, 11, 11, 16, 16),
            Block.createCuboidShape(5, 11, 0, 11, 16, 5),
            Block.createCuboidShape(4, 4, 4, 12, 10, 12),
            Block.createCuboidShape(6, 4, 12, 10, 8, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    protected final VoxelShape WEST_SHAPE = Stream.of(
            Block.createCuboidShape(0, 10, 0, 16, 11, 16),
            Block.createCuboidShape(0, 11, 11, 16, 16, 16),
            Block.createCuboidShape(0, 11, 0, 16, 16, 5),
            Block.createCuboidShape(0, 11, 5, 5, 16, 11),
            Block.createCuboidShape(11, 11, 5, 16, 16, 11),
            Block.createCuboidShape(4, 4, 4, 11, 10, 12),
            Block.createCuboidShape(0, 4, 6, 4, 8, 10)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    public SlopperHopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case DOWN -> DOWN_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> VoxelShapes.empty();
        };
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SlopperHopperEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return null;
        }

        return BlockWithEntity.checkType(type, ModBlockEntities.SLOPPER_HOPPER, (world1, pos, state1, slopperHopper) -> slopperHopper.tick(world1, pos, state1));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity item)) return;
        if (!(world.getBlockEntity(pos) instanceof SlopperHopperEntity slopperHopper)) {
            return;
        }
        var itemBox = entity.getBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ());
        var inputArea = slopperHopper.getInputAreaShape();
        if (!VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(itemBox), inputArea, BooleanBiFunction.AND)) {
            return;
        }
        slopperHopper.onItemCollision(state, world, pos, item);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient || !(world.getBlockEntity(pos) instanceof SlopperHopperEntity slopperHopper)) {
            return ActionResult.SUCCESS;
        }

        player.openHandledScreen(slopperHopper);
        player.incrementStat(Stats.INSPECT_HOPPER);
        return ActionResult.CONSUME;
    }
}
