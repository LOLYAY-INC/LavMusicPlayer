package io.lolyay.musicbot.backendswapper.lavaplayer;

import com.github.topi314.lavasrc.applemusic.AppleMusicSourceManager;
import com.github.topi314.lavasrc.deezer.DeezerAudioSourceManager;
import com.github.topi314.lavasrc.deezer.DeezerAudioTrack;
import com.github.topi314.lavasrc.mirror.DefaultMirroringAudioTrackResolver;
import com.github.topi314.lavasrc.spotify.SpotifySourceManager;
import com.github.topi314.lavasrc.tidal.TidalSourceManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import io.lolyay.config.ConfigManager;
import io.lolyay.utils.Logger;

import java.util.function.Function;

public class AdditionalSourcesManager {

    private void setup(AudioPlayerManager playerManager) {

    }


    private void setupSpotify(AudioPlayerManager playerManager) {
        if (!ConfigManager.getConfigBool("enable-spotify")) return;
        if (ConfigManager.getConfig("spotify-client-id").equals("YOUR_SPOTIFY_CLIENT_ID") || ConfigManager.getConfig("spotify-client-secret").equals("YOUR_SPOTIFY_CLIENT_SECRET")) {
            Logger.err("You need to set your spotify client id and secret in the config file!");
            return;
        }
        Function<Void, AudioPlayerManager> sourceManagerSupplier = (pm) -> playerManager;
        var spotifySourceManager = new SpotifySourceManager(
                null,
                ConfigManager.getConfig("spotify-client-id"),
                ConfigManager.getConfig("spotify-client-secret"),
                ConfigManager.getConfig("country-code"),
                sourceManagerSupplier,
                new DefaultMirroringAudioTrackResolver(new String[]{})
        );
        playerManager.registerSourceManager(spotifySourceManager);
        Logger.debug("Registered spotify source manager");
    }

    private void setupAppleMusic(AudioPlayerManager playerManager) {
        if (!ConfigManager.getConfigBool("enable-apple-music")) return;
        if (ConfigManager.getConfig("apple-music-token").equals("YOUR_APPLE_MUSIC_TOKEN")) {
            Logger.err("You need to set your apple music token in the config file!");
            return;
        }
        Function<Void, AudioPlayerManager> sourceManagerSupplier = (pm) -> playerManager;
        AppleMusicSourceManager appleMusicSourceManager = new AppleMusicSourceManager(
                null,
                ConfigManager.getConfig("apple-music-token"),
                ConfigManager.getConfig("country-code"),
                sourceManagerSupplier
        );
        playerManager.registerSourceManager(appleMusicSourceManager);
        Logger.debug("Registered apple music source manager");
    }

    private void setupDeezer(AudioPlayerManager playerManager) {
        if (!ConfigManager.getConfigBool("enable-deezer")) return;
        if (ConfigManager.getConfig("deezer-decryption-key").equals("YOUR_DEEZER_DECRYPTION_KEY") || ConfigManager.getConfig("deezer-arl-cookie").equals("YOUR_DEEZER_ARL_COOKIE")) {
            Logger.err("You need to set your deezer decryption key and alr cookie in the config file!");
            return;
        }
        DeezerAudioSourceManager deezerAudioSourceManager = new DeezerAudioSourceManager(
                ConfigManager.getConfig("deezer-decryption-key"),
                ConfigManager.getConfig("deezer-arl-cookie"),
                DeezerAudioTrack.TrackFormat.DEFAULT_FORMATS
        );
        playerManager.registerSourceManager(deezerAudioSourceManager);
        Logger.debug("Registered deezer source manager");
    }

    private void setupTidal(AudioPlayerManager playerManager) {
        if (!ConfigManager.getConfigBool("enable-tidal")) return;
        if (ConfigManager.getConfig("tidal-token").equals("YOUR_TIDAL_TOKEN")) {
            Logger.err("You need to set your tidal token in the config file!");
            return;
        }
        Function<Void, AudioPlayerManager> sourceManagerSupplier = (pm) -> playerManager;
        TidalSourceManager tidalSourceManager = new TidalSourceManager(
                ConfigManager.getConfig("country-code"),
                sourceManagerSupplier,
                new DefaultMirroringAudioTrackResolver(new String[]{}),
                ConfigManager.getConfig("tidal-token")
        );
        playerManager.registerSourceManager(tidalSourceManager);
        Logger.debug("Registered tidal source manager");
    }

}
