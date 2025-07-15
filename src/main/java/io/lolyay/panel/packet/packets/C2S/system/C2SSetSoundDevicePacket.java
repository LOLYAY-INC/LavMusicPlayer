package io.lolyay.panel.packet.packets.C2S.system;

import com.google.gson.annotations.Expose;
import io.lolyay.config.ConfigManager;
import io.lolyay.music.output.OpenAlPlayer;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.C2SPacket;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.packets.S2C.system.S2CGetAvalibleDevicesPacket;
import org.java_websocket.WebSocket;

public class C2SSetSoundDevicePacket extends AbstractPacket implements C2SPacket {
    @Expose
    private String device;
    @Override
    public void recivePacket(WebSocket socket) {
        ConfigManager.getConfig().getSound().setDefaultOutputName(device);
        OpenAlPlayer.INSTANCE.changeDevice(device);
    }

    @Override
    public int getOpcode() {
        return 502;
    }
}
