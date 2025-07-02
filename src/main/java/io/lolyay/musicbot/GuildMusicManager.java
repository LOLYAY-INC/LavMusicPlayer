package io.lolyay.musicbot;


import io.lolyay.JdaMain;
import io.lolyay.config.guildconfig.GuildConfig;
import io.lolyay.musicbot.backendswapper.AbstractPlayerManager;
import io.lolyay.musicbot.queue.QueManager;
import io.lolyay.musicbot.queue.RepeatMode;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

import java.util.List;

/**
 * Manages all audio operations for a single Guild.
 * It holds the queue manager and is controlled by events from the PlayerManager.
 */
public class GuildMusicManager {

    private final AbstractPlayerManager playerManager;
    private final QueManager queManager;
    private final long guildId;
    private long volume;
    private final GuildConfig config;
    private boolean isPaused = false;

    // A flag to track the playing state to prevent race conditions.
    private volatile boolean isPlaying = false;

    public GuildMusicManager(AbstractPlayerManager playerManager, long guildId, GuildConfig config) {
        this.playerManager = playerManager;
        this.guildId = guildId;
        this.queManager = new QueManager(config);
        this.queManager.init(); // Load settings like repeat mode
        this.config = config;
        this.volume = config.volume();
    }

    /**
     * Adds a track to the queue. If nothing is playing, it starts playback.
     * @param track The track to add.
     */
    public void queueTrack(MusicAudioTrack track) {
        // queManager.queueTrack returns true if the queue was empty before adding.
        boolean shouldStart = queManager.queueTrack(track);
        config.plays(config.plays() + 1);
        if (shouldStart) {
            startPlaying();
        }
    }

    /**
     * Starts playing the first track in the queue if not already playing.
     */
    public void startPlaying() {
        if (isPlaying || queManager.isEmpty()) {
            return; // Already playing or nothing to play
        }

        // get(0) gets the track at the head of the queue without removing it.
        MusicAudioTrack firstTrack = queManager.getQueue().get(0);
        this.isPlaying = true;
        playerManager.playTrack(firstTrack);
    }

    /**
     * Stops the current track, clears the queue, and resets the player state.
     */
    public void stop() {
        this.isPlaying = false;
        queManager.clear();
        playerManager.stop(guildId);
    }

    public void pause() {
        playerManager.pause(guildId);
        isPaused = true;
    }

    public void resume() {
        playerManager.resume(guildId);
        isPaused = false;
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public Long getGuildId() {
        return guildId;
    }

    public boolean isPaused(){
        return isPaused;
    }



    /**
     * Skips the current track and plays the next one in the queue.
     */
    public MusicAudioTrack skip() {
        MusicAudioTrack currentrack = queManager.getQueue().getFirst();
        MusicAudioTrack nextTrack = queManager.skip();
        if (nextTrack != null) {
            isPlaying = true;
            playerManager.playTrack(nextTrack);
            config.plays(config.plays() + 1);
            return currentrack;
        } else {
            // Skipped the last song, so stop everything.
            stop();
            return null; // No track to skip, Stopped
        }
    }

    /**
     * This is the core logic that runs when a track ends.
     * It's called by the event listener registered in the PlayerManager.
     */
    public void onTrackEnd() {
        // Get the next track based on the repeat mode
        MusicAudioTrack nextTrack = queManager.nextTrack();

        if (nextTrack != null) {
            // If there's a next track, play it. State remains isPlaying=true.
            playerManager.playTrack(nextTrack);
            config.plays(config.plays() + 1);
        } else {
            // The queue is now empty, so we are no longer playing.
            this.isPlaying = false;
            JdaMain.jda.getDirectAudioController().disconnect(JdaMain.jda.getGuildById(guildId));
        }
    }

    // --- Delegated Methods to QueManager ---
    // These methods simply pass the call through to the QueManager, providing a clean API.

    public void shuffle() { queManager.shuffle(); }
    public List<MusicAudioTrack> getQueue() { return queManager.getQueue(); }
    public void setRepeatMode(RepeatMode mode) { queManager.setRepeatMode(mode); }
    public RepeatMode getRepeatMode() { return queManager.getRepeatMode(); }

    public void setVolume(long volume) {
        this.volume = volume;
        this.config.volume((int) volume);
        playerManager.setVolume(this.guildId, (int) volume);
    }
    public long getVolume() { return this.volume; }
    public QueManager getQueManager() { return queManager; }
}