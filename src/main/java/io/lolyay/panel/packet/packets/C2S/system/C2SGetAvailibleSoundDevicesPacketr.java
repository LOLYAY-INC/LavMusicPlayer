package io.lolyay.panel.packet.packets.C2S.system;

import io.lolyay.music.output.OpenAlPlayer;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.C2SPacket;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.packets.S2C.system.S2CGetAvalibleDevicesPacket;
import org.java_websocket.WebSocket;

public class C2SGetAvailibleSoundDevicesPacketr extends AbstractPacket implements C2SPacket {
    @Override
    public void recivePacket(WebSocket socket) {
        String[] devices = OpenAlPlayer.getAvailableDevices().toArray(new String[0]);
        PacketHandler.sendPacket(socket, new S2CGetAvalibleDevicesPacket(devices));
    }

    @Override
    public int getOpcode() {
        return 501;
    }
}
