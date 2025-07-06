package io.lolyay.panel.Packet.Packets.C2S.media;

import com.google.gson.annotations.Expose;
import com.sedmelluq.discord.lavaplayer.tools.io.MessageInput;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.lolyay.JdaMain;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.media.S2CUpdatePlayerPacket;
import io.lolyay.panel.Packet.Packets.S2C.utilpackets.S2CSuccessPacket;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.io.StringBufferInputStream;
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

        JdaMain.musicManager.playTrack(track);

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
