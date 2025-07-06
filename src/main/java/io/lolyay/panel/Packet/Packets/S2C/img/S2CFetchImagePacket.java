package io.lolyay.panel.Packet.Packets.S2C.img;

import com.google.gson.annotations.Expose;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.S2CPacket;
import org.java_websocket.WebSocket;

public class S2CFetchImagePacket extends AbstractPacket implements S2CPacket {
    @Expose
    public String base64;
    @Expose
    public String url;

    public S2CFetchImagePacket(String url,String base64) {
        this.base64 = base64;
        this.url = url;
    }

    @Override
    public int getOpcode() {
        return 401;
    }

}
