package me.gravityio.varhopper.mixin.impl;

import me.gravityio.varhopper.block.AbstractHopperBlock;
import me.gravityio.varhopper.block.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlock.class)
public class HopperMixin {
    @Shadow @Final public static DirectionProperty FACING;

    @Inject(
            method = "onUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;"
            ),
            cancellable = true
    )
    private void replace(
            BlockState state, World world, BlockPos pos,
            PlayerEntity player, Hand hand, BlockHitResult hit,
            CallbackInfoReturnable<ActionResult> cir) {
        if (player.isSneaking()) {
            world.setBlockState(pos, state.with(ModProperties.LID_OPENED, !state.get(ModProperties.LID_OPENED)));
            cir.setReturnValue(ActionResult.CONSUME);
            return;
        }
        if (AbstractHopperBlock.replaceWithOther(player, world, pos, state)) {
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }

    @Inject(method = "appendProperties", at = @At("RETURN"))
    private void addLidOpen(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(ModProperties.LID_OPENED);
    }

    @Inject(method = "getOutlineShape", at = @At("RETURN"), cancellable = true)
    private void setOutlineShape(
            BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (state.get(ModProperties.LID_OPENED)) {
            return;
        }

        cir.setReturnValue(VoxelShapes.union(AbstractHopperBlock.LID, cir.getReturnValue()));
    }
}
