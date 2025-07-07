package io.lolyay.panel.webserver;

import io.lolyay.LavMusicPlayer;
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
    private final AssetType type;
    private final ExecutorService executorService;
    private final CompletionService<Void> completionService;
    private int pendingTasks = 0;

    public AssetProvider() {
        // Create a thread pool with core pool size based on available processors
        int corePoolSize = Math.max(2, Runtime.getRuntime().availableProcessors() - 1);
        this.executorService = Executors.newFixedThreadPool(corePoolSize, r -> {
            Thread t = new Thread(r, "AssetExtractor-" + System.currentTimeMillis() % 1000);
            t.setDaemon(true);
            return t;
        });
        this.completionService = new ExecutorCompletionService<>(executorService);
        this.type = detectType();

        if (type == AssetType.RESOURCE && LavMusicPlayer.shouldExtract) {
            extractAssets();
        }
    }

    public File getAsset(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if(type == AssetType.FILE)
            Logger.debug("Using local file system: " + ASSETS_DIR + "/" + path);
        else
            Logger.debug("Using jar file: " + ASSETS_DIR + "/" + path);
        return type == AssetType.FILE
                ? new File(ASSETS_DIR, path)
                : new File(Objects.requireNonNull(getClass().getResource("/" + ASSETS_DIR + "/" + path)).getFile());
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

    private AssetType detectType() {
        return new File(ASSETS_DIR).exists() ? AssetType.FILE : AssetType.RESOURCE;
    }

    private void extractAssets() {
        try {
            // Create the base assets directory if it doesn't exist
            Path assetsPath = Paths.get(ASSETS_DIR);
            if (!Files.exists(assetsPath)) {
                Files.createDirectories(assetsPath);
            }

            // Get the resource URL
            URL resourceUrl = getClass().getClassLoader().getResource(ASSETS_DIR);
            if (resourceUrl == null) {
                Logger.err("Could not find assets directory in resources");
                return;
            }

            Logger.log("Starting asset extraction...");
            long startTime = System.currentTimeMillis();

            // Handle JAR file case
            if (resourceUrl.getProtocol().equals("jar")) {
                extractFromJar(assetsPath);
            } else {
                // Handle IDE/development case
                extractFromFileSystem(Paths.get(resourceUrl.toURI()), assetsPath);
            }

            // Wait for all tasks to complete
            waitForCompletion();

            long duration = System.currentTimeMillis() - startTime;
            Logger.log(String.format("Assets extraction completed in %dms", duration));

        } catch (Exception e) {
            Logger.err("Error during asset extraction: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Shutdown the executor service when done
            executorService.shutdown();
        }
    }

    private void extractFromJar(Path targetPath) throws IOException {
        String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        try (JarFile jar = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                // Only process entries in the assets directory
                if (entryName.startsWith(ASSETS_DIR + "/") && !entry.isDirectory()) {
                    String relativePath = entryName.substring(ASSETS_DIR.length() + 1);
                    Path destPath = targetPath.resolve(relativePath);

                    // Submit the extraction task
                    submitExtractionTask(() -> {
                        try {
                            // Create parent directories if they don't exist
                            Files.createDirectories(destPath.getParent());

                            // Copy the file
                            try (InputStream is = getClass().getClassLoader().getResourceAsStream(entryName)) {
                                if (is != null) {
                                    Files.copy(is, destPath, StandardCopyOption.REPLACE_EXISTING);
                                    Logger.debug("Extracted: " + relativePath);
                                }
                            }
                        } catch (IOException e) {
                            Logger.err("Failed to extract asset: " + relativePath + " - " + e.getMessage());
                        }
                    });
                }
            }
        }
    }

    private void extractFromFileSystem(Path sourcePath, Path targetPath) throws IOException {
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path relativePath = sourcePath.relativize(file);
                Path destPath = targetPath.resolve(relativePath.toString());

                // Submit the extraction task
                submitExtractionTask(() -> {
                    try {
                        // Create parent directories if they don't exist
                        Files.createDirectories(destPath.getParent());

                        // Copy the file
                        Files.copy(file, destPath, StandardCopyOption.REPLACE_EXISTING);
                        Logger.debug("Extracted: " + relativePath);
                    } catch (IOException e) {
                        Logger.err("Failed to extract asset: " + relativePath + " - " + e.getMessage());
                    }
                });

                return FileVisitResult.CONTINUE;
            }
        });
    }

    private synchronized void submitExtractionTask(Runnable task) {
        pendingTasks++;
        completionService.submit(() -> {
            try {
                task.run();
            } finally {
                synchronized (this) {
                    pendingTasks--;
                    notifyAll();
                }
            }
            return null;
        });
    }

    private synchronized void waitForCompletion() throws InterruptedException {
        while (pendingTasks > 0) {
            try {
                // Wait for tasks to complete, but check every 100ms to prevent deadlocks
                wait(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw e;
            }
        }
    }

    private enum AssetType {
        RESOURCE,
        FILE
    }
}