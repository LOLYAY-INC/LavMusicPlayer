package io.lolyay.panel.Packet.Packets.S2C.media;

import com.google.gson.annotations.Expose;
import io.lolyay.LavMusicPlayer;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.S2CPacket;

public class S2CUpdatePlayerPositionPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public long position;
    @Expose
    public long duration;

    public S2CUpdatePlayerPositionPacket() {
        if(LavMusicPlayer.playerManager.getPlayerFactory().getOrCreatePlayer().getPlayingTrack() == null) {
            this.position = 0;
            return;
        }
        this.position = LavMusicPlayer.playerManager.getPlayerFactory().getOrCreatePlayer().getPlayingTrack().getPosition();
        this.duration = LavMusicPlayer.playerManager.getPlayerFactory().getOrCreatePlayer().getPlayingTrack().getDuration();

    }

    @Override
    public int getOpcode() {
        return 202;
    }
}
