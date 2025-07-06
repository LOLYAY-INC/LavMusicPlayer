package io.lolyay.panel.Packet.Packets.S2C.media;

import com.google.gson.annotations.Expose;
import io.lolyay.JdaMain;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.S2CPacket;

public class S2CRequestLengthPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public long duration;
    @Expose
    public MusicAudioTrack track;

    public S2CRequestLengthPacket(long length, MusicAudioTrack track) {
        this.duration = length;
        this.track = track;

    }

    @Override
    public int getOpcode() {
        return 291;
    }
}
