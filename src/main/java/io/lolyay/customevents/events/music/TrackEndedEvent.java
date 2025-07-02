package io.lolyay.customevents.events.music;

import dev.arbjerg.lavalink.client.LavalinkNode;
import io.lolyay.customevents.Event;
import io.lolyay.musicbot.backendswapper.structs.MusicTrackEndReason;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

public class TrackEndedEvent extends Event {

    private final MusicAudioTrack track;
    private final MusicTrackEndReason endReason;
    private final long guildId;
    private final LavalinkNode node;

    public TrackEndedEvent(MusicAudioTrack track, MusicTrackEndReason endReason, long guildId, LavalinkNode node) {
        this.track = track;
        this.endReason = endReason;
        this.guildId = guildId;
        this.node = node;
    }

    public MusicAudioTrack getTrack() {
        return track;
    }

    public MusicTrackEndReason getEndReason() {
        return endReason;
    }

    public long getGuildId() {
        return guildId;
    }

    public LavalinkNode getNode() {
        return node;
    }
}
