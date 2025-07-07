package io.lolyay.panel.packet.packets.S2C.utilpackets;

import com.google.gson.annotations.Expose;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.S2CPacket;

public class S2CErrorPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public int packetOpcode;
    @Expose
    public String message;

    public S2CErrorPacket(int packetOpcode, String message) {
        this.packetOpcode = packetOpcode;
        this.message = message;
    }

    @Override
    public int getOpcode() {
        return -1;
    }
}
