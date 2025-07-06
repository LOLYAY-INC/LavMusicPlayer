package io.lolyay.panel.Packet.Packets.S2C.media;

import com.google.gson.annotations.Expose;
import io.lolyay.musicbot.search.Search;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.C2S.media.C2SSearchTrackPacket;
import io.lolyay.panel.Packet.S2CPacket;
import org.java_websocket.WebSocket;

public class S2CSearchTrackPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public String query;

    @Expose
    public Search[] results;

    public S2CSearchTrackPacket(String query, Search[] results) {
        this.query = query;
        this.results = results;
    }

    @Override
    public int getOpcode() {
        return 111;
    }
}
