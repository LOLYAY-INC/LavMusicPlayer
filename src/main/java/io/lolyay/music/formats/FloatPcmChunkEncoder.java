package io.lolyay.music.formats;


import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.transcoder.AudioChunkEncoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * An AudioChunkEncoder that converts 16-bit PCM samples into 32-bit float PCM samples.
 */
public class FloatPcmChunkEncoder implements AudioChunkEncoder {
    private final ByteBuffer outputBuffer;
    private final FloatBuffer outputAsFloat;

    /**
     * @param format    The target audio format (must be a float format).
     * @param bigEndian Whether the output samples should be big-endian.
     */
    public FloatPcmChunkEncoder(AudioDataFormat format, boolean bigEndian) {
        this.outputBuffer = ByteBuffer.allocate(format.maximumChunkSize());
        this.outputBuffer.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        this.outputAsFloat = this.outputBuffer.asFloatBuffer();
    }

    @Override
    public byte[] encode(ShortBuffer inputBuffer) {
        inputBuffer.mark();
        outputBuffer.clear();
        outputAsFloat.clear();

        // Convert each 16-bit short sample to a 32-bit float sample.
        while (inputBuffer.hasRemaining()) {
            // Scale the short's range [-32768, 32767] to the float's range [-1.0, 1.0].
            float floatSample = inputBuffer.get() / 32768.0f;
            outputAsFloat.put(floatSample);
        }

        // Set the byte buffer's limit to how many floats were written (times 4 bytes per float).
        outputBuffer.limit(outputAsFloat.position() * 4);

        byte[] encodedBytes = new byte[outputBuffer.remaining()];
        outputBuffer.get(encodedBytes);

        inputBuffer.reset();
        return encodedBytes;
    }

    @Override
    public void encode(ShortBuffer inputBuffer, ByteBuffer out) {
        // This method is less commonly used, but we implement it for completeness.
        byte[] encoded = encode(inputBuffer);
        out.put(encoded);
        out.flip();
    }

    @Override
    public void close() {
        // Nothing to close.
    }
}