package io.lolyay.panel.packet.packets.S2C.youtube;

import com.google.gson.annotations.Expose;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.S2CPacket;

public class S2COauthFinishPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public final boolean success;
    @Expose
    public final String refreshToken;

    public S2COauthFinishPacket(boolean success, String refreshToken) {
        this.success = success;
        this.refreshToken = refreshToken;
    }


    @Override
    public int getOpcode() {
        return 712;
    }

}
