package io.lolyay.panel.webserver;

import io.lolyay.LavMusicPlayer;
import io.lolyay.config.ConfigManager;
import io.lolyay.panel.assets.AssetsDownloaderManager;
import io.lolyay.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AssetProvider {
    private static final String ASSETS_DIR = "assets";

    public AssetProvider() {
        if (!Files.exists(Paths.get(ASSETS_DIR))) {
            if(ConfigManager.getConfig().getPanel().getAssetsUrl().isEmpty()){
                Logger.err("You need to set a valid assets url, please check your config file.");
                Logger.err("Falling back to default assets url.");
                ConfigManager.getConfig().getPanel().setAssetsUrl("https://github.com/LOLYAY-INC/LavMusicPlayerWeb");
            }
            Logger.log("Downloading assets from : " + ConfigManager.getConfig().getPanel().getAssetsUrl());
            AssetsDownloaderManager.downloadAssets(ConfigManager.getConfig().getPanel().getAssetsUrl());
        }
    }
    public File getAsset(String path) {
        if (path.startsWith("/")) path = path.substring(1);
        path = ASSETS_DIR + "/" + path;
        return new File(path);
    }

    public String getMimeType(String path) {
        if (path == null) return "text/plain";

        path = path.toLowerCase();
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".html")) return "text/html";
        return "application/octet-stream";
    }




}