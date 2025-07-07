package io.lolyay.panel.packet.packets.S2C.media;

import com.google.gson.annotations.Expose;
import io.lolyay.LavMusicPlayer;
import io.lolyay.music.MusicManager;
import io.lolyay.music.track.MusicAudioTrack;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.S2CPacket;

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
        this.headless = LavMusicPlayer.headlessMode.isHeadless;
    }

    @Override
    public int getOpcode() {
        return 200;
    }
}
