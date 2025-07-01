package io.lolyay.customevents.events.music;

import dev.arbjerg.lavalink.client.LavalinkNode;
import dev.arbjerg.lavalink.protocol.v4.Message;
import io.lolyay.customevents.Event;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

public class TrackEndedEvent extends Event {

    private final MusicAudioTrack track;
    private final Message.EmittedEvent.TrackEndEvent.AudioTrackEndReason endReason;
    private final long guildId;
    private final LavalinkNode node;

    public TrackEndedEvent(MusicAudioTrack track, Message.EmittedEvent.TrackEndEvent.AudioTrackEndReason endReason, long guildId, LavalinkNode node) {
        this.track = track;
        this.endReason = endReason;
        this.guildId = guildId;
        this.node = node;
    }

    public MusicAudioTrack getTrack() {
        return track;
    }

    public Message.EmittedEvent.TrackEndEvent.AudioTrackEndReason getEndReason() {
        return endReason;
    }

    public long getGuildId() {
        return guildId;
    }

    public LavalinkNode getNode() {
        return node;
    }
}
