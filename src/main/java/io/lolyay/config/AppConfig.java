package io.lolyay.config;

import com.google.gson.annotations.SerializedName;

public class AppConfig {
    @SerializedName("discord")
    private MusicConfig music;

    @SerializedName("additional_sources")
    private AdditionalSourcesConfig additionalSources;

    @SerializedName("lyrics")
    private LyricsConfig lyrics;

    public LyricsConfig getLyrics() {
        return lyrics;
    }

    public void setLyrics(LyricsConfig lyrics) {
        this.lyrics = lyrics;
    }

    public MusicConfig getMusic() {
        return music;
    }

    public void setMusic(MusicConfig music) {
        this.music = music;
    }

    public AdditionalSourcesConfig getAdditionalSources() {
        return additionalSources;
    }
    public void setAdditionalSources(AdditionalSourcesConfig additionalSources) {
        this.additionalSources = additionalSources;
    }


    public static class MusicConfig {
        @SerializedName("volume")
        private int volume = 0;

        @SerializedName("country-code")
        private String countryCode = "";

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }
    }


    public static class AdditionalSourcesConfig {
        @SerializedName("volume")
        private int volume;

        @SerializedName("yt-oauth2-refresh-token")
        private String youtubeOauth2RefreshToken = "";


        @SerializedName("spotify-client-id")
        private String spotifyClientId = "";

        @SerializedName("spotify-client-secret")
        private String spotifyClientSecret = "";


        @SerializedName("apple-music-token")
        private String appleMusicToken = "";


        @SerializedName("tidal-token")
        private String tidalToken = "";


        @SerializedName("deezer-decryption-key")
        private String deezerDecryptionKey = "";

        @SerializedName("deezer-arl-cookie")
        private String deezerArlCookie = "";


        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public String getYoutubeOauth2RefreshToken() {
            return youtubeOauth2RefreshToken;
        }

        public void setYoutubeOauth2RefreshToken(String youtubeOauth2RefreshToken) {
            this.youtubeOauth2RefreshToken = youtubeOauth2RefreshToken;
        }

        public String getSpotifyClientId() {
            return spotifyClientId;
        }

        public void setSpotifyClientId(String spotifyClientId) {
            this.spotifyClientId = spotifyClientId;
        }

        public String getSpotifyClientSecret() {
            return spotifyClientSecret;
        }

        public void setSpotifyClientSecret(String spotifyClientSecret) {
            this.spotifyClientSecret = spotifyClientSecret;
        }

        public String getAppleMusicToken() {
            return appleMusicToken;
        }

        public void setAppleMusicToken(String appleMusicToken) {
            this.appleMusicToken = appleMusicToken;
        }

        public String getTidalToken() {
            return tidalToken;
        }

        public void setTidalToken(String tidalToken) {
            this.tidalToken = tidalToken;
        }

        public String getDeezerDecryptionKey() {
            return deezerDecryptionKey;
        }

        public void setDeezerDecryptionKey(String deezerDecryptionKey) {
            this.deezerDecryptionKey = deezerDecryptionKey;
        }

        public String getDeezerArlCookie() {
            return deezerArlCookie;
        }

        public void setDeezerArlCookie(String deezerArlCookie) {
            this.deezerArlCookie = deezerArlCookie;
        }
    }

    public static class LyricsConfig {
        @SerializedName("musicmatch-auth-token")
        private String musicmatchAuthToken = "";

        public String getMusicmatchAuthToken() {
            return musicmatchAuthToken;
        }

        public void setMusicmatchAuthToken(String musicmatchAuthToken) {
            this.musicmatchAuthToken = musicmatchAuthToken;
        }
    }
}
