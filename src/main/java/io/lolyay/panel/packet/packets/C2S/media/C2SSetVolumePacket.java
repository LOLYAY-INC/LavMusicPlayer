package io.lolyay.panel.packet.packets.C2S.media;

import com.google.gson.annotations.Expose;
import io.lolyay.LavMusicPlayer;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.C2SPacket;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.packets.S2C.media.S2CUpdatePlayerPacket;
import io.lolyay.panel.packet.packets.S2C.utilpackets.S2CSuccessPacket;
import org.java_websocket.WebSocket;


public class C2SSetVolumePacket extends AbstractPacket implements C2SPacket {
    @Expose
    public int volume;

    @Override
    public void recivePacket(WebSocket socket) {

        LavMusicPlayer.musicManager.setVolume(volume);

        PacketHandler.sendPacket(socket, new S2CSuccessPacket(getOpcode(), String.valueOf(volume)));
        PacketHandler.broadcastPacket(new S2CUpdatePlayerPacket());

    }

    @Override
    public int getOpcode() {
        return 117;
    }


}
