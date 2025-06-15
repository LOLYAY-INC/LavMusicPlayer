package io.lolyay.config.guildconfig;

import io.lolyay.config.jsonnodes.JsonNode;
import io.lolyay.musicbot.queue.RepeatMode;

import java.util.Objects;

public final class GuildConfig {
    private String guildId;
    private RepeatMode repeatMode;
    private int volume;
    private int plays;
    private JsonNode node; //TODO


    public GuildConfig(String guildId, RepeatMode repeatMode, int volume, int plays, JsonNode node) {
        this.guildId = guildId;
        this.repeatMode = repeatMode;
        this.volume = volume;
        this.plays = plays;
        this.node = node;
    }

    public String guildId() {
        return this.guildId;
    }

    public GuildConfig guildId(String guildId) {
        this.guildId = guildId;
        return this;
    }

    public RepeatMode repeatMode() {
        return this.repeatMode;
    }

    public GuildConfig repeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
        return this;
    }

    public int volume() {
        return this.volume;
    }

    public GuildConfig volume(int volume) {
        this.volume = volume;
        return this;
    }

    public int plays() {
        return this.plays;
    }

    public GuildConfig plays(int plays) {
        this.plays = plays;
        return this;
    }

    public JsonNode node() {
        return this.node;
    }

    public GuildConfig node(JsonNode node) {
        this.node = node;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuildConfig that = (GuildConfig) o;
        return volume == that.volume &&
                plays == that.plays &&
                Objects.equals(guildId, that.guildId) &&
                repeatMode == that.repeatMode &&
                Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildId, repeatMode, volume, plays, node);
    }

    @Override
    public String toString() {
        return "GuildConfig{" +
                "guildId='" + guildId + '\'' +
                ", repeatMode=" + repeatMode +
                ", volume=" + volume +
                ", plays=" + plays +
                ", node=" + node +
                '}';
    }
}