package io.lolyay.panel.assets.downloaders;

public abstract class AbstractAssetsDownloader {
    protected final String assetsUrl;

    public AbstractAssetsDownloader(String assetsUrl) {
        this.assetsUrl = assetsUrl;
    }

    public abstract String getDownloaderName();

    public abstract String getAssetsDownloadUrl();

    public String getAssetsUrl(){
        return assetsUrl;
    }
}
