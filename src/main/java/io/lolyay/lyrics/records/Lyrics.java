package io.lolyay.lyrics.records;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public final class Lyrics {
    private final SearchLyrics query;
    @Expose
    private final String content;
    @Expose
    private final String source;

    public Lyrics(SearchLyrics query, String content, String source) {
        this.query = query;
        this.content = content;
        this.source = source;
    }

    public SearchLyrics query() {
        return query;
    }

    public String content() {
        return content;
    }

    public String source() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lyrics lyrics = (Lyrics) o;
        return Objects.equals(query, lyrics.query) &&
               Objects.equals(content, lyrics.content) &&
               Objects.equals(source, lyrics.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, content, source);
    }

    @Override
    public String toString() {
        return "Lyrics[" +
               "query=" + query + 
               ", content=" + content + 
               ", source=" + source + 
               ']';
    }
}
