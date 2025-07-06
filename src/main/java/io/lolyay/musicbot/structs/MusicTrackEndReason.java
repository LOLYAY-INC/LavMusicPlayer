package io.lolyay.musicbot.structs;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import dev.arbjerg.lavalink.protocol.v4.Message;

public enum MusicTrackEndReason {
    FINISHED(true),
    LOAD_FAILED(true),
    STOPPED(false),
    REPLACED(false),
    CLEANUP(false);

    public final boolean mayStartNext;

    MusicTrackEndReason(boolean mayStartNext) {
        this.mayStartNext = mayStartNext;
    }

    public static MusicTrackEndReason fromAudioTrackEndReason(AudioTrackEndReason reason) {
        return switch (reason) {
            case FINISHED -> FINISHED;
            case LOAD_FAILED -> LOAD_FAILED;
            case STOPPED -> STOPPED;
            case REPLACED -> REPLACED;
            case CLEANUP -> CLEANUP;
            default -> null;
        };
    }

    public static MusicTrackEndReason fromAudioTrackEndReason(Message.EmittedEvent.TrackEndEvent.AudioTrackEndReason reason) {
        return switch (reason) {
            case FINISHED -> FINISHED;
            case LOAD_FAILED -> LOAD_FAILED;
            case STOPPED -> STOPPED;
            case REPLACED -> REPLACED;
            case CLEANUP -> CLEANUP;
            default -> null;
        };
    }
    // yes, stupid
}
