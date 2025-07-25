package io.lolyay.music.output;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import io.lolyay.utils.Logger;
import org.lwjgl.openal.*;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.charset.StandardCharsets;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.ALC_ALL_DEVICES_SPECIFIER;
import static org.lwjgl.openal.EXTFloat32.AL_FORMAT_STEREO_FLOAT32;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenAlPlayer {
    public static OpenAlPlayer INSTANCE;

    private static final int BUFFER_COUNT = 4;
    private static final int OPENAL_FORMAT = AL_FORMAT_STEREO_FLOAT32;
    private static final int FRAME_BYTE_SIZE = 7680;
    private String deviceName;

    private final AudioPlayer audioPlayer;
    private Thread audioThread;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public OpenAlPlayer(AudioPlayer audioPlayer, String deviceName) {
        this.audioPlayer = audioPlayer;
        this.deviceName = deviceName;
        INSTANCE = this;
    }

    public void changeDevice(String newDeviceName) {
        if (Objects.equals(this.deviceName, newDeviceName)) {
            Logger.debug("Attempted to change to the same device. No action taken.");
            return;
        }

        Logger.log("Changing audio output device to: " + (newDeviceName != null ? newDeviceName : "Default"));

        stopSending();

        this.deviceName = newDeviceName;

        if (audioPlayer.getPlayingTrack() != null) {
            startSending();
        }
    }

    public static List<String> getAvailableDevices() {
        if (!alcIsExtensionPresent(NULL, "ALC_ENUMERATION_EXT")) {
            Logger.warn("OpenAL Enumeration extension not available. Cannot list devices.");
            return Collections.emptyList();
        }

        return ALUtil.getStringList(NULL, ALC_ALL_DEVICES_SPECIFIER);

    }

    public void startSending() {
        if (running.get()) {
            Logger.log("startSending called while a player was already running. Stopping the old one first. this is expected for song changes.");
            stopSending();
        }

        Logger.debug("Starting new OpenAL player thread.");
        audioThread = new Thread(this::audioLoop, "OpenAL-Playback-Thread");
        running.set(true);
        audioThread.setDaemon(true);
        audioThread.start();
    }

    public void stopSending() {
        if (!running.compareAndSet(true, false)) {
            return;
        }

        Logger.debug("Stopping OpenAL player thread.");
        if (audioThread != null) {
            audioThread.interrupt();
            try {
                audioThread.join(500);
            } catch (InterruptedException e) {
                Logger.err("Failed to cleanly join OpenAL thread.");
                Thread.currentThread().interrupt();
            }
        }
    }

    private void audioLoop() {
        long device;
        if (deviceName != null) {
            Logger.debug("Opening specified OpenAL device: " + deviceName);
            device = alcOpenDevice(MemoryUtil.memUTF8(deviceName));
        } else {
            Logger.debug("Opening default OpenAL device.");
            device = alcOpenDevice((ByteBuffer) null);
        }

        if (device == NULL) {
            Logger.err("Failed to open OpenAL device. Device name: " + (deviceName != null ? deviceName : "Default"));
            return;
        }

        long context = alcCreateContext(device, (int[]) null);
        alcMakeContextCurrent(context);
        AL.createCapabilities(ALC.createCapabilities(device));

        final int source = alGenSources();

        final int[] buffers = new int[BUFFER_COUNT];
        for (int i = 0; i < BUFFER_COUNT; i++) {
            buffers[i] = alGenBuffers();
            checkAlError("Generate Buffer " + i);
        }

        final ByteBuffer directBuffer = ByteBuffer.allocateDirect(FRAME_BYTE_SIZE).order(ByteOrder.LITTLE_ENDIAN);

        try {
            for (int bufferId : buffers) {
                AudioFrame frame = audioPlayer.provide(1, java.util.concurrent.TimeUnit.SECONDS);
                if (frame == null) {
                    Logger.warn("Could not get initial frame. Track might fail to start.");
                    break;
                }
                directBuffer.clear();
                directBuffer.put(frame.getData());
                directBuffer.flip();
                alBufferData(bufferId, OPENAL_FORMAT, directBuffer, 48000);
                alSourceQueueBuffers(source, bufferId);
            }

            alSourcePlay(source);

            while (running.get()) {
                int processed = alGetSourcei(source, AL_BUFFERS_PROCESSED);
                for (int i = 0; i < processed; i++) {
                    int bufferId = alSourceUnqueueBuffers(source);
                    checkAlError("Unqueue Buffer");
                    AudioFrame frame = audioPlayer.provide();
                    if (frame != null) {
                        directBuffer.clear();
                        directBuffer.put(frame.getData());
                        directBuffer.flip();
                        alBufferData(bufferId, OPENAL_FORMAT, directBuffer, 48000);
                        checkAlError("Re-buffer Data");
                        alSourceQueueBuffers(source, bufferId);
                        checkAlError("Re-queue Buffer");
                    }
                }

                if (alGetSourcei(source, AL_SOURCE_STATE) != AL_PLAYING && alGetSourcei(source, AL_BUFFERS_QUEUED) > 0) {
                    alSourcePlay(source);
                }

                Thread.sleep(15);
            }
        } catch (InterruptedException e) {
            Logger.debug("OpenAL thread interrupted, shutting down.");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            Logger.err("Exception in OpenAL audio loop: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Logger.debug("Cleaning up OpenAL resources for this track.");
            alSourceStop(source);
            alDeleteSources(source);
            alDeleteBuffers(buffers);
            alcMakeContextCurrent(NULL);
            alcDestroyContext(context);
            alcCloseDevice(device);
        }
    }

    private static void checkAlError(String context) {
        int error = alGetError();
        if (error != AL_NO_ERROR) {
            throw new RuntimeException("OpenAL Error in " + context + ": " + alGetString(error));
        }
    }
}