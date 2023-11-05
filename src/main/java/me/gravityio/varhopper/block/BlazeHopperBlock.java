package me.gravityio.varhopper.block;

import me.gravityio.varhopper.block.entity.BlazeHopperEntity;
import me.gravityio.varhopper.block.entity.ModBlockEntities;
import net.minecraft.block.entity.BlockEntityType;

public class BlazeHopperBlock extends AbstractHopperBlock<BlazeHopperEntity> {
    public BlazeHopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected BlockEntityType<BlazeHopperEntity> getEntityType() {
        return ModBlockEntities.BLAZE_HOPPER;
    }

    @Override
    protected boolean isReplaceable() {
        return true;
    }


}
