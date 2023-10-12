package me.gravityio.varhopper;

import me.gravityio.varhopper.block.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<SuperHopperEntity> SUPER_HOPPER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(VarHopperMod.MOD_ID, "super_hopper"),
            FabricBlockEntityTypeBuilder.create(SuperHopperEntity::new, ModBlocks.SUPER_HOPPER).build()
    );

    public static final BlockEntityType<HyperHopperEntity> HYPER_HOPPER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(VarHopperMod.MOD_ID, "hyper_hopper"),
            FabricBlockEntityTypeBuilder.create(HyperHopperEntity::new, ModBlocks.HYPER_HOPPER).build()
    );

    public static final BlockEntityType<EchoHopperEntity> ECHO_HOPPER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(VarHopperMod.MOD_ID, "echo_hopper"),
            FabricBlockEntityTypeBuilder.create(EchoHopperEntity::new, ModBlocks.ECHO_HOPPER).build()
    );

    public static final BlockEntityType<SlopperHopperEntity> SLOPPER_HOPPER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(VarHopperMod.MOD_ID, "slopper_hopper"),
            FabricBlockEntityTypeBuilder.create(SlopperHopperEntity::new, ModBlocks.SLOPPER_HOPPER).build()
    );

    public static final BlockEntityType<SplotterHopperEntity> SPLOTTER_HOPPER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(VarHopperMod.MOD_ID, "splotter_hopper"),
            FabricBlockEntityTypeBuilder.create(SplotterHopperEntity::new, ModBlocks.SPLOTTER_HOPPER).build()
    );

    public static void init() {

    }

}
