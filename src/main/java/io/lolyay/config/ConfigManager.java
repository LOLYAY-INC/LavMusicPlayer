package io.lolyay.config;

import io.lolyay.utils.Logger;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static Map<String, String> configMap = new HashMap<>();
    private static String version;

    public static String getConfig(String key) {
        return configMap.get(key);
    }

    public static void loadConfig(Map<String, String> configMap) {
        ConfigManager.configMap = configMap;
        if (configMap.get("version") == null) {
            Logger.err("Config file is not valid or is empty.");
            System.exit(1);
        }
        version = configMap.get("version");
        Logger.log("Config loaded, version: " + version);
    }
}
