package me.gravityio.varhopper.item;

import me.gravityio.varhopper.VarHopperMod;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTags {
    public static final TagKey<Item> REPLACABLE_HOPPERS = of("replaceable_hoppers");

    public static TagKey<Item> of(String name) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(VarHopperMod.MOD_ID, name));
    }
}
