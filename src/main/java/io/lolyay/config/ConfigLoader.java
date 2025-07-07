package io.lolyay.config;

import io.lolyay.utils.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Handles loading and managing the application configuration.
 * This class is now a simple wrapper around ConfigManager for backward compatibility.
 */
public class ConfigLoader {
    /**
     * Loads the configuration from the config file.
     * @throws IOException if there's an error reading the config file
     */
    public static void load() throws IOException {
        Logger.debug("Loading configuration...");
        ConfigManager.loadConfig();
        Logger.debug("Configuration loaded successfully");
    }

    public static void load(String configFile) throws IOException {
        ConfigManager.CONFIG_FILE = configFile;
        load();
    }

    /**
     * Forces the creation of a new config file with default values.
     * @return true if the config file was created successfully, false otherwise
     */
    public static boolean forceCreateNewConfig() {
        try {
            File configFile = new File("config.json");
            if (configFile.exists() && !configFile.delete()) {
                Logger.err("Failed to delete existing config file");
                return false;
            }
            ConfigManager.loadConfig();
            return true;
        } catch (IOException e) {
            Logger.err("Failed to create new config file: " + e.getMessage());
            return false;
        }
    }
}
