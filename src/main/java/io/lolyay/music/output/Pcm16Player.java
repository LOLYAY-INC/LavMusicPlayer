package io.lolyay.music.output;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;

public class Pcm16Player {
    @Deprecated
    public static Pcm16Player INSTANCE;


    private final AudioPlayer audioPlayer;
    private volatile boolean sending = false;
    private Thread audioThread;

    @Deprecated
    public Pcm16Player(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        INSTANCE = this;
    }
    @Deprecated
    private ByteBuffer get20MsAudio() {
        AudioFrame frame = audioPlayer.provide();
        if (frame != null) {
            return ByteBuffer.wrap(frame.getData());
        }
        return null;
    }
    @Deprecated
    public void startSending() {
        if (sending) {
            return;
        }

        // The AudioFormat now needs to match the new 32-bit float format.
        // Format: 48000Hz, 32-bit, stereo, signed, little-endian FLOAT.
        AudioFormat format = new AudioFormat(
                48000,    // Sample rate
                24,       // Sample size in bits <-- THE KEY CHANGE
                2,        // Channels (stereo)
                true,     // Signed
                false     // Little-endian
        );

        SourceDataLine line;
        try {
            // ... (The rest of this class is the same as the previous suggestion)
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Audio line not supported for format: " + format);
                return;
            }
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        }

        line.start();
        sending = true;

        audioThread = new Thread(() -> {
            while (sending) {
                ByteBuffer audioBuffer = get20MsAudio();
                if (audioBuffer != null) {
                    byte[] audioData = audioBuffer.array();
                    line.write(audioData, 0, audioData.length);
                } else if (audioPlayer.getPlayingTrack() == null) {
                    sending = false;
                }
            }
            line.drain();
            line.close();
        });
        audioThread.setDaemon(true);
        audioThread.setName("Local-Audio-Output-Thread");
        audioThread.start();
    }
    @Deprecated
    public void stopSending() {
        sending = false;
        if (audioThread != null) {
            try {
                audioThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}
