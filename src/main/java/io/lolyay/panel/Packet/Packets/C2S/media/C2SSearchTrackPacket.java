package io.lolyay.panel.Packet.Packets.C2S.media;

import com.google.gson.annotations.Expose;
import io.lolyay.JdaMain;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CSearchTrackPacket;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class C2SSearchTrackPacket extends AbstractPacket implements C2SPacket {
    @Expose
    public String query;

    @Override
    public void recivePacket(WebSocket socket) {
        JdaMain.playerManager.searchWithDefaultOrderMultiple(query,searches -> {

                    PacketHandler.sendPacket(socket, new S2CSearchTrackPacket(query, searches));
                }
        );



    }

    @Override
    public int getOpcode() {
        return 111;
    }

}
