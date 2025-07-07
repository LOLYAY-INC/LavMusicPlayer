package io.lolyay.panel.packet.packets.C2S.media;

import com.google.gson.annotations.Expose;
import io.lolyay.LavMusicPlayer;
import io.lolyay.music.track.MusicAudioTrack;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.C2SPacket;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.packets.S2C.media.S2CUpdatePlayerPacket;
import io.lolyay.panel.packet.packets.S2C.utilpackets.S2CSuccessPacket;
import org.java_websocket.WebSocket;

import java.util.Base64;

public class C2SPlayTrack extends AbstractPacket implements C2SPacket {
    @Expose
    public MusicAudioTrack track;

    @Override
    public void recivePacket(WebSocket socket) {
        track.fillInAudioTrack();

        if (track == null || track.audioTrack() == null) {
            PacketHandler.sendPacket(socket, new S2CSuccessPacket(getOpcode(), "Track is null"));
            return;
        }

        LavMusicPlayer.musicManager.playTrack(track);

        PacketHandler.sendPacket(socket, new S2CSuccessPacket(getOpcode(), track.trackInfo().title()));

        PacketHandler.broadcastPacket(new S2CUpdatePlayerPacket());

    }


    public static byte[] encodeFromString(String encoded) {
        if (encoded == null) {
            throw new IllegalArgumentException("Input String cannot be null.");
        }
        return Base64.getDecoder().decode(encoded);
    }


    @Override
    public int getOpcode() {
        return 112;
    }


}
