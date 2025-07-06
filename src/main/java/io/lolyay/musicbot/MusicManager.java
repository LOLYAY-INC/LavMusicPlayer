package io.lolyay.musicbot;


import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.musicbot.abstracts.AbstractPlayerManager;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CTrackStoppedPacket;

/**
 * Manages all audio operations for a single Guild.
 * It holds the queue manager and is controlled by events from the PlayerManager.
 */
public class MusicManager {

    private final AbstractPlayerManager playerManager;
    private long volume;
    private static MusicManager instance;
    private boolean isPaused = false;

    // A flag to track the playing state to prevent race conditions.
    private volatile boolean isPlaying = false;

    public MusicManager(AbstractPlayerManager playerManager) {
        instance = this;
        this.playerManager = playerManager;

        this.volume = ConfigManager.getConfig().getMusic().getVolume();
    }

    /**
     * Adds a track to the queue. If nothing is playing, it starts playback.
     * @param track The track to add.
     */
    public void playTrack(MusicAudioTrack track) {
        // queManager.queueTrack returns true if the queue was empty before adding.
        this.isPlaying = true;
        playerManager.playTrack(track);
    }


    /**
     * Stops the current track, clears the queue, and resets the player state.
     */
    public void stop() {
        this.isPlaying = false;
        playerManager.stop();
    }

    public void pause() {
        playerManager.pause();
        isPaused = true;
    }

    public void resume() {
        playerManager.resume();
        isPaused = false;
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public boolean isPaused(){
        return isPaused;
    }



    /**
     * This is the core logic that runs when a track ends.
     * It's called by the event listener registered in the PlayerManager.
     */
    public void onTrackEnd() {
            this.isPlaying = false;
            if(!JdaMain.headlessMode.isHeadless)
                PacketHandler.broadcastPacket(new S2CTrackStoppedPacket());
    }

    // --- Delegated Methods to QueManager ---
    // These methods simply pass the call through to the QueManager, providing a clean API.

    public void setVolume(long volume) {
        this.volume = volume;
        playerManager.setVolume((int) volume);
    }
    public long getVolume() { return this.volume; }

    public static MusicManager getInstance() {
        return instance;
    }

    public MusicAudioTrack getCurrentTrack() {
        if(JdaMain.playerManager.getPlayerFactory().getOrCreatePlayer().getPlayingTrack() == null) return null;
        return new MusicAudioTrack(JdaMain.playerManager.getPlayerFactory().getOrCreatePlayer().getPlayingTrack());
    }
}