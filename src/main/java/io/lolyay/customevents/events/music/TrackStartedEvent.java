package io.lolyay.customevents.events.music;

import dev.arbjerg.lavalink.client.LavalinkNode;
import io.lolyay.customevents.Event;
import io.lolyay.musicbot.tracks.MusicAudioTrack;

public class TrackStartedEvent extends Event {

    private final MusicAudioTrack track;
    private final long guildId;
    private final LavalinkNode node;

    public TrackStartedEvent(MusicAudioTrack track, long guildId, LavalinkNode node) {
        this.track = track;
        this.guildId = guildId;
        this.node = node;
    }

    public MusicAudioTrack getTrack() {
        return track;
    }

    public long getGuildId() {
        return guildId;
    }

    public LavalinkNode getNode() {
        return node;
    }
}
