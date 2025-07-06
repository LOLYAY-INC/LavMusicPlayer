package io.lolyay.musicbot;

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

    public void setup(AudioPlayerManager playerManager) {
        setupDeezer(playerManager);
        setupTidal(playerManager);
        setupSpotify(playerManager);
        setupAppleMusic(playerManager);
    }


    private void setupSpotify(AudioPlayerManager playerManager) {
        if (ConfigManager.getConfig().getAdditionalSources().getSpotifyClientId().isEmpty() || ConfigManager.getConfig().getAdditionalSources().getSpotifyClientSecret().isEmpty()) {
            Logger.err("You need to set your spotify client id and secret!");
            return;
        }
        Function<Void, AudioPlayerManager> sourceManagerSupplier = (pm) -> playerManager;
        var spotifySourceManager = new SpotifySourceManager(
                null,
                ConfigManager.getConfig().getAdditionalSources().getSpotifyClientId(),
                ConfigManager.getConfig().getAdditionalSources().getSpotifyClientSecret(),
                ConfigManager.getConfig().getMusic().getCountryCode(),
                sourceManagerSupplier,
                new DefaultMirroringAudioTrackResolver(new String[]{})
        );
        playerManager.registerSourceManager(spotifySourceManager);
        Logger.debug("Registered spotify source manager");
    }

    private void setupAppleMusic(AudioPlayerManager playerManager) {
        if (ConfigManager.getConfig().getAdditionalSources().getAppleMusicToken().isEmpty()) {
            Logger.err("You need to set your apple music token!");
            return;
        }
        Function<Void, AudioPlayerManager> sourceManagerSupplier = (pm) -> playerManager;
        AppleMusicSourceManager appleMusicSourceManager = new AppleMusicSourceManager(
                null,
                ConfigManager.getConfig().getAdditionalSources().getAppleMusicToken(),
                ConfigManager.getConfig().getMusic().getCountryCode(),
                sourceManagerSupplier
        );
        playerManager.registerSourceManager(appleMusicSourceManager);
        Logger.debug("Registered apple music source manager");
    }

    private void setupDeezer(AudioPlayerManager playerManager) {
        if (ConfigManager.getConfig().getAdditionalSources().getDeezerDecryptionKey().isEmpty() || ConfigManager.getConfig().getAdditionalSources().getDeezerArlCookie().isEmpty()) {
            Logger.err("You need to set your deezer decryption key and alr cookie!");
            return;
        }
        DeezerAudioSourceManager deezerAudioSourceManager = new DeezerAudioSourceManager(
                ConfigManager.getConfig().getAdditionalSources().getDeezerDecryptionKey(),
                ConfigManager.getConfig().getAdditionalSources().getDeezerArlCookie(),
                DeezerAudioTrack.TrackFormat.DEFAULT_FORMATS
        );
        playerManager.registerSourceManager(deezerAudioSourceManager);
        Logger.debug("Registered deezer source manager");
    }

    private void setupTidal(AudioPlayerManager playerManager) {
        if (ConfigManager.getConfig().getAdditionalSources().getTidalToken().isEmpty()) {
            Logger.err("You need to set your tidal token!");
            return;
        }
        Function<Void, AudioPlayerManager> sourceManagerSupplier = (pm) -> playerManager;
        TidalSourceManager tidalSourceManager = new TidalSourceManager(
                ConfigManager.getConfig().getMusic().getCountryCode(),
                sourceManagerSupplier,
                new DefaultMirroringAudioTrackResolver(new String[]{}),
                ConfigManager.getConfig().getAdditionalSources().getTidalToken()
        );
        playerManager.registerSourceManager(tidalSourceManager);
        Logger.debug("Registered tidal source manager");
    }

}
