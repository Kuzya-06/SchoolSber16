package org.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 */
public class Encryptor {
    private static final Log log = LogFactory.getLog(Encryptor.class);


    private final File sourceFilePath;
    private final File targetFilePath;
    private final String key;

    public Encryptor(File sourceFilePath, File targetFilePath, String key) {
        this.sourceFilePath = sourceFilePath;
        this.targetFilePath = targetFilePath;
        this.key = key;
    }

    public Path encryptAndSaveClass(String className) throws IOException {
        log.info("Начало Encriptor.class метод encryptAndSaveClass");

        if (!className.endsWith(".class")) {
            throw new IllegalArgumentException("Переданный файл не с расширением ***.class!");
        }

        Path filePath = sourceFilePath.toPath().resolve(className);
        log.info("Encriptor.class filePath = " + filePath);
        byte[] bytes = Files.readAllBytes(filePath);
        log.info("Encriptor.class bytes.length = " + bytes.length);

        // Шифрование байтов перед сохранением
        encrypt(bytes);

        // Сохранение зашифрованных байтов в файл
        Path encryptedFilePath = targetFilePath.toPath().resolve(className);
        log.info("Encriptor.class encryptedFilePath = " + encryptedFilePath);
        Files.write(encryptedFilePath, bytes);
        log.info("Конец Encriptor.class метод encryptAndSaveClass");
        return encryptedFilePath;
    }

    private void encrypt(byte[] bytes) {
        byte code = (byte) key.length();
        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] -= code;
        }
    }
}
