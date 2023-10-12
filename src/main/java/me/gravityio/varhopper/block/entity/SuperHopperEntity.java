package me.gravityio.varhopper.block.entity;

import me.gravityio.varhopper.ModBlockEntities;
import me.gravityio.varhopper.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

// Super Hopper
public class SuperHopperEntity extends VanillaHopperEntity {

    public SuperHopperEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SUPER_HOPPER, blockPos, blockState);
    }

    @Override
    public int getDefaultCooldown() {
        return ModConfig.INSTANCE.superCooldown;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.varhopper.super_hopper");
    }

}
