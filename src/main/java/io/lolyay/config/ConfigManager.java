package io.lolyay.config;

import io.lolyay.utils.Logger;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static Map<String, Object> configMap = new HashMap<>();
    private static String version;

    public static String getConfig(String key) {
        String conf = configMap.get(key).toString();
        if (configMap.get(key) == null) {
            Logger.err("Config file is not valid or is empty, It may be out of date, please update it so it includes: " + key);
            System.exit(1);
        }
        if (conf.equals("${project.version}"))
            return "A development build"; // if it isnt a gh release
        return configMap.get(key).toString();
    }

    public static boolean getConfigBool(String key) {
        if (configMap.get(key) == null) {
            Logger.err("Config file is not valid or is empty, It may be out of date, please update it so it includes: " + key);
            System.exit(1);
        }
        return (boolean) configMap.get(key);
    }

    public static Object getConfigRaw(String key) {
        if (configMap.get(key) == null) {
            Logger.err("Config file is not valid or is empty, It may be out of date, please update it so it includes: " + key);
            System.exit(1);
        }
        return configMap.get(key);
    }

    public static void loadConfig(Map<String, Object> configMap) {
        ConfigManager.configMap = configMap;
        if (configMap.get("version") == null) {
            Logger.err("Config file is not valid or is empty.");
            System.exit(1);
        }
        version = getConfig("version");
        Logger.debug("Config loaded, version: " + version);
        Logger.log("Loaded Config, Bot version is " + version);
    }
}
