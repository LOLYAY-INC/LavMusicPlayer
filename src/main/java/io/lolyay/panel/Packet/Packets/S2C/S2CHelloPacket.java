package io.lolyay.panel.Packet.Packets.S2C;

import com.google.gson.annotations.Expose;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.S2CPacket;

public class S2CHelloPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public String message = "Hi!";


    @Override
    public int getOpcode() {
        return 1;
    }

}
