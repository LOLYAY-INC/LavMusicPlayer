package io.lolyay.features;

import io.lolyay.eventbus.EventListener;
import io.lolyay.events.api.packet.PrePacketSendEvent;
import io.lolyay.panel.packet.packets.S2C.media.S2CUpdatePlayerPacket;
import io.lolyay.utils.Logger;

import static io.lolyay.LavMusicPlayer.PLAYER_UPDATE_INTERVAL; // Assuming this is a constant like 500 (ms)

public class ErrorDetectSystem { // Renamed for clarity
    // --- Configuration ---
    private static final int MAX_PACKET_DRIFT = 200; // Max allowed deviation for packet timing
    private static final int MAX_SONG_DRIFT = 100;   // Max allowed deviation for song position
    private static final int ANALYSIS_INTERVAL = 12; // Number of player updates to analyze
    private static final int MAX_ERRORS_PER_INTERVAL = 8; // Threshold to trigger a warning
    private static final int WARN_MS_THRESHOLD = 700; // Warning threshold in milliseconds

    // --- State for Interval Analysis ---
    private int packetErrorCount = 0;
    private int songErrorCount = 0;
    private int updateCountInInterval = 0;
    private long totalPacketDrift = 0;
    private long totalSongDrift = 0;

    // --- State for Delta Calculation ---
    private long lastPacketTimestamp = 0;
    private long lastSongPosition = 0;

    public void onPlayerUpdate(PrePacketSendEvent event) {
        // We only care about the player update packet
        if (!(event.getPacket() instanceof S2CUpdatePlayerPacket)) {
            return;
        }

        S2CUpdatePlayerPacket packet = (S2CUpdatePlayerPacket) event.getPacket();

        // If there's no track playing, reset the state and do nothing.
        if (packet.current == null || packet.current.audioTrack() == null || !packet.playing || packet.paused) {
            resetState();
            return;
        }

        long currentTime = System.currentTimeMillis();

        // On the very first packet, we can't calculate a delta. Just initialize the state.
        if (lastPacketTimestamp == 0) {
            initializeState(currentTime, packet);
            return;
        }

        // --- Perform Checks and Update State ---
        checkPacketTiming(currentTime);
        checkSongPlayback(packet, currentTime);

        // Update the state for the *next* event
        lastPacketTimestamp = currentTime;
        lastSongPosition = packet.current.audioTrack().getPosition();

        // Check if our analysis interval is complete
        updateCountInInterval++;
        if (updateCountInInterval >= ANALYSIS_INTERVAL) {
            runIntervalAnalysis();
        }
    }

    /**
     * Checks if the time between packets is within the acceptable range.
     */
    private void checkPacketTiming(long currentTime) {
        long actualPacketDelta = currentTime - lastPacketTimestamp;
        long packetDrift = actualPacketDelta - PLAYER_UPDATE_INTERVAL;
        totalPacketDrift += packetDrift;

        if (Math.abs(packetDrift) > MAX_PACKET_DRIFT) {
            packetErrorCount++;
            if (Math.abs(packetDrift) > WARN_MS_THRESHOLD) {
                String direction = packetDrift > 0 ? "slow" : "fast";
                Logger.warn(String.format("Packet handler drift: %dms (running %s)", packetDrift, direction));
            }
        }
    }

    /**
     * Checks if the song's position has advanced correctly.
     */
    private void checkSongPlayback(S2CUpdatePlayerPacket packet, long currentTime) {
        long currentSongPosition = packet.current.audioTrack().getPosition();
        long actualSongDelta = currentSongPosition - lastSongPosition;

        // The expected delta is the actual time that passed between packets, not the ideal interval.
        // This is more accurate for detecting song-specific issues vs. packet-timing issues.
        long expectedSongDelta = currentTime - lastPacketTimestamp;
        long songDrift = actualSongDelta - expectedSongDelta;
        totalSongDrift += songDrift;

        if (Math.abs(songDrift) > MAX_SONG_DRIFT) {
            songErrorCount++;
            if (Math.abs(songDrift) > WARN_MS_THRESHOLD) {
                String direction = songDrift < 0 ? "slow (playback lag)" : "fast (skipped forward)";
                Logger.warn(String.format("Song position drift: %dms (running %s)", songDrift, direction));
            }
        }
    }

    /**
     * Analyzes the collected data for the interval and reports significant, persistent issues.
     */
    private void runIntervalAnalysis() {
        if (packetErrorCount >= MAX_ERRORS_PER_INTERVAL) {
            long averageDrift = totalPacketDrift / ANALYSIS_INTERVAL;
            Logger.err(String.format("Persistent Packet Handler slowdown/speedup detected! Average drift: %+dms over %d updates.", averageDrift, ANALYSIS_INTERVAL));
        }

        if (songErrorCount >= MAX_ERRORS_PER_INTERVAL) {
            long averageDrift = totalSongDrift / ANALYSIS_INTERVAL;
            Logger.err(String.format("Persistent Playback slowdown/speedup detected! Average drift: %+dms over %d updates.", averageDrift, ANALYSIS_INTERVAL));
            Logger.err("Restarting the track and seeking to the correct position...");
            // TODO: Implement the track restart/seek logic here.
        }

        // Reset for the next interval
        resetIntervalCounters();
    }

    // --- Helper Methods for State Management ---
    private void initializeState(long currentTime, S2CUpdatePlayerPacket packet) {
        lastPacketTimestamp = currentTime;
        lastSongPosition = packet.current.audioTrack().getPosition();
        resetIntervalCounters(); // Ensure counters are zeroed
    }

    private void resetState() {
        lastPacketTimestamp = 0;
        lastSongPosition = 0;
        resetIntervalCounters();
    }

    private void resetIntervalCounters() {
        updateCountInInterval = 0;
        packetErrorCount = 0;
        songErrorCount = 0;
        totalPacketDrift = 0;
        totalSongDrift = 0;
    }
}