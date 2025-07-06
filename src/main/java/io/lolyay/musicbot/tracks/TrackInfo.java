package io.lolyay.musicbot.tracks;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public final class TrackInfo {
    @Expose
    private final String title;
    @Expose
    private final String author;
    @Expose
    private final String artWorkUrl;
    @Expose
    private final long duration;

    public TrackInfo(String title, String author, String artWorkUrl, long  duration) {
        this.title = title;
        this.author = author;
        this.artWorkUrl = artWorkUrl;
        this.duration = duration;
    }

    public String title() {
        return title;
    }

    public String author() {
        return author;
    }

    public String artWorkUrl() {
        return artWorkUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TrackInfo trackInfo = (TrackInfo) obj;
        return Objects.equals(title, trackInfo.title) &&
               Objects.equals(author, trackInfo.author) &&
               Objects.equals(artWorkUrl, trackInfo.artWorkUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, artWorkUrl);
    }

    @Override
    public String toString() {
        return "TrackInfo[" +
               "title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", artWorkUrl='" + artWorkUrl + '\'' +
               ']';
    }
}
