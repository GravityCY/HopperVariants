package me.gravityio.varhopper.block;

import me.gravityio.varhopper.VarHopperMod;
import me.gravityio.varhopper.block.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block SLOPPER_HOPPER = register(new SlopperHopperBlock(AbstractBlock.Settings.copy(Blocks.HOPPER)), "slopper_hopper");
    public static final Block BLAZE_HOPPER = register(new BlazeHopperBlock(AbstractBlock.Settings.copy(Blocks.HOPPER)), "blaze_hopper");
    public static final Block PHANTOM_HOPPER = register(new PhantomHopperBlock(AbstractBlock.Settings.copy(Blocks.HOPPER)), "phantom_hopper");
    public static final Block ECHO_HOPPER = register(new EchoHopperBlock(AbstractBlock.Settings.copy(Blocks.HOPPER)), "echo_hopper");
    public static final Block SPLOTTER_HOPPER = register(new SplotterHopperBlock(AbstractBlock.Settings.copy(Blocks.HOPPER)), "splotter_hopper");

    public static Block register(Block block, String name) {
        VarHopperMod.DEBUG("[ModBlocks] Registering block {}", name);
        Registry.register(Registries.BLOCK, new Identifier(VarHopperMod.MOD_ID, name), block);
        return block;
    }

    public static void init() {

    }

}
