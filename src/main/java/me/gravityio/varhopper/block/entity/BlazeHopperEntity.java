package me.gravityio.varhopper.block.entity;

import me.gravityio.varhopper.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

// Fast Hopper
public class BlazeHopperEntity extends AbstractHopperEntity {
    public BlazeHopperEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.BLAZE_HOPPER, blockPos, blockState);
    }

    @Override
    public int getDefaultCooldown() {
        return ModConfig.INSTANCE.blazeCooldown;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.varhopper.blaze_hopper");
    }
}
