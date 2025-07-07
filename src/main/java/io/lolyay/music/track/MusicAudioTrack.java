package io.lolyay.music.track;

import com.google.gson.annotations.Expose;
import com.sedmelluq.discord.lavaplayer.tools.io.ByteBufferInputStream;
import com.sedmelluq.discord.lavaplayer.tools.io.MessageInput;
import com.sedmelluq.discord.lavaplayer.tools.io.MessageOutput;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.lolyay.LavMusicPlayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;

public class MusicAudioTrack {
    @Expose
    private TrackInfo trackInfo;
    @Expose
    private String encoded;

    private AudioTrack audioTrack;
    @Expose
    private long startTime;

    public MusicAudioTrack(AudioTrack track) {
        this.audioTrack = track;
        this.trackInfo = new TrackInfo(track.getInfo().title, track.getInfo().author, track.getInfo().artworkUrl.replace("w1000-h1000","w60-h60-l90-rj"),track.getDuration());
        this.encoded = encodeToString(encodeTrack(track));
    }

    public static MusicAudioTrack ofEncoded(String encoded2) {
        byte[] decoded = encodeFromString(encoded2);

        AudioTrack audioTrack1 = null;
        try {
            audioTrack1 = LavMusicPlayer.playerManager.getAudioPlayerManager().decodeTrack(new MessageInput(new ByteBufferInputStream(ByteBuffer.wrap(decoded)))).decodedTrack;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new MusicAudioTrack(audioTrack1);
    }

    public String getEncoded() {
        return encoded;
    }

    public TrackInfo trackInfo() {
        return trackInfo;
    }

    public void trackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }

    public AudioTrack audioTrack() {
        return audioTrack;
    }

    public long startTime() {
        return startTime;
    }

    public void startTime(long timestamp) {
        this.startTime = timestamp;
    }

    public void audioTrack(AudioTrack audioTrack) {
        this.audioTrack = audioTrack;
    }

    public static byte[] encodeFromString(String encoded) {
        if (encoded == null) {
            throw new IllegalArgumentException("Input String cannot be null.");
        }
        return Base64.getDecoder().decode(encoded);
    }

    private byte[] encodeTrack(AudioTrack audioTrack) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            LavMusicPlayer.playerManager.getAudioPlayerManager().encodeTrack(new MessageOutput(outputStream), audioTrack);
        } catch (IOException e) {
            // It's good practice to log the exception or handle it appropriately.
            throw new RuntimeException("Failed to encode audio track", e);
        }
        return outputStream.toByteArray();
    }

    public static String encodeToString(byte[] input) {
        if (input == null) {
            throw new IllegalArgumentException("Input byte array cannot be null.");
        }
        return Base64.getEncoder().encodeToString(input);
    }

    public void fillInAudioTrack(){
        if(audioTrack == null){
            try {
                byte[] encoded = encodeFromString(this.encoded);
                audioTrack = LavMusicPlayer.playerManager.getAudioPlayerManager().decodeTrack(new MessageInput(new ByteBufferInputStream(ByteBuffer.wrap(encoded)))).decodedTrack;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}