package com.junioraww.glitterbox;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Thanks one guy!
public class Config {
    private YamlConfiguration config;
    private File file;

    public Config(File configFile) {
        this.config = new YamlConfiguration();
        this.file = configFile;
    }

    public boolean load() {
        try {
            if (!file.exists()) {
                Bukkit.getLogger().info("saving " + file.getName());
                Main.getInstance().saveResource(file.getName(), true);
            }
            config.load(file);
            save();
            return true;
        }
        catch (Exception e) {
            Bukkit.getLogger().severe("Config Failed to load, returned error:\n" + e.getMessage());
            return false;
        }
    }

    public static void copyFile(InputStream in, File out) throws Exception {
        try (InputStream fis = in; FileOutputStream fos = new FileOutputStream(out)) {
            byte[] buf = new byte[1024];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        }
    }

    public boolean save() {
        try {
            config.save(file);
            //Bukkit.getLogger().info("Saved " + this);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Config Failed to save, returned error: " + e.getMessage());
        }
        return true;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

}
