package io.lolyay.musicbot.dlp;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class YoutubeDlpDownloader {
    private static final String YT_DLP_VERSION = "2025.06.30";
    private static final String YT_DLP_BASE_URL = "https://github.com/yt-dlp/yt-dlp/releases/download/" + YT_DLP_VERSION + "/";

    public enum OS {
        WINDOWS("yt-dlp.exe", "yt-dlp.exe"),
        MACOS("yt-dlp_macos", "yt-dlp"),
        LINUX("yt-dlp_linux", "yt-dlp"),
        LINUX_ARM64("yt-dlp_linux_aarch64", "yt-dlp"),
        LINUX_ARM32("yt-dlp_linux_armv7l", "yt-dlp"),
        UNKNOWN(null, null);
        
        final String downloadName;
        public final String binaryName;
        
        OS(String downloadName, String binaryName) {
            this.downloadName = downloadName;
            this.binaryName = binaryName;
        }
    }
    
    public OS getOS() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String osArch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
        
        if (osName.contains("win")) {
            return OS.WINDOWS;
        } else if (osName.contains("mac") || osName.contains("darwin")) {
            return OS.MACOS;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            if (osArch.contains("aarch64") || osArch.contains("arm64")) {
                return OS.LINUX_ARM64;
            } else if (osArch.contains("arm")) {
                return OS.LINUX_ARM32;
            } else {
                return OS.LINUX;
            }
        }
        return OS.UNKNOWN;
    }

    public void downloadYtDlp(OS os) throws IOException {
        if (os.downloadName == null) {
            throw new UnsupportedOperationException("No download available for this OS/architecture");
        }
        
        String downloadUrl = YT_DLP_BASE_URL + os.downloadName;
        System.out.println("Downloading yt-dlp from: " + downloadUrl);
        
        try (InputStream in = new URL(downloadUrl).openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(os.binaryName)) {
            
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            // Clean up partially downloaded file if any
            if (Files.exists(Path.of(os.binaryName))) {
                Files.delete(Path.of(os.binaryName));
            }
            throw new IOException("Failed to download yt-dlp: " + e.getMessage(), e);
        }
    }

}
