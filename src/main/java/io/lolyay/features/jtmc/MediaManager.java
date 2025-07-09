package io.lolyay.features.jtmc;

import io.github.selemba1000.*;
import io.lolyay.eventbus.EventBus;
import io.lolyay.events.media.MediaPauseEvent;
import io.lolyay.events.media.MediaPlayEvent;
import io.lolyay.events.media.MediaStopEvent;
import io.lolyay.music.track.MusicAudioTrack;
import io.lolyay.music.track.TrackInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class MediaManager {

    private final JMTC jmtc;
    private final EventBus eventBus;
    private Path tempArtworkPath = null;

    public MediaManager(EventBus eventBus, String playerName, String desktopFileName) {
        this.eventBus = eventBus;
        this.jmtc = JMTC.getInstance(new JMTCSettings(playerName, desktopFileName));

        setupCallbacks();
        jmtc.setEnabled(true);
        jmtc.setMediaType(JMTCMediaType.Music);
    }

    private void setupCallbacks() {
        JMTCCallbacks callbacks = new JMTCCallbacks();
        callbacks.onPlay = () -> eventBus.post(new MediaPlayEvent());
        callbacks.onPause = () -> eventBus.post(new MediaPauseEvent());
        callbacks.onStop = () -> eventBus.post(new MediaStopEvent());
        // You can also add handlers for next, previous, etc.
        // callbacks.onNext = () -> eventBus.post(new MediaNextEvent());
        // callbacks.onPrevious = () -> eventBus.post(new MediaPreviousEvent());

        jmtc.setCallbacks(callbacks);

        // Enable the buttons you want to be available in the media controls
        jmtc.setEnabledButtons(new JMTCEnabledButtons(
                true, // Play
                true, // Pause
                true, // Stop
                false, // Next
                false  // Previous
        ));
    }

    public void started(MusicAudioTrack musicTrack) {
        TrackInfo trackInfo = musicTrack.trackInfo();
        String artworkUrl = trackInfo.artWorkUrl();
        File artworkFile = null;
        
        if (artworkUrl != null && !artworkUrl.isEmpty()) {
            try {
                // Download the artwork to a temporary file
                URL url = new URL(artworkUrl);
                tempArtworkPath = Files.createTempFile("artwork-", ".tmp");
                if (tempArtworkPath != null) {
                    try {
                        Files.deleteIfExists(tempArtworkPath);
                    } catch (IOException e) {
                        System.err.println("Failed to delete temporary artwork file: " + e.getMessage());
                    }
                }
                try (InputStream in = url.openStream()) {
                    Files.copy(in, tempArtworkPath, StandardCopyOption.REPLACE_EXISTING);
                    artworkFile = tempArtworkPath.toFile();
                }
            } catch (IOException e) {
                System.err.println("Failed to download or save artwork: " + e.getMessage());
                e.printStackTrace();
            }
        }

        JMTCMusicProperties properties = new JMTCMusicProperties(
                trackInfo.title(),
                trackInfo.author(),
                "", // Album Title
                "", // Album Artist
                new String[]{}, // Genres
                0, // Track Number
                1, // Album Track Count
                artworkFile // File object for the artwork
        );
        jmtc.setMediaProperties(properties);
        jmtc.setPlayingState(JMTCPlayingState.PLAYING);
        jmtc.updateDisplay();
    }

    public void paused() {
        jmtc.setPlayingState(JMTCPlayingState.PAUSED);
        jmtc.updateDisplay();
    }

    public void stopped() {
        jmtc.setPlayingState(JMTCPlayingState.STOPPED);
        // Clear properties when stopped
        jmtc.setMediaProperties(new JMTCMusicProperties("", "", "", "", new String[]{}, 0, 0, null));
        jmtc.updateDisplay();
        cleanupArtwork();
    }

    private String getArtworkPath(String urlString) {
        if (urlString == null || urlString.isEmpty()) {
            return null;
        }
        cleanupArtwork(); // Clean up old artwork before downloading new
        try {
            URL url = new URL(urlString);
            try (InputStream in = url.openStream()) {
                // Create a temporary file for the artwork
                tempArtworkPath = Files.createTempFile("artwork-", ".tmp");
                Files.copy(in, tempArtworkPath, StandardCopyOption.REPLACE_EXISTING);
                return tempArtworkPath.toAbsolutePath().toString();
            }
        } catch (IOException e) {
            System.err.println("Failed to download or save artwork: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void cleanupArtwork() {
        if (tempArtworkPath != null) {
            try {
                Files.deleteIfExists(tempArtworkPath);
            } catch (IOException e) {
                System.err.println("Failed to delete temporary artwork file: " + e.getMessage());
            } finally {
                tempArtworkPath = null;
            }
        }
    }
}