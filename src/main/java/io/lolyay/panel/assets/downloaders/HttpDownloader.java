package io.lolyay.panel.assets.downloaders;

import io.lolyay.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpDownloader extends AbstractAssetsDownloader{
    public HttpDownloader(String assetsUrl) {
        super(assetsUrl);
    }

    @Override
    public String getDownloaderName() {
        return "Default-Http";
    }

    @Override
    public String getAssetsDownloadUrl() {
        return assetsUrl;

    }
}
