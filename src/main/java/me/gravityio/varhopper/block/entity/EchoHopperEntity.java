package me.gravityio.varhopper.block.entity;

import me.gravityio.varhopper.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

// Super Hopper
public class EchoHopperEntity extends AbstractHopperEntity {

    public EchoHopperEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ECHO_HOPPER, blockPos, blockState);
    }

    @Override
    public int getDefaultCooldown() {
        return ModConfig.INSTANCE.echoCooldown;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.varhopper.echo_hopper");
    }

}
