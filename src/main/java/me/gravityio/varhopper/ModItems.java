package me.gravityio.varhopper;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item ECHO_HOPPER = register(new BlockItem(ModBlocks.ECHO_HOPPER, new FabricItemSettings()), "echo_hopper");
    public static final Item SUPER_HOPPER = register(new BlockItem(ModBlocks.SUPER_HOPPER, new FabricItemSettings()), "super_hopper");
    public static final Item HYPER_HOPPER = register(new BlockItem(ModBlocks.HYPER_HOPPER, new FabricItemSettings()), "hyper_hopper");
    public static final Item SLOPPER_HOPPER = register(new BlockItem(ModBlocks.SLOPPER_HOPPER, new FabricItemSettings()), "slopper_hopper");
    public static final Item SPLOTTER_HOPPER = register(new BlockItem(ModBlocks.SPLOTTER_HOPPER, new FabricItemSettings()), "splotter_hopper");

    private static Item register(Item item, String name) {
        VarHopperMod.DEBUG("[ModItems] Registering item {}", name);
        Registry.register(Registries.ITEM, new Identifier(VarHopperMod.MOD_ID, name), item);
        return item;
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
           entries.add(ECHO_HOPPER);
           entries.add(SUPER_HOPPER);
           entries.add(HYPER_HOPPER);
           entries.add(SLOPPER_HOPPER);
           entries.add(SPLOTTER_HOPPER);
        });
    }

}
