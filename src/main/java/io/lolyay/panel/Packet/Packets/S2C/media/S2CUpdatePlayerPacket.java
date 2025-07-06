package io.lolyay.panel.Packet.Packets.S2C.media;

import com.google.gson.annotations.Expose;
import io.lolyay.JdaMain;
import io.lolyay.musicbot.MusicManager;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.panel.Packet.AbstractPacket;
import io.lolyay.panel.Packet.S2CPacket;
import io.lolyay.utils.Logger;

public class S2CUpdatePlayerPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public MusicAudioTrack current;
    @Expose
    public int volume;
    @Expose
    public boolean paused;
    @Expose
    public boolean playing;
    @Expose
    public boolean headless;

    public S2CUpdatePlayerPacket() {
        this.current = MusicManager.getInstance().getCurrentTrack();
        this.volume = (int) MusicManager.getInstance().getVolume();
        this.paused = MusicManager.getInstance().isPaused();
        this.playing = MusicManager.getInstance().isPlaying();
        this.headless = JdaMain.headlessMode.isHeadless;
    }

    @Override
    public int getOpcode() {
        return 200;
    }
}
