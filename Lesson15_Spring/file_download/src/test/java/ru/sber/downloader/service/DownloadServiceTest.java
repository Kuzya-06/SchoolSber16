package ru.sber.downloader.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class DownloadServiceTest {
    Logger log = LoggerFactory.getLogger(DownloadServiceTest.class);

    @Value("${download.folder}")
    private String downloadFolderPath;

    @Autowired
    private DownloadService downloadService;

    @Test
    void testStartDownloads() {
        // Убедимся, что папка загрузки пуста перед началом теста
        File downloadFolder = new File(downloadFolderPath);
        log.info("Test downloadFolderPath = {}", downloadFolderPath);

        // Удаляем файлы из папки, если она существует
        if (downloadFolder.exists()) {
            for (File file : Objects.requireNonNull(downloadFolder.listFiles())) {
                log.info("Test file = {}", file);
                file.delete();
            }
        }
        // Создаем папку, если её нет
        boolean mkdirs = downloadFolder.mkdirs();
        log.info("Test mkdirs = {}", mkdirs);

        // Запускаем загрузку
        boolean started = downloadService.startDownloads();
        log.info("Test started = {}", started);
        assertTrue(started, "Загрузка не началась");

        // Ждем завершения загрузки (максимум 10 секунд)
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Проверяем, что файлы были загружены
        assertTrue(Objects.requireNonNull(
                downloadFolder.listFiles()).length > 0, "Файлы не были загружены");
    }
}
