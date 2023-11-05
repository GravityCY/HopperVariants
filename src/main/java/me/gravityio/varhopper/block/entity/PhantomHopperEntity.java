package me.gravityio.varhopper.block.entity;

import me.gravityio.varhopper.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

// Super Hopper
public class PhantomHopperEntity extends AbstractHopperEntity {

    public PhantomHopperEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.PHANTOM_HOPPER, blockPos, blockState);
    }

    @Override
    public int getDefaultCooldown() {
        return ModConfig.INSTANCE.phantomCooldown;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.varhopper.phantom_hopper");
    }

}
