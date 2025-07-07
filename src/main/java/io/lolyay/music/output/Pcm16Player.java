package io.lolyay.music.output;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;

public class Pcm16Player {
    public static Pcm16Player INSTANCE;


    private final AudioPlayer audioPlayer;
    private volatile boolean sending = false;
    private Thread audioThread;

    public Pcm16Player(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        INSTANCE = this;
    }

    private ByteBuffer get20MsAudio() {
        AudioFrame frame = audioPlayer.provide();
        if (frame != null) {
            return ByteBuffer.wrap(frame.getData());
        }
        return null;
    }

    public void startSending(){
        if (sending) {
            return;
        }

        // IMPORTANT: The AudioPlayer must be configured to output PCM data for this to work.
        // This is typically done on the AudioPlayerManager configuration:
        // playerManager.getConfiguration().setOutputFormat(StandardAudioDataFormats.PCM_S16_LE);
        // The format is 48000Hz, 16-bit, stereo, signed, little-endian PCM.
        AudioFormat format = new AudioFormat(48000, 16, 2, true, false);
        SourceDataLine line;

        try {
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
                    // Track has ended
                    sending = false;
                }
                // If provide() returns null but a track is playing, it's buffering.
                // The next call will block, so no sleep is needed.
            }
            line.drain();
            line.close();
        });
        audioThread.setDaemon(true);
        audioThread.setName("Local-Audio-Output-Thread");
        audioThread.start();
    }

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
