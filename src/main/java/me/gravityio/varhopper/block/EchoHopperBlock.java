package me.gravityio.varhopper.block;

import me.gravityio.varhopper.block.entity.EchoHopperEntity;
import me.gravityio.varhopper.block.entity.ModBlockEntities;
import net.minecraft.block.entity.BlockEntityType;

public class EchoHopperBlock extends AbstractHopperBlock<EchoHopperEntity> {
    public EchoHopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected BlockEntityType<EchoHopperEntity> getEntityType() {
        return ModBlockEntities.ECHO_HOPPER;
    }

    @Override
    protected boolean isReplaceable() {
        return true;
    }

}
