package io.lolyay.features.headless;

import com.google.gson.annotations.Expose;
import io.lolyay.LavMusicPlayer;
import io.lolyay.eventbus.EventListener;
import io.lolyay.eventbus.events.music.TrackEndedEvent;
import io.lolyay.music.MusicManager;
import io.lolyay.music.track.MusicAudioTrack;
import io.lolyay.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class HeadlessMode {
    public boolean isHeadless = false;
    public RepeatingType repeatingType = RepeatingType.REPEAT_ALL;
    public int currentIndex = 0;
    public ArrayList<MusicAudioTrack> tracks = new ArrayList<>();
    public String playlistId = "";

    public void enable(HeadlessData headlessData){
        isHeadless = true;
        repeatingType = headlessData.repeatingType;
        currentIndex = headlessData.currentIndex;
        playlistId = headlessData.playlistId;
        tracks = new ArrayList<>(List.of(headlessData.tracks));
        for(MusicAudioTrack track : tracks){
            track.fillInAudioTrack();
        }
        LavMusicPlayer.eventBus.register(this);
        Logger.warn("[HEADLESS] Headless mode enabled!");
    }

    public void disable(){
        if(isHeadless)
            LavMusicPlayer.eventBus.unregister(this);
        isHeadless = false;
    }



    public enum RepeatingType{
        SHUFFLE,
        REPEAT_ALL,
        REPEAT_ONE
    }

    public static class HeadlessData{
        @Expose
        public MusicAudioTrack[] tracks;
        @Expose
        public RepeatingType repeatingType;
        @Expose
        public int currentIndex;
        @Expose
        public String playlistId;

        public HeadlessData(MusicAudioTrack[] tracks, RepeatingType repeatingType, int currentIndex, String playlistId) {
            this.tracks = tracks;
            this.repeatingType = repeatingType;
            this.currentIndex = currentIndex;
            this.playlistId = playlistId;
        }
    }

    @EventListener
    public void onTrackEnd(TrackEndedEvent event){
        Logger.debug("Track ended!");
        if (isHeadless) {
            MusicManager.getInstance().playTrack(getNextTrack());
            Logger.warn("[HEADLESS] Playing next track...");
        }
    }

    private MusicAudioTrack getNextTrack() {
        if (tracks.isEmpty()) {
            return null;
        }
        
        return switch (repeatingType) {
            case SHUFFLE -> tracks.get((int) (Math.random() * tracks.size()));
            case REPEAT_ONE -> tracks.getFirst();
            case REPEAT_ALL -> {
                currentIndex = (currentIndex + 1) % tracks.size();
                yield tracks.get(currentIndex);
            }
        };
    }
}
