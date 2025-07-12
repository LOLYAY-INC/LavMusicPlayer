package io.lolyay.music.lavalink;

import com.github.topi314.lavasrc.ytdlp.YtdlpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.container.MediaContainerRegistry;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.getyarn.GetyarnAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.nico.NicoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.yamusic.YandexMusicAudioSourceManager;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.clients.*;
import io.lolyay.LavMusicPlayer;
import io.lolyay.config.ConfigManager;
import io.lolyay.music.sources.AdditionalSourcesManager;
import io.lolyay.music.sources.dlp.YoutubeDlpDownloader;
import io.lolyay.utils.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class LavaInitializer  {
    private static YoutubeType DEFAULT_YOUTUBE_TYPE = YoutubeType.YOUTUBE_SOURCE;
    public static String YTDLP_PATH = new YoutubeDlpDownloader().getOS().binaryName;

    public void init() {
        setup();

    }

    private void setup() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

        // Configure output format to match Player class requirements: 48kHz, 16-bit, stereo, signed, little-endian PCM
        playerManager.getConfiguration().setOutputFormat(new Pcm16AudioDataFormat(2, 48000, 960, false));
        playerManager.getConfiguration().setOpusEncodingQuality(10);
        playerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);

        registerSourceManagers(playerManager);

        LavMusicPlayer.playerManager = new LavaLinkPlayerManager(playerManager, new LavaPlayerFactory(playerManager));
    }

    private void registerSourceManagers(AudioPlayerManager playerManager) {
        playerManager.registerSourceManager(new YandexMusicAudioSourceManager(true));
        playerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new BeamAudioSourceManager());
        playerManager.registerSourceManager(new GetyarnAudioSourceManager());
        playerManager.registerSourceManager(new NicoAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager(MediaContainerRegistry.DEFAULT_REGISTRY));
        setupYoutube(playerManager);

        new AdditionalSourcesManager().setup(playerManager);

    }

    private void setupYoutube(AudioPlayerManager playerManager) {
        if(DEFAULT_YOUTUBE_TYPE == YoutubeType.YTDLP) {
            Logger.log("Using yt-dlp");
            if(!Files.exists(Path.of(YTDLP_PATH))) {
                Logger.warn("yt-dlp not found, downloading...");
                try {
                    new YoutubeDlpDownloader().downloadYtDlp(new YoutubeDlpDownloader().getOS());
                } catch (IOException e) {
                    Logger.err("Failed to download yt-dlp: " + e.getMessage());
                    Logger.err("Falling back to youtube source");
                    DEFAULT_YOUTUBE_TYPE = YoutubeType.YOUTUBE_SOURCE;
                    setupYoutubeSource(playerManager);
                    return;
                }
            }


            YtdlpAudioSourceManager source = new YtdlpAudioSourceManager(YTDLP_PATH);
            playerManager.registerSourceManager(source);
        } else {
            setupYoutubeSource(playerManager);
        }




    }

    private void setupYoutubeSource(AudioPlayerManager playerManager){
        Logger.log("Using youtube source");

        YoutubeAudioSourceManager source = new YoutubeAudioSourceManager(true, true, true,
                new MusicWithThumbnail(), new AndroidMusicWithThumbnail(), new AndroidVrWithThumbnail(), new WebWithThumbnail(),
                new WebEmbeddedWithThumbnail(), new MWebWithThumbnail(),
                new TvHtml5EmbeddedWithThumbnail());

        //OAUTH2
        String token = ConfigManager.getConfig().getAdditionalSources().getYoutubeOauth2RefreshToken();
        if (token == null || token.isBlank()) {
            Logger.err("No refresh token found, will need to log in again");
            source.useOauth2(null, false);
        } else {
            Logger.success("Loaded refresh token for youtube oauth2 : " + token);
            source.useOauth2(token, true);
        }
        playerManager.registerSourceManager(source);
    }

    public enum YoutubeType{
        YTDLP,
        YOUTUBE_SOURCE
    }

    public void swapYoutubeType(AudioPlayerManager playerManager, YoutubeType type){
        unRegisterYoutube(playerManager);

        DEFAULT_YOUTUBE_TYPE = type;
        setupYoutube(playerManager);
    }


    private void unRegisterYoutube(AudioPlayerManager playerManager) {
        if(DEFAULT_YOUTUBE_TYPE == YoutubeType.YOUTUBE_SOURCE) {
            playerManager.getSourceManagers().removeIf(manager -> manager instanceof YoutubeAudioSourceManager);
        }
        else {
            playerManager.getSourceManagers().removeIf(manager -> manager instanceof YtdlpAudioSourceManager);
        }
    }


}
