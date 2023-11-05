package me.gravityio.varhopper.screen;

import me.gravityio.varhopper.VarHopperMod;
import me.gravityio.varhopper.screen.SlopperScreen;
import me.gravityio.varhopper.screen.SlopperScreenHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreens {
    public static final ScreenHandlerType<SlopperScreenHandler> SLOPPER_SCREEN = register(
            new ScreenHandlerType<>(SlopperScreenHandler::new, FeatureFlags.VANILLA_FEATURES),
            "slopper_hopper",
            SlopperScreen::new
    );

    public static void init() {
    }

    private static <T extends ScreenHandler, U extends Screen & ScreenHandlerProvider<T>> ScreenHandlerType<T> register(ScreenHandlerType<T> type,
                                                                                                                        String name,
                                                                                                                        HandledScreens.Provider<T, U> provider) {
        VarHopperMod.DEBUG("[ModScreens] Registering screen handler {}", name);
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(VarHopperMod.MOD_ID, name), type);
        HandledScreens.register(type, provider);
        return type;
    }
}
