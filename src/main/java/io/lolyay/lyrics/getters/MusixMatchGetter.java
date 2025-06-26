package io.lolyay.lyrics.getters;

import io.lolyay.JdaMain;
import io.lolyay.config.ConfigManager;
import io.lolyay.lyrics.LyricsNotFoundException;
import io.lolyay.lyrics.Scraper;
import io.lolyay.lyrics.records.Lyrics;
import io.lolyay.lyrics.records.SearchLyrics;
import io.lolyay.utils.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MusixMatchGetter extends LyricsGetter {
    private static final String searchUrlBase = "https://www.musixmatch.com/search?query=%s";

    private String getSearchUrl(String songName) {
        return String.format(searchUrlBase, URLEncoder.encode(songName, StandardCharsets.UTF_8));
    }

    private Map<String, String> getCookies() {
        final String userToken = ConfigManager.getConfig("musixmatch-user-cookie");
        Map<String, String> cookies = new HashMap<>();
        cookies.put("musixmatchUserToken", userToken);
        return cookies;
    }

    private SearchLyrics processSearchResultsDom(Document document) {
        String bestResultSelector = "div:contains(Best result) + div a[href^='/lyrics/']";
        Element bestResultLink = document.selectFirst(bestResultSelector);

        if (bestResultLink == null) {
            Logger.warn("Could not find 'Best result' section, falling back to first track.");
            bestResultLink = document.selectFirst("a[href^='/lyrics/']");
        }

        if (bestResultLink != null) {

            String url = bestResultLink.attr("abs:href");

            Elements infoElements = bestResultLink.select("div[dir=auto]");

            String title = null;
            String author = null;

            if (infoElements.size() >= 2) {
                title = infoElements.get(0).text().trim();
                author = infoElements.get(1).text().trim();
            }

            if (title != null) {
                return new SearchLyrics(url, title, author);
            } else {
                Logger.err("A lyric link was found, but title/author elements inside it were missing.");
                return null;
            }
        } else {
            Logger.err("No lyric links found on the page.");
            return null;
        }
    }

    private String parseLyricsDom(Document document) throws Exception {
        Element scriptElement = document.getElementById("__NEXT_DATA__");

        if (scriptElement == null) {
            Logger.err("Could not find the __NEXT_DATA__ script tag. The page structure may have changed.");
            throw new Exception("Lyrics data script not found.");
        }

        String jsonData = scriptElement.html();
        JSONObject rootJson = new JSONObject(jsonData);


        try {
            String lyricsBody = rootJson.getJSONObject("props")
                    .getJSONObject("pageProps")
                    .getJSONObject("data")
                    .getJSONObject("trackInfo")
                    .getJSONObject("data")
                    .getJSONObject("lyrics")
                    .getString("body");


            return cleanLyrics(lyricsBody);

        } catch (org.json.JSONException e) {
            Logger.err("Failed to parse lyrics from JSON. The JSON structure might have changed. " + e.getMessage());
            throw new Exception("Could not find 'body' in the lyrics JSON object.");
        }
    }


    private String cleanLyrics(String lyricsText) {
        String cleaned = lyricsText.replaceAll("(?s)Writer\\(s\\):.*", "");

        return cleaned
                .replaceAll("(?i)\\[?(verse|chorus|hook|intro|outro|bridge|pre-chorus|refrain|post-chorus|interlude|breakdown|solo|instrumental)\\]?\\s*[:\\s]*[0-9]*", "")
                .replaceFirst("(?i)^Lyrics of .* by .*\r?\n?", "")
                .replaceAll("\n{3,}", "\n\n")
                .trim();
    }

    @Override
    public String getSourceName() {
        return "MusixMatch";
    }

    @Override
    public boolean canGetLyrics(String songName) {
        return ConfigManager.getConfig("musixmatch-user-token") != null &&
                !ConfigManager.getConfig("musixmatch-user-token").equals("YOUR_MUSIXMATCH_USER_TOKEN");
    }

    @Override
    public CompletableFuture<Lyrics> getLyrics(String songName) {
        CompletableFuture<Lyrics> future = new CompletableFuture<>();


        Runnable scrapingTask = () -> {
            try {
                Logger.debug("Starting lyrics fetch for song: '{}'".replace("{}", songName));


                String searchUrl = getSearchUrl(songName);
                String searchHtml = Scraper.getSiteHTML(searchUrl, getCookies());
                Document searchDocument = Jsoup.parse(searchHtml, searchUrl);
                SearchLyrics searchResult = processSearchResultsDom(searchDocument);


                if (searchResult == null || searchResult.url() == null || searchResult.url().isEmpty()) {
                    Logger.debug("No valid search result found for '{}'.".replace("{}", songName));

                    throw new LyricsNotFoundException(searchResult == null ? new SearchLyrics(null, songName, null) : searchResult, getSourceName());
                }

                Logger.debug("Found potential match for '{}': {}".replace("{}", songName).replace("{}", searchResult.toString()));


                String lyricsUrl = searchResult.url();
                String lyricsHtml = Scraper.getSiteHTML(lyricsUrl, getCookies());
                Document lyricsDocument = Jsoup.parse(lyricsHtml, lyricsUrl);
                String lyricsText = parseLyricsDom(lyricsDocument);


                Logger.debug("Successfully parsed lyrics for %s from source %s.".formatted(songName, getSourceName()));
                Lyrics finalLyrics = new Lyrics(searchResult, lyricsText, getSourceName());
                future.complete(finalLyrics);

            } catch (Exception e) {

                Logger.err("Error while fetching lyrics for %s from source %s: %s".formatted(songName, getSourceName(), e.getMessage()));
                future.completeExceptionally(e);
            }
        };


        JdaMain.scheduledTasksManager.startDelayedTask(new Thread(scrapingTask), 100, TimeUnit.MILLISECONDS);

        return future;
    }
}
