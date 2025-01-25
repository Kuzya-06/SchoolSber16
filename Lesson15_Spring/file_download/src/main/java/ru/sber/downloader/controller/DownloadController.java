package ru.sber.downloader.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sber.downloader.service.DownloadService;

@RestController
@RequestMapping("/api/download")
public class DownloadController {
    private final DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @GetMapping("/start")
    public ResponseEntity<String> startDownload() {
        boolean started = downloadService.startDownloads();
        if (started) {
            return ResponseEntity.ok("Загрузка началась!");
        } else {
            return ResponseEntity.badRequest().body("Загрузка уже выполняется!");
        }
    }
}

