package io.lolyay.panel.packet.packets.S2C.youtube;

import com.google.gson.annotations.Expose;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.S2CPacket;

public class S2COauthRequiredPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public final String deviceCode;

    public S2COauthRequiredPacket(String deviceCode) {
        this.deviceCode = deviceCode;
    }


    @Override
    public int getOpcode() {
        return 710;
    }

}
