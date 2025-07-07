package io.lolyay.panel.packet.packets.S2C;

import com.google.gson.annotations.Expose;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.S2CPacket;

public class S2CHelloPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public String message = "Hi!";


    @Override
    public int getOpcode() {
        return 1;
    }

}
