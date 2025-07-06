package io.lolyay.panel.Packet.Packets.S2C.media;

import com.google.gson.annotations.Expose;
import io.lolyay.JdaMain;
import io.lolyay.musicbot.MusicManager;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.S2CPacket;

public class S2CUpdatePlayerPositionPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public long position;
    @Expose
    public long duration;

    public S2CUpdatePlayerPositionPacket() {
        if(JdaMain.playerManager.getPlayerFactory().getOrCreatePlayer().getPlayingTrack() == null) {
            this.position = 0;
            return;
        }
        this.position = JdaMain.playerManager.getPlayerFactory().getOrCreatePlayer().getPlayingTrack().getPosition();
        this.duration = JdaMain.playerManager.getPlayerFactory().getOrCreatePlayer().getPlayingTrack().getDuration();

    }

    @Override
    public int getOpcode() {
        return 202;
    }
}
