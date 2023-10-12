package me.gravityio.varhopper.block.entity;

import me.gravityio.varhopper.ModBlockEntities;
import me.gravityio.varhopper.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

// Slow Hopper == Slopper
public class SlopperHopperEntity extends VanillaHopperEntity {
    public SlopperHopperEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SLOPPER_HOPPER, blockPos, blockState);
    }

    @Override
    public int getDefaultCooldown() {
        return ModConfig.INSTANCE.slopperCooldown;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.varhopper.slopper_hopper");
    }
}
