package io.lolyay.events.listeners.lavalink;

import dev.arbjerg.lavalink.client.event.TrackEndEvent;
import io.lolyay.JdaMain;
import io.lolyay.customevents.EventListener;
import io.lolyay.customevents.events.music.TrackEndedEvent;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.backendswapper.structs.MusicTrackEndReason;
import io.lolyay.utils.Logger;

public class OnTrackEnd {
    @EventListener
    public void onTrackEnd(TrackEndEvent event) {
        Logger.debug("TrackEndEvent for guild " + event.getGuildId());
        GuildMusicManager musicManager = JdaMain.playerManager.getGuildMusicManager(event.getGuildId());

        if (musicManager.getQueManager().isEmpty()) return;

        JdaMain.eventBus.post(new TrackEndedEvent(musicManager.getQueue().getFirst(), MusicTrackEndReason.fromAudioTrackEndReason(event.getEndReason()), event.getGuildId(), event.getNode()));

        if (event.getEndReason().getMayStartNext()) {
            musicManager.onTrackEnd();
        }
    }
}
