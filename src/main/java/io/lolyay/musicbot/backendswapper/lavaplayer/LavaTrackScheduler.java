package io.lolyay.musicbot.backendswapper.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.customevents.events.music.TrackEndedEvent;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.backendswapper.structs.MusicTrackEndReason;
import io.lolyay.musicbot.lyrics.live.SyncedLyricsPlayer;
import io.lolyay.utils.Logger;

public class LavaTrackScheduler extends AudioEventAdapter {
    @Override
    public void onPlayerPause(AudioPlayer player) {


    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        long guildId = ((LavaLinkPlayerManager) JdaMain.playerManager).getPlayerFactory().guildIdFromPlayer(player);
        JdaMain.playerManager.getGuildMusicManager(guildId).getQueue().getFirst().startTime(System.currentTimeMillis() - Integer.parseInt(ConfigManager.getConfig("live-lyrics-ping-compensation")));
        SyncedLyricsPlayer.adjustStartTime(guildId, System.currentTimeMillis() - Integer.parseInt(ConfigManager.getConfig("live-lyrics-ping-compensation")));
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        LavaPlayerFactory factory = ((LavaLinkPlayerManager) JdaMain.playerManager).getPlayerFactory();
        long guildId = factory.guildIdFromPlayer(player);
        Logger.debug("TrackEndEvent for guild " + guildId);
        GuildMusicManager musicManager = JdaMain.playerManager.getGuildMusicManager(guildId);

        if (musicManager.getQueManager().isEmpty()) return;

        JdaMain.eventBus.post(new TrackEndedEvent(musicManager.getQueue().getFirst(), MusicTrackEndReason.fromAudioTrackEndReason(endReason), guildId, null));

        if (endReason.mayStartNext) {
            musicManager.onTrackEnd();
        }

    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        // An already playing track threw an exception (track end event will still be received separately)
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
    }
}
