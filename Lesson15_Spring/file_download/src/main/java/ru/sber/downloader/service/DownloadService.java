package ru.sber.downloader.service;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ru.sber.downloader.util.RateLimitedOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Data
public class DownloadService {
    Logger log = LoggerFactory.getLogger(DownloadService.class);
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";   // Сброс цвета

    @Value("${links-file}")
    private Resource linksFile;

    @Value("${download.folder}")
    private String downloadFolderPath;

    @Value("${download.threads}")
    private int threadCount;

    @Value("${download.speed-limit}")
    private int speedLimit;

    private AtomicBoolean isDownloading;
    private ExecutorService executorService;

    private final ResourceLoader resourceLoader;

    public DownloadService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    private void init() {
        this.isDownloading = new AtomicBoolean(false);
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }


    public boolean startDownloads() {
        if (isDownloading.compareAndSet(false, true)) {
//            executorService.submit(() -> {
                try {
                    // Убедимся, что папка загрузки пуста перед началом теста
                    File downloadFolder = new File(downloadFolderPath);
                    log.info("downloadFolderPath = {}", downloadFolder.getAbsolutePath());


                    // Удаляем файлы из папки, если она существует
                    if (downloadFolder.exists()) {
                        for (File file : Objects.requireNonNull(downloadFolder.listFiles())) {
                            log.info("file = {}", file);
                            file.delete();
                        }
                    }
                    // Читаем список ссылок из файла links.txt
                    List<String> links = Files.readAllLines(Paths.get(linksFile.getURI()));

                    for (String link : links) {
                        executorService.submit(() -> downloadFile(link, downloadFolder));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isDownloading.set(false);
                }
//            });
            return true;
        }
        return false;
    }

    private void downloadFile(String fileUrl, File downloadFolder) {
        try {


            URL url = new URL(fileUrl);
            log.info("url = " + url);

            String fileName = new File(url.getPath()).getName();
            log.info("*** Имя файла url.getPath() = " + fileName);

            File outputFile = new File(downloadFolder, fileName);
            log.info("outputFile = " + outputFile);

            // Создаем папку для загрузок, если она не существует
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }

            // Загрузка файла с ограничением скорости
            try (InputStream in = new BufferedInputStream(url.openStream());
                 RateLimitedOutputStream out = new RateLimitedOutputStream(outputFile, speedLimit)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                System.out.println(BLUE+"Файл загружен: " + outputFile.getAbsolutePath()+RESET);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке файла: " + fileUrl);
            e.printStackTrace();
        }
    }

}
