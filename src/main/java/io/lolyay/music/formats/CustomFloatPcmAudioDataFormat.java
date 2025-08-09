package io.lolyay.music.formats; // Or whatever package you prefer

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.transcoder.AudioChunkDecoder;
import com.sedmelluq.discord.lavaplayer.format.transcoder.AudioChunkEncoder;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * A custom AudioDataFormat for 32-bit floating-point PCM.
 * It uses a custom encoder and decoder to convert 16-bit PCM samples into 32-bit float PCM samples.
 */
public class CustomFloatPcmAudioDataFormat extends AudioDataFormat {
    private final boolean bigEndian;
    private final byte[] silenceBytes;

    /**
     * @param channelCount     Number of channels.
     * @param sampleRate       Sample rate (frequency).
     * @param chunkSampleCount Number of samples in one chunk.
     * @param bigEndian        Whether the samples are big-endian.
     */
    public CustomFloatPcmAudioDataFormat(int channelCount, int sampleRate, int chunkSampleCount, boolean bigEndian) {
        super(channelCount, sampleRate, chunkSampleCount);
        this.bigEndian = bigEndian;
        this.silenceBytes = new byte[channelCount * chunkSampleCount * 4];
    }

    @Override
    public String codecName() {
        return bigEndian ? "PCM_FLOAT32_BE" : "PCM_FLOAT32_LE";
    }

    @Override
    public byte[] silenceBytes() {
        return silenceBytes;
    }

    @Override
    public int expectedChunkSize() {
        return silenceBytes.length;
    }

    @Override
    public int maximumChunkSize() {
        return silenceBytes.length;
    }

    @Override
    public AudioChunkDecoder createDecoder() {
        return new FloatPcmChunkDecoder(this,bigEndian);
    }

    @Override
    public AudioChunkEncoder createEncoder(AudioConfiguration configuration) {
        return new FloatPcmChunkEncoder(this, bigEndian);
    }
}