package me.gravityio.varhopper.block;

import me.gravityio.varhopper.block.entity.ModBlockEntities;
import me.gravityio.varhopper.block.entity.PhantomHopperEntity;
import net.minecraft.block.entity.BlockEntityType;

public class PhantomHopperBlock extends AbstractHopperBlock<PhantomHopperEntity> {
    public PhantomHopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected BlockEntityType<PhantomHopperEntity> getEntityType() {
        return ModBlockEntities.PHANTOM_HOPPER;
    }

    @Override
    protected boolean isReplaceable() {
        return true;
    }
}
