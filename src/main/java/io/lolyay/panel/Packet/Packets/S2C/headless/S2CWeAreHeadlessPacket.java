package io.lolyay.panel.Packet.Packets.S2C.headless;

import com.google.gson.annotations.Expose;
import io.lolyay.LavMusicPlayer;
import io.lolyay.musicbot.HeadlessMode;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.S2CPacket;

public class S2CWeAreHeadlessPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public HeadlessMode.HeadlessData data;

    public S2CWeAreHeadlessPacket() {
        data = new HeadlessMode.HeadlessData(
                LavMusicPlayer.headlessMode.tracks.toArray(new MusicAudioTrack[0]),
                LavMusicPlayer.headlessMode.repeatingType,
                LavMusicPlayer.headlessMode.currentIndex,
                LavMusicPlayer.headlessMode.playlistId
        );

    }


    @Override
    public int getOpcode() {
        return 903;
    }

}
