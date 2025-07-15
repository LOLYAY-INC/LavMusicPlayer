package io.lolyay.panel.assets;

import io.lolyay.panel.assets.downloaders.AbstractAssetsDownloader;
import io.lolyay.panel.assets.downloaders.GithubAssetsDownloader;
import io.lolyay.panel.assets.downloaders.HttpDownloader;
import io.lolyay.utils.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AssetsDownloaderManager {

    private static final Path ASSETS_DIR = Paths.get("assets");

    public static void downloadAssets(String assetsUrl) {
        Path tempZipFile = null;
        try {
            String assetsDownloadUrl = getDownloader(assetsUrl).getAssetsDownloadUrl();
            Logger.log("Resolved assets download URL to: " + assetsDownloadUrl);

            tempZipFile = Files.createTempFile("panel-assets-", ".zip");
            Logger.log("Downloading assets from " + assetsDownloadUrl + " to " + tempZipFile);
            try (InputStream in = new URI(assetsDownloadUrl).toURL().openStream()) {
                Files.copy(in, tempZipFile, StandardCopyOption.REPLACE_EXISTING);
            }
            Logger.log("Download complete.");

            if (Files.exists(ASSETS_DIR)) {
                Logger.log("Deleting existing assets directory...");
                Files.walk(ASSETS_DIR)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                Logger.err("Failed to delete " + path + ": " + e.getMessage());
                            }
                        });
                Logger.log("Existing assets directory deleted.");
            }

            Files.createDirectories(ASSETS_DIR);
            Logger.log("Created new assets directory.");

            Logger.log("Extracting assets...");
            unzip(tempZipFile, ASSETS_DIR);
            Logger.log("Assets successfully downloaded and extracted.");

        } catch (Exception e) {
            Logger.err("Failed to download and process assets: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (tempZipFile != null) {
                try {
                    Files.deleteIfExists(tempZipFile);
                    Logger.log("Cleaned up temporary file: " + tempZipFile);
                } catch (IOException e) {
                    Logger.err("Failed to delete temporary file: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Unzips a file into a destination directory.
     * Includes a security check to prevent Zip Slip vulnerabilities.
     * @param zipFilePath Path to the .zip file.
     * @param destDir Path to the destination directory.
     * @throws IOException if an I/O error occurs.
     */
    private static void unzip(Path zipFilePath, Path destDir) throws IOException {
        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                Path filePath = destDir.resolve(entry.getName());

                if (!filePath.normalize().startsWith(destDir.normalize())) {
                    throw new IOException("Bad zip entry: " + entry.getName() + " (Zip Slip vulnerability detected)");
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zipIn, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private static AbstractAssetsDownloader getDownloader(String assetsUrl) {
        if (isGithubUrl(assetsUrl)) {
            return new GithubAssetsDownloader(assetsUrl);
        }
        return new HttpDownloader(assetsUrl);
    }


    private static boolean isGithubUrl(String url) {
        return url != null && (url.startsWith("https://github.com/") || url.startsWith("http://github.com/"));
    }
}