package me.gravityio.varhopper.block.entity;

import me.gravityio.varhopper.ModBlockEntities;
import me.gravityio.varhopper.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

// Fast Hopper
public class HyperHopperEntity extends VanillaHopperEntity {
    public HyperHopperEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.HYPER_HOPPER, blockPos, blockState);
    }

    @Override
    public int getDefaultCooldown() {
        return ModConfig.INSTANCE.hyperCooldown;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.varhopper.hyper_hopper");
    }
}
