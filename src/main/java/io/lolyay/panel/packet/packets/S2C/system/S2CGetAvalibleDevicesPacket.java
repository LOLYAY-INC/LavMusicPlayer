package io.lolyay.panel.packet.packets.S2C.system;

import com.google.gson.annotations.Expose;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.S2CPacket;

public class S2CGetAvalibleDevicesPacket extends AbstractPacket implements S2CPacket {
    @Expose
    private final String[] devices;

    public S2CGetAvalibleDevicesPacket(String[] devices) {
        this.devices = devices;
    }

    @Override
    public int getOpcode() {
        return 501;
    }
}
