package io.lolyay.panel.packet.packets.C2S.media;

import com.google.gson.annotations.Expose;
import io.lolyay.LavMusicPlayer;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.C2SPacket;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.packets.S2C.media.S2CSearchTrackPacket;
import org.java_websocket.WebSocket;

public class C2SSearchTrackPacket extends AbstractPacket implements C2SPacket {
    @Expose
    public String query;

    @Override
    public void recivePacket(WebSocket socket) {
        LavMusicPlayer.playerManager.searchWithDefaultOrderMultiple(query, searches -> {

                    PacketHandler.sendPacket(socket, new S2CSearchTrackPacket(query, searches));
                }
        );



    }

    @Override
    public int getOpcode() {
        return 111;
    }

}
