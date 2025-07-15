package io.lolyay.panel.assets.downloaders;

import io.lolyay.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class GithubAssetsDownloader extends AbstractAssetsDownloader{
    private final String RELEASE_API_URL = "https://api.github.com/repos/%s/%s/releases/%s";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final String version;
    private final String repoUrl;

    public GithubAssetsDownloader(String assetsUrl, String version) {
        super(assetsUrl);
        this.version = version;
        this.repoUrl = assetsUrl;
    }

    public GithubAssetsDownloader(String assetsUrl) {
        super(assetsUrl);
        this.version = "latest";
        this.repoUrl = assetsUrl;
    }

    @Override
    public String getDownloaderName() {
        return "Github-Releases-" + version;
    }

    @Override
    public String getAssetsDownloadUrl() {
        JSONObject release = getJsonFromUrl(formatApiUrl());
        if(release == null) {
            Logger.err("Failed to get release from " + formatApiUrl());
            System.exit(1);
            return "";
        }
        JSONArray assets = release.getJSONArray("assets");
        if(assets == null) {
            Logger.err("Failed to get assets from " + formatApiUrl());
            Logger.err(release.toString());
            System.exit(1);
            return "";
        }
        for(int i = 0; i < assets.length(); i++) {
            JSONObject asset = assets.getJSONObject(i);
            if(asset.getString("name").equals("assets.zip")) {
                return asset.getString("browser_download_url");
            }
        }
        return "";

    }


    private JSONObject getJsonFromUrl(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(response.body());
        } catch (Exception e) {
            return null;
        }
    }

    private String formatApiUrl(){
        if(repoUrl.endsWith("/")) {
            return repoUrl.replace(
                    "https://github.com/",
                    "https://api.github.com/repos/"
            ) + "releases/" + version;
        }
        else {
            return repoUrl.replace(
                    "https://github.com/",
                    "https://api.github.com/repos/"
            ) + "/releases/" + version;
        }
    }
}
