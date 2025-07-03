package io.lolyay.musicbot.backendswapper.lavaplayer.search;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.customevents.events.search.PlaylistTrackLoadedEvent;
import io.lolyay.customevents.events.search.PreSearchEvent;
import io.lolyay.customevents.events.search.SearchNotFoundEvent;
import io.lolyay.customevents.events.search.SingleTrackLoadedEvent;
import io.lolyay.musicbot.GuildMusicManager;
import io.lolyay.musicbot.RequestorData;
import io.lolyay.musicbot.backendswapper.AbstractSearchManager;
import io.lolyay.musicbot.search.AbstractSearcher;
import io.lolyay.musicbot.search.PlaylistData;
import io.lolyay.musicbot.search.Search;
import io.lolyay.musicbot.search.searchers.DefaultSearcher;
import io.lolyay.musicbot.search.searchers.HttpSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeMusicSearcher;
import io.lolyay.musicbot.search.searchers.YoutubeSearcher;
import io.lolyay.musicbot.tracks.MusicAudioTrack;
import io.lolyay.utils.Logger;
import net.dv8tion.jda.api.entities.Member;

import java.util.Optional;
import java.util.function.Consumer;

public class LavaSearchManager extends AbstractSearchManager {

    // Default Search Order:
    // Internet (http) -> Youtube Music (ytm) -> Youtube (yt) -> Other (other)
    private final Class<? extends AbstractSearcher>[] searchers = new Class[]{
            HttpSearcher.class,
            YoutubeMusicSearcher.class,
            YoutubeSearcher.class,
            DefaultSearcher.class
    };

    public LavaSearchManager(long guildId) {
        super(guildId);
    }

    public void searchWithDefaultOrder(String query, Optional<Member> member, Consumer<Search> callback, long guildId) {
        GuildMusicManager guildMusicManager = JdaMain.playerManager.getGuildMusicManager(guildId);

        PreSearchEvent.SearchEventResult override = JdaMain.eventBus.postAndGet(new PreSearchEvent(guildId, query, callback, member.orElse(null))).getOverride();

        if (override != null) {
            Logger.debug("Search override: " + override.getStatus());
            switch (override.getStatus()) {
                case NOT_FOUND -> {
                    callback.accept(Search.wasNotFound(Search.SearchResult.NOT_FOUND("Search was not found (override)"), "System", query));
                    return;
                }
                case FOUND -> {
                    MusicAudioTrack track = MusicAudioTrack.fromTrack(override.getTrack().track(), RequestorData.fromMember(member, guildId));
                    callback.accept(Search.wasTrack(Search.SearchResult.SUCCESS("Track was found (override)"), "System", query, track));
                    return;
                }
                case ERROR -> {
                    callback.accept(Search.wasError(Search.SearchResult.ERROR("Search failed (override)"), "System", query));
                    return;
                }
                case NO_OVERRIDE -> {
                    break;
                }
                case CANCELLED -> {
                    callback.accept(Search.wasError(Search.SearchResult.ERROR("Search was cancelled (override)"), "System", query));
                    return;
                }
            }
        }

        LavaYoutubeSearcher.doSearch(query, member, callback, guildId, guildMusicManager, this);

    }

    protected static class LavaResultHandler implements AudioLoadResultHandler {
        private final Consumer<Search> callback;
        private final String query;
        private final long guildId;
        private final Optional<Member> member;
        private final AbstractSearcher source;

        public LavaResultHandler(Consumer<Search> callback, String query, long guildId, Optional<Member> member, AbstractSearcher source) {
            this.callback = callback;
            this.query = query;
            this.guildId = guildId;
            this.member = member;
            this.source = source;
        }

        @Override
        public void trackLoaded(AudioTrack audioTrack) {
            Logger.log("Loaded single track from " + source.getSourceName() + ": " + audioTrack.getInfo().title);
            MusicAudioTrack musicAudioTrack = MusicAudioTrack.fromTrack(audioTrack, RequestorData.fromMember(member, guildId));

            SingleTrackLoadedEvent event = JdaMain.eventBus.postAndGet(new SingleTrackLoadedEvent(callback, source, guildId, member, musicAudioTrack));

            event.getCallback().accept(Search.wasTrack(
                    Search.SearchResult.SUCCESS(), source.getSourceName(),
                    query,
                    musicAudioTrack
            ));
        }

        @Override
        public void playlistLoaded(AudioPlaylist audioPlaylist) {
            if (audioPlaylist.isSearchResult()) {
                trackSearch(audioPlaylist);
                return;
            }
            if (ConfigManager.getConfigBool("add-full-playlists-to-queue")) {
                Logger.debug("Treating Playlist as full playlist from " + source.getSourceName() + ": " + audioPlaylist.getName());
                RequestorData userData = RequestorData.fromMember(member, guildId);

                PlaylistData playlistData = PlaylistData.fromTracksAndInfo(audioPlaylist.getTracks(), audioPlaylist, userData);

                JdaMain.eventBus.post(new PlaylistTrackLoadedEvent(callback, source, guildId, member, playlistData.tracks()));

                callback.accept(Search.wasPlaylist(
                        Search.SearchResult.PLAYLIST(),
                        source.getSourceName(),
                        query,
                        playlistData
                ));


            } else {

                // No event because it will call handleTrackLoadedResult

                Logger.debug("Treating Playlist as single track from " + source.getSourceName() + ": " + audioPlaylist.getName());
                trackLoaded(audioPlaylist.getSelectedTrack() == null ? audioPlaylist.getTracks().getFirst() : audioPlaylist.getSelectedTrack());
            }
        }

        @Override
        public void noMatches() {
            JdaMain.eventBus.post(new SearchNotFoundEvent(guildId, query, callback, member.orElse(null), source));

            Logger.debug("Track not found from " + source.getSourceName() + ": " + query);

            callback.accept(Search.wasNotFound(
                    Search.SearchResult.NOT_FOUND(),
                    source.getSourceName(),
                    query
            ));
        }

        @Override
        public void loadFailed(FriendlyException e) {
            Logger.err("Error loading track from " + source.getSourceName() + ": " + e.getMessage());

            JdaMain.eventBus.post(new SearchNotFoundEvent(-1, query, callback, member.orElse(null), source));

            callback.accept(Search.wasError(
                    Search.SearchResult.ERROR(e.getMessage()),
                    source.getSourceName(),
                    query
            ));
        }

        private void trackSearch(AudioPlaylist audioPlaylist) {

            // here for future search selection???
            Logger.debug("Search result from " + source.getSourceName() + ": " + query);

            // No event because it will call handleTrackLoadedResult

            trackLoaded(audioPlaylist.getSelectedTrack() == null ? audioPlaylist.getTracks().getFirst() : audioPlaylist.getSelectedTrack());
        }
    }


}
