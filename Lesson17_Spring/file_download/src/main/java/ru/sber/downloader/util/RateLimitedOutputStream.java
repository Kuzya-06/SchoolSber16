package ru.sber.downloader.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Класс RateLimitedOutputStream ограничивает скорость записи данных в файл.
 * Он делает это, вычисляя, сколько времени требуется для записи данных с учетом установленного ограничения скорости
 * (в килобайтах в секунду). Если скорость записи превышает установленное значение, поток "засыпает" на определённое
 * время, чтобы не превышать лимит.
 */
public class RateLimitedOutputStream extends OutputStream {
    Logger log = LoggerFactory.getLogger(RateLimitedOutputStream.class);
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";   // Сброс цвета

    private final OutputStream outputStream;
    private final int maxBytesPerSecond;
    private long bytesWritten;
    private final long startTime;

    private long lastLogTime;
    private long lastBytesWritten;

    /**
     * @param file                  - Файл, в который будут записываться данные.
     * @param maxKilobytesPerSecond - Максимальная скорость записи в килобайтах в секунду
     * @throws IOException
     */
    public RateLimitedOutputStream(File file, int maxKilobytesPerSecond) throws IOException {
        log.info("        RateLimitedOutputStream file = {}, maxKilobytesPerSecond = {}", file, maxKilobytesPerSecond);
        this.outputStream = new FileOutputStream(file);
        this.maxBytesPerSecond = maxKilobytesPerSecond * 1024;
        this.bytesWritten = 0;
        this.startTime = System.currentTimeMillis();
        this.lastLogTime = startTime;
        this.lastBytesWritten = 0;
    }

    @Override
    public void write(int b) throws IOException {
        throttle();
        outputStream.write(b);
        bytesWritten++;
        logSpeed();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        throttle();
        outputStream.write(b, off, len);
        bytesWritten += len;
        logSpeed();
    }

    /**
     * Вычисляет, сколько времени прошло с начала записи (elapsedTime).
     * Рассчитывает, сколько времени ожидалось бы для записи текущего объема данных (expectedTime).
     * Если ожидаемое время больше, чем фактическое время, поток приостанавливается (Thread.sleep), чтобы
     * компенсировать превышение скорости.
     *
     * @throws IOException
     */
    private void throttle() throws IOException {
        long elapsedTime = System.currentTimeMillis() - startTime; // фактическое время

        if (elapsedTime > 0) {
            // сколько времени ожидалось бы для записи текущего объема данных
            long expectedTime = (bytesWritten * 1000) / maxBytesPerSecond;
//            log.info("Время ожидания для записи текущего объема данных = {}", expectedTime);
            if (expectedTime > elapsedTime) {
                try {
                    Thread.sleep(expectedTime - elapsedTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Thread был прерван", e);
                }
            }
        }
    }

    /**
     * Вычисляет объем данных, записанных за последний интервал (5 секунд).
     * Рассчитывает текущую скорость в КБ/с.
     * Логирует общую статистику:
     * Среднюю скорость за интервал.
     * Общее количество записанных данных (в КБ).
     * Общее время работы.
     */
    private void logSpeed() {
        long currentTime = System.currentTimeMillis();
        long interval = currentTime - lastLogTime;

        if (interval >= 5000) { // Лог каждые 5 секунд
            long bytesThisInterval = bytesWritten - lastBytesWritten;
            double speedKbPerSec = (bytesThisInterval * 1000.0 / interval) / 1024;

            System.out.printf(
                    "%s Скорость загрузки: %.2f KB/s, Записано всего: %d KB, Прошло времени: %.2f s %s%n", GREEN,
                    speedKbPerSec, bytesWritten / 1024, (currentTime - startTime) / 1000.0, RESET);

            lastLogTime = currentTime;
            lastBytesWritten = bytesWritten;
        }
    }

}
