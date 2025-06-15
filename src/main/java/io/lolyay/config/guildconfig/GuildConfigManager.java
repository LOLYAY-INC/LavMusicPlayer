package io.lolyay.config.guildconfig;

import io.lolyay.utils.Logger;

import java.util.HashMap;
import java.util.Map;

public class GuildConfigManager {
    private static final Map<String, GuildConfig> guildConfigMap = new HashMap<>();

    public static GuildConfig getGuildConfig(String guildId) {
        if (!guildConfigMap.containsKey(guildId))
            guildConfigMap.put(guildId, GuildConfigLoader.getGuildConfig(guildId));

        return guildConfigMap.get(guildId);
    }

    public static void saveGuildConfigCacheToFiles() {
        for (GuildConfig guildConfig : guildConfigMap.values()) {
            try {
                GuildConfigLoader.saveGuildConfig(guildConfig);
            } catch (Exception e) {
                Logger.err("Error saving guild config: " + e.getMessage());
            }
        }
    }


}
