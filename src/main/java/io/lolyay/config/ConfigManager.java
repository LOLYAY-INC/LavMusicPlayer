package io.lolyay.config;

import io.lolyay.utils.Logger;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static Map<String, Object> configMap = new HashMap<>();
    private static String version;

    public static String getConfig(String key) {
        return configMap.get(key).toString();
    }

    public static void loadConfig(Map<String, Object> configMap) {
        ConfigManager.configMap = configMap;
        if (configMap.get("version") == null) {
            Logger.err("Config file is not valid or is empty.");
            System.exit(1);
        }
        version = configMap.get("version").toString();
        Logger.log("Config loaded, version: " + version);
    }
}
