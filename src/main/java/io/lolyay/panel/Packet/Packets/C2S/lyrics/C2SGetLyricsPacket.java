package io.lolyay.panel.Packet.Packets.C2S.lyrics;

import com.google.gson.annotations.Expose;
import io.lolyay.musicbot.lyrics.getters.LyricsGetterManager;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.C2SPacket;
import io.lolyay.panel.Packet.PacketHandler;
import io.lolyay.panel.Packet.Packets.S2C.lyrics.S2CGetLyricsPacket;
import org.java_websocket.WebSocket;

public class C2SGetLyricsPacket extends AbstractPacket implements C2SPacket {
    @Expose
    public MusicAudioTrack track;

    @Override
    public void recivePacket(WebSocket socket) {
        LyricsGetterManager.getLyricsGetterForText(track.trackInfo().title())
                        .getLyrics(track.trackInfo().title() + " " + track.trackInfo().author()).thenAccept(lyrics -> {
                    PacketHandler.sendPacket(socket, new S2CGetLyricsPacket(track, lyrics));


                });


    }

    @Override
    public int getOpcode() {
        return 801;
    }

}