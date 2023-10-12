package me.gravityio.varhopper.mixins.impl;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HopperBlockEntity.class)
public interface HopperAccessor {


    @Invoker("canMergeItems")
    static boolean canMergeItems(ItemStack first, ItemStack second) {
        return false;
    }
}
