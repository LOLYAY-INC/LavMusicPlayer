package io.lolyay.panel.packet.packets.S2C.img;

import com.google.gson.annotations.Expose;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.S2CPacket;

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
