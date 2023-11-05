package me.gravityio.varhopper.block.entity;

import me.gravityio.varhopper.ModConfig;
import me.gravityio.varhopper.screen.SlopperScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

// Slow Hopper == Slopper
public class SlopperHopperEntity extends AbstractHopperEntity {
    public SlopperHopperEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SLOPPER_HOPPER, blockPos, blockState);
    }

    @Override
    public int getInventorySize() {
        return 1;
    }

    @Override
    public int getDefaultCooldown() {
        return ModConfig.INSTANCE.slopperCooldown;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.varhopper.slopper_hopper");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new SlopperScreenHandler(syncId, playerInventory, this);
    }
}
