package io.lolyay.music.lavalink;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class LavaPlayerFactory {
    private final AudioPlayerManager playerManager;
    public static AudioPlayer player;


    public LavaPlayerFactory(AudioPlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public AudioPlayer getOrCreatePlayer() {
        if (player != null) return player;
        player = playerManager.createPlayer();
        LavaEventTranslator trackScheduler = new LavaEventTranslator();
        player.addListener(trackScheduler);
        return player;
    }

}
