package me.gravityio.varhopper.block;

import me.gravityio.varhopper.ModBlockEntities;
import me.gravityio.varhopper.block.entity.EchoHopperEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
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
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EchoHopperBlock extends VanillaHopperBlock {
    public EchoHopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EchoHopperEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return null;
        }

        return BlockWithEntity.checkType(type, ModBlockEntities.ECHO_HOPPER, (world1, pos, state1, echoHopper) -> echoHopper.tick(world1, pos, state1));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity item)) return;
        if (!(world.getBlockEntity(pos) instanceof EchoHopperEntity echoHopper)) {
            return;
        }
        var itemBox = entity.getBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ());
        var inputArea = echoHopper.getInputAreaShape();
        if (!VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(itemBox), inputArea, BooleanBiFunction.AND)) {
            return;
        }
        echoHopper.onItemCollision(state, world, pos, item);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient || !(world.getBlockEntity(pos) instanceof EchoHopperEntity echoHopper)) {
            return ActionResult.SUCCESS;
        }

        player.openHandledScreen(echoHopper);
        player.incrementStat(Stats.INSPECT_HOPPER);
        return ActionResult.CONSUME;
    }
}
