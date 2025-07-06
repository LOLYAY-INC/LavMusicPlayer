package io.lolyay.panel.Packet.Packets.C2S.img;

import com.google.gson.annotations.Expose;
import io.lolyay.JdaMain;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.img.S2CFetchImagePacket;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CSearchTrackPacket;
import io.lolyay.utils.ImageFetcher;
import org.java_websocket.WebSocket;

public class C2SFetchImagePacket extends AbstractPacket implements C2SPacket {
    @Expose
    public String url;

    @Override
    public void recivePacket(WebSocket socket) {
        PacketHandler.sendPacket(socket, new S2CFetchImagePacket(url, ImageFetcher.fetchImageAsBase64(url)));
    }

    @Override
    public int getOpcode() {
        return 401;
    }

}
