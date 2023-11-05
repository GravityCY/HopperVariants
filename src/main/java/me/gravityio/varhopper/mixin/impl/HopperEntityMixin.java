package me.gravityio.varhopper.mixin.impl;

import me.gravityio.varhopper.block.ModProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public class HopperEntityMixin {
    @Inject(
            method = "extract(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/Hopper;)Z",
            at = @At("HEAD"),
            cancellable = true)
    private static void dontExtractWhenLidClosed(World world, Hopper hopper, CallbackInfoReturnable<Boolean> cir) {
        var state = world.getBlockState(BlockPos.ofFloored(hopper.getHopperX(), hopper.getHopperY(), hopper.getHopperZ()));
        if (state.get(ModProperties.LID_OPENED)) return;
        cir.setReturnValue(false);
    }

    @Inject(method = "onEntityCollided", at = @At("HEAD"), cancellable = true)
    private static void dontExtractWhenLidClosedColl(World world, BlockPos pos, BlockState state, Entity entity, HopperBlockEntity blockEntity, CallbackInfo ci) {
        if (state.get(ModProperties.LID_OPENED)) return;
        ci.cancel();
    }
}
