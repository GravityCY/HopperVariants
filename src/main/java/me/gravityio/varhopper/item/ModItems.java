package me.gravityio.varhopper.item;

import me.gravityio.varhopper.VarHopperMod;
import me.gravityio.varhopper.block.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item SLOPPER_HOPPER = register("slopper_hopper", ModBlocks.SLOPPER_HOPPER);
    public static final Item BLAZE_HOPPER = register("blaze_hopper", ModBlocks.BLAZE_HOPPER);
    public static final Item PHANTOM_HOPPER = register("phantom_hopper", ModBlocks.PHANTOM_HOPPER);
    public static final Item ECHO_HOPPER = register("echo_hopper", ModBlocks.ECHO_HOPPER);
    public static final Item SPLOTTER_HOPPER = register("splotter_hopper", ModBlocks.SPLOTTER_HOPPER);

    private static Item register(String name, Block block) {
        VarHopperMod.DEBUG("[ModItems] Registering block item {}", name);
        return Registry.register(
                Registries.ITEM,
                new Identifier(VarHopperMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings())
        );
    }

    private static Item register(String name, Item item) {
        VarHopperMod.DEBUG("[ModItems] Registering item {}", name);
        return Registry.register(
                Registries.ITEM,
                new Identifier(VarHopperMod.MOD_ID, name),
                item
        );
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.add(ECHO_HOPPER);
            entries.add(PHANTOM_HOPPER);
            entries.add(BLAZE_HOPPER);
            entries.add(SLOPPER_HOPPER);
            entries.add(SPLOTTER_HOPPER);
        });
    }

}
