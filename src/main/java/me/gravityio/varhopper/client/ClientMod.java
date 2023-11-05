package me.gravityio.varhopper.client;

import me.gravityio.varhopper.screen.ModScreens;
import net.fabricmc.api.ClientModInitializer;

public class ClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModScreens.init();
    }
}
