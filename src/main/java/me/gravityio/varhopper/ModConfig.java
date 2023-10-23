package me.gravityio.varhopper;

import net.fabricmc.loader.api.FabricLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class ModConfig {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("varhopper.properties");
    public static ModConfig INSTANCE = new ModConfig();
    public int splotterCooldown = 2;
    public int echoCooldown = 1;
    public int superCooldown = 2;
    public int hyperCooldown = 4;
    public int slopperCooldown = 16;

    public void load() {
        try {
            var props = new Properties();
            props.load(new FileInputStream(PATH.toFile()));
            INSTANCE.splotterCooldown = Integer.parseInt(props.getProperty("splotterCooldown"));
            INSTANCE.echoCooldown = Integer.parseInt(props.getProperty("echoCooldown"));
            INSTANCE.superCooldown = Integer.parseInt(props.getProperty("superCooldown"));
            INSTANCE.hyperCooldown = Integer.parseInt(props.getProperty("hyperCooldown"));
            INSTANCE.slopperCooldown = Integer.parseInt(props.getProperty("slopperCooldown"));
            VarHopperMod.DEBUG("[ModConfig] Loaded config");
        } catch (FileNotFoundException e) {
            VarHopperMod.DEBUG("[ModConfig] FileNotFoundException, saving config");
            this.save();
        } catch (IOException e) {
            VarHopperMod.DEBUG("[ModConfig] Failed to load config (IOException)");
        }
    }

    public void save() {
        try {
            var props = new Properties();
            props.setProperty("splotterCooldown", String.valueOf(this.splotterCooldown));
            props.setProperty("echoCooldown", String.valueOf(this.echoCooldown));
            props.setProperty("superCooldown", String.valueOf(this.superCooldown));
            props.setProperty("hyperCooldown", String.valueOf(this.hyperCooldown));
            props.setProperty("slopperCooldown", String.valueOf(this.slopperCooldown));
            VarHopperMod.DEBUG("[ModConfig] Saving config");
            props.store(new FileOutputStream(PATH.toFile()), null);
        } catch (IOException e) {
            VarHopperMod.DEBUG("[ModConfig] Failed to save config");
        }
    }
}
