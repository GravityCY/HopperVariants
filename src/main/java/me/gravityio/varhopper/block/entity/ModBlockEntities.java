package me.gravityio.varhopper.block.entity;

import me.gravityio.varhopper.VarHopperMod;
import me.gravityio.varhopper.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<SlopperHopperEntity> SLOPPER_HOPPER = register(
            "slopper_hopper", SlopperHopperEntity::new, ModBlocks.SLOPPER_HOPPER
    );

    public static final BlockEntityType<BlazeHopperEntity> BLAZE_HOPPER = register(
            "blaze_hopper", BlazeHopperEntity::new, ModBlocks.BLAZE_HOPPER
    );

    public static final BlockEntityType<PhantomHopperEntity> PHANTOM_HOPPER = register(
            "phantom_hopper", PhantomHopperEntity::new, ModBlocks.PHANTOM_HOPPER
    );

    public static final BlockEntityType<EchoHopperEntity> ECHO_HOPPER = register(
            "echo_hopper", EchoHopperEntity::new, ModBlocks.ECHO_HOPPER
    );

    public static final BlockEntityType<SplotterHopperEntity> SPLOTTER_HOPPER = register(
            "splotter_hopper", SplotterHopperEntity::new, ModBlocks.SPLOTTER_HOPPER
    );

    public static <T extends BlockEntity> BlockEntityType<T> register(
            String name, FabricBlockEntityTypeBuilder.Factory<T> builder, Block block) {
        VarHopperMod.DEBUG("[ModBlockEntities] Registering block entity {}", name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(VarHopperMod.MOD_ID, name),
                FabricBlockEntityTypeBuilder.create(builder, block).build()               );
    }

    public static void init() {

    }

}
