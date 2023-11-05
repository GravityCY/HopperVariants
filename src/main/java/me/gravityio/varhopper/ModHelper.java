package me.gravityio.varhopper;

import me.gravityio.varhopper.item.ModItemTags;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class ModHelper {

    public static boolean isReplacableHopper(ItemStack stack) {
        return stack.getItem() instanceof BlockItem && stack.isIn(ModItemTags.REPLACABLE_HOPPERS);
    }

    public static Block getReplaceableHopper(ItemStack stack) {
        if (!isReplacableHopper(stack)) {
            return null;
        }

        return ((BlockItem) stack.getItem()).getBlock();
    }

}
