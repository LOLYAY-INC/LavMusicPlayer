package io.lolyay.config;

import io.lolyay.Main;
import io.lolyay.utils.Logger;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Map;

public class ConfigLoader {
    private static final String CONFIG_FILE_NAME = "settings.yml";
    private static final InputStream CONFIG_TEMPLATE = Main.class.getResourceAsStream("/" + CONFIG_FILE_NAME);




    public static void load() throws FileNotFoundException {
        createConfigIfNotExists();
        Logger.log("Loading config file...");
        ConfigManager.loadConfig(YamlParser.loadYaml(new FileInputStream(CONFIG_FILE_NAME)));
    }


    private static void createConfigIfNotExists() {
        if (!Path.of(CONFIG_FILE_NAME).toFile().exists()) {
            createConfig();
            Logger.err("Config file created. Please edit it and restart the bot.");
            System.exit(1);
        }
    }

    private static void createConfig() {
        // read template into file
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(CONFIG_FILE_NAME));
            writer.write(new String(CONFIG_TEMPLATE.readAllBytes()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
