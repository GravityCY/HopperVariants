package me.gravityio.varhopper.block;

import me.gravityio.varhopper.block.entity.ModBlockEntities;
import me.gravityio.varhopper.block.entity.SlopperHopperEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.stream.Stream;

public class SlopperHopperBlock extends AbstractHopperBlock<SlopperHopperEntity> {

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
    protected BlockEntityType<SlopperHopperEntity> getEntityType() {
        return ModBlockEntities.SLOPPER_HOPPER;
    }
    @Override
    protected boolean isReplaceable() {
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        var shape = switch (state.get(FACING)) {
            case DOWN -> DOWN_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> VoxelShapes.empty();
        };
        if (state.get(LID_OPENED)) {
            return shape;
        }
        return VoxelShapes.union(LID, shape);
    }


}
