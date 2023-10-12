package me.gravityio.varhopper;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VarHopperMod implements ModInitializer {
    public static String MOD_ID = "varhopper";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static boolean IS_DEBUG = false;

    @Override
    public void onInitialize() {
        IS_DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();
        ModBlocks.init();
        ModItems.init();
        ModBlockEntities.init();
    }

    public static void DEBUG(String message, Object... args) {
        if (!IS_DEBUG) {
            return;
        }

        LOGGER.info(message, args);
    }
}
