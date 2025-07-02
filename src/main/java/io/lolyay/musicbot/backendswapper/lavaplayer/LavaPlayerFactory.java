package io.lolyay.musicbot.backendswapper.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LavaPlayerFactory {
    private final AudioPlayerManager playerManager;
    private final Map<Long, AudioPlayer> players = new ConcurrentHashMap<>();

    public LavaPlayerFactory(AudioPlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public AudioPlayer getOrCreatePlayer(long guildId) {
        if (players.containsKey(guildId)) {
            return players.get(guildId);
        }

        AudioPlayer player = playerManager.createPlayer();
        LavaTrackScheduler trackScheduler = new LavaTrackScheduler();
        player.addListener(trackScheduler);
        players.put(guildId, player);
        return player;
    }

    public void removePlayer(long guildId) {
        players.remove(guildId);
    }


    public long guildIdFromPlayer(AudioPlayer lavaLinkPlayerManager) {
        return players.entrySet().stream().filter(entry -> entry.getValue().equals(lavaLinkPlayerManager)).findFirst().get().getKey();
    }
}
