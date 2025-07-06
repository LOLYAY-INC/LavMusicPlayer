package io.lolyay.musicbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LavaPlayerFactory {
    private final AudioPlayerManager playerManager;
    private AudioPlayer player;


    public LavaPlayerFactory(AudioPlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public AudioPlayer getOrCreatePlayer() {
        if (this.player != null) return player;
        AudioPlayer player = playerManager.createPlayer();
        LavaTrackScheduler trackScheduler = new LavaTrackScheduler();
        player.addListener(trackScheduler);
        this.player = player;
        return player;
    }

}
