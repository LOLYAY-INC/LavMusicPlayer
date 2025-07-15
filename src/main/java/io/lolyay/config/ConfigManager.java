package io.lolyay.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lolyay.utils.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    public static String CONFIG_FILE = "config.json";
    private static AppConfig config;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Loads the configuration from the config file
     * @throws IOException if there's an error reading the config file
     */
    public static void loadConfig() throws IOException {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            createDefaultConfig(configFile);
            Logger.err("Config file created. Please edit it and restart the application.");
            System.exit(1);
        }

        try (FileReader reader = new FileReader(configFile)) {
            config = gson.fromJson(reader, AppConfig.class);
        } catch (Exception e) {
            Logger.err("Failed to load config file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves the current configuration to the config file
     * @throws IOException if there's an error writing to the config file
     */
    public static void saveConfig() throws IOException {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(config, writer);
        }
    }

    /**
     * Gets the application configuration
     * @return the application configuration
     */
    public static AppConfig getConfig() {
        if (config == null) {
            throw new IllegalStateException("Configuration not loaded. Call loadConfig() first.");
        }
        return config;
    }

    /**
     * Creates a default configuration file
     * @param configFile the file to create
     * @throws IOException if there's an error creating the file
     */
    private static void createDefaultConfig(File configFile) throws IOException {
        AppConfig defaultConfig = new AppConfig();
        AppConfig.MusicConfig musicConfig = new AppConfig.MusicConfig();
        defaultConfig.setMusic(musicConfig);

        AppConfig.AdditionalSourcesConfig additionalSourcesConfig = new AppConfig.AdditionalSourcesConfig();
        defaultConfig.setAdditionalSources(additionalSourcesConfig);

        AppConfig.LyricsConfig lyricsConfig = new AppConfig.LyricsConfig();
        defaultConfig.setLyrics(lyricsConfig);

        AppConfig.PanelConfig panelConfig = new AppConfig.PanelConfig();
        defaultConfig.setPanel(panelConfig);

        AppConfig.SoundConfig soundConfig = new AppConfig.SoundConfig();
        defaultConfig.setSound(soundConfig);


        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(defaultConfig, writer);
        }
    }
}
