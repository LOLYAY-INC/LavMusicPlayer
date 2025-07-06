package io.lolyay.panel.Packet.Packets.S2C.lyrics;

import com.google.gson.annotations.Expose;
import io.lolyay.musicbot.lyrics.records.Lyrics;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CUpdatePlayerPacket;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CUpdatePlayerPositionPacket;
import io.lolyay.panel.Packet.Packets.S2C.utilpackets.S2CSuccessPacket;
import io.lolyay.panel.Packet.S2CPacket;
import org.java_websocket.WebSocket;

public class S2CGetLyricsPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public MusicAudioTrack track;
    @Expose
    public Lyrics lyrics;

    public S2CGetLyricsPacket(MusicAudioTrack track, Lyrics lyrics) {
        this.track = track;
        this.lyrics = lyrics;
    }


    @Override
    public int getOpcode() {
        return 801;
    }
}

