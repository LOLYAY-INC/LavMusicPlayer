package io.lolyay.config.guildconfig;

import com.google.gson.Gson;
import io.lolyay.config.ConfigManager;
import io.lolyay.config.jsonnodes.JsonNode;
import io.lolyay.musicbot.queue.RepeatMode;
import io.lolyay.utils.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GuildConfigLoader {
    private static final String GUILD_CONFIG_FOLDER_NAME = "guildconfigs";
    public static void init(){
        try {
            Files.createDirectories(Path.of(GUILD_CONFIG_FOLDER_NAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GuildConfig getGuildConfig(String guildId) {
        Path guildConfigPath = getGuildConfigPath(guildId);
        if (!Files.exists(guildConfigPath)) {
            try {
                createGuildConfig(new GuildConfig(guildId, RepeatMode.OFF, Integer.parseInt(ConfigManager.getConfig("default-volume")), 0, null));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        String guildConfigJson = "";
        try {
            guildConfigJson = new String(Files.readAllBytes(guildConfigPath));
            return new Gson().fromJson(guildConfigJson, GuildConfig.class);
        } catch (IOException e) {
            Logger.err("Error loading guild config: " + e.getMessage());
            return new GuildConfig(guildId, RepeatMode.OFF, 0, 0, new JsonNode("localhost", "password", "localhost", 8080, false));
        }
    }

    public static void saveGuildConfig(GuildConfig guildConfig) throws Exception {
        Path guildConfigPath = getGuildConfigPath(guildConfig.guildId());
        String guildConfigJson = new Gson().toJson(guildConfig);
        Files.write(guildConfigPath, guildConfigJson.getBytes());
        Logger.debug("Saved guild config: " + guildConfigPath);
    }

    private static void createGuildConfig(GuildConfig guildConfig) throws Exception {
        Path guildConfigPath = getGuildConfigPath(guildConfig.guildId());
        String guildConfigJson = new Gson().toJson(guildConfig);
        Files.write(guildConfigPath, guildConfigJson.getBytes());
        Logger.debug("Created guild config: " + guildConfigPath);
    }

    private static Path getGuildConfigPath(String guildId) {
        return Path.of(GUILD_CONFIG_FOLDER_NAME, guildId + ".json");
    }

}
