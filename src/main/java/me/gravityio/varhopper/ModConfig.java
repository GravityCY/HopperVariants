package me.gravityio.varhopper;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class ModConfig {
    private static final String SLOPPER_PATH = "slopperCooldown";
    private static final String BLAZE_PATH = "blazeCooldown";
    private static final String PHANTOM_PATH = "phantomCooldown";
    private static final String ECHO_PATH = "echoCooldown";
    private static final String SPLOTTER_PATH = "splotterCooldown";
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("varhopper.properties");
    public static ModConfig INSTANCE = new ModConfig();
    public int splotterCooldown = 2;
    public int echoCooldown = 1;
    public int phantomCooldown = 2;
    public int blazeCooldown = 4;
    public int slopperCooldown = 16;

    static <T> T getOrDefault(@Nullable T value, T defaultValue) {
        if (value == null)
            return defaultValue;
        return value;
    }

    static Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void load() {
        try {
            var props = new Properties();
            props.load(new FileInputStream(PATH.toFile()));
            INSTANCE.slopperCooldown = getOrDefault(parseInt(props.getProperty(SLOPPER_PATH)), 16);
            INSTANCE.blazeCooldown = getOrDefault(parseInt(props.getProperty(BLAZE_PATH)), 4);
            INSTANCE.phantomCooldown = getOrDefault(parseInt(props.getProperty(PHANTOM_PATH)), 2);
            INSTANCE.echoCooldown = getOrDefault(parseInt(props.getProperty(ECHO_PATH)), 1);
            INSTANCE.splotterCooldown = getOrDefault(parseInt(props.getProperty(SPLOTTER_PATH)), 16);
            VarHopperMod.DEBUG("[ModConfig] Loaded config");
        } catch (FileNotFoundException e) {
            VarHopperMod.DEBUG("[ModConfig] FileNotFoundException, saving config");
        } catch (IOException e) {
            VarHopperMod.DEBUG("[ModConfig] Failed to load config (IOException)");
        }
        this.save();
    }

    public void save() {
        try {
            var props = new Properties();
            props.setProperty(SLOPPER_PATH, String.valueOf(this.slopperCooldown));
            props.setProperty(BLAZE_PATH, String.valueOf(this.blazeCooldown));
            props.setProperty(PHANTOM_PATH, String.valueOf(this.phantomCooldown));
            props.setProperty(ECHO_PATH, String.valueOf(this.echoCooldown));
            props.setProperty(SPLOTTER_PATH, String.valueOf(this.splotterCooldown));
            VarHopperMod.DEBUG("[ModConfig] Saving config");
            props.store(new FileOutputStream(PATH.toFile()), null);
        } catch (IOException e) {
            VarHopperMod.DEBUG("[ModConfig] Failed to save config");
        }
    }
}
