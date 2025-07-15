package io.lolyay.music.formats;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.transcoder.AudioChunkDecoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Audio chunk decoder for 32-bit float PCM data.
 * This class is designed to mirror the structure of PcmChunkDecoder for compatibility.
 */
public class FloatPcmChunkDecoder implements AudioChunkDecoder {
    private final ByteBuffer byteInput;
    private final FloatBuffer floatInput;

    /**
     * @param format    Source audio format. Must be a 32-bit float format.
     * @param bigEndian Whether the samples are in big-endian format.
     */
    public FloatPcmChunkDecoder(AudioDataFormat format, boolean bigEndian) {
        // Allocate an internal buffer to hold the raw byte data from one chunk.
        this.byteInput = ByteBuffer.allocate(format.maximumChunkSize());

        // Set the byte order once during construction.
        if (!bigEndian) {
            this.byteInput.order(ByteOrder.LITTLE_ENDIAN);
        }

        // Create a "view" of this byte buffer as a float buffer.
        // This allows us to read floats directly without manual byte manipulation.
        this.floatInput = this.byteInput.asFloatBuffer();
    }

    @Override
    public void decode(byte[] encoded, ShortBuffer output) {
        // 1. Clear the output buffer to prepare it for writing.
        output.clear();

        // 2. Clear our internal byte buffer and copy the incoming encoded data into it.
        byteInput.clear();
        byteInput.put(encoded);

        // 3. Reset the position of our float view and set its limit based on
        //    how many bytes were just put into the byte buffer (bytes / 4 bytes_per_float).
        floatInput.clear();
        floatInput.limit(byteInput.position() / 4);

        // 4. Loop through the available floats in the view buffer.
        while (floatInput.hasRemaining()) {
            // Read one 32-bit float sample.
            float floatSample = floatInput.get();

            // Scale the float sample from [-1.0, 1.0] to the 16-bit short range.
            // Clamp the value to prevent overflow/underflow before casting.
            short shortSample = (short) Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, floatSample * 32767.0f));

            // Put the resulting 16-bit sample into the output buffer.
            output.put(shortSample);
        }

        // 5. Flip the output buffer to prepare it for being read by Lavaplayer's filters.
        output.flip();
    }

    @Override
    public void close() {
        // Nothing to close here.
    }
}