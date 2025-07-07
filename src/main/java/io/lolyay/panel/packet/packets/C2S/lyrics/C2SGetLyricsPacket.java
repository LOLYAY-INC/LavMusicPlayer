package io.lolyay.panel.packet.packets.C2S.lyrics;

import com.google.gson.annotations.Expose;
import io.lolyay.lyrics.getters.LyricsGetterManager;
import io.lolyay.music.track.MusicAudioTrack;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.C2SPacket;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.packets.S2C.lyrics.S2CGetLyricsPacket;
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