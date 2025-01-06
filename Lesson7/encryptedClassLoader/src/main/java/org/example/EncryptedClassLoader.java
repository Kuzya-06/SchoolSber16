package org.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class EncryptedClassLoader extends ClassLoader {
    private static final Log log = LogFactory.getLog(EncryptedClassLoader.class);

    private final File dir;
    private final String key;

    public EncryptedClassLoader(String key, File dir, ClassLoader parent) {
        super(parent);
        this.key = key;
        this.dir = dir;
    }

    @Override
    protected Class<?> findClass(String classFileName) throws ClassNotFoundException {
        log.info("EncryptedClassLoader classFileName = " + classFileName);
        try {
            Path filePath = dir.toPath().resolve(classFileName);
            log.info("EncryptedClassLoader filePath = " + filePath);

            byte[] bytes = Files.readAllBytes(filePath);
            log.info("EncryptedClassLoader bytes.length = " + bytes.length);
            // Дешифрование
            decrypt(bytes);

            return defineClass(null, bytes, 0, bytes.length);
        } catch (IOException exception) {
            throw new ClassNotFoundException(String.format("Ошибка загрузки класса: %s", classFileName), exception);
        }
    }

    private void decrypt(byte[] bytes) {
        byte code = (byte) key.length();
        for (var i = 0; i < bytes.length; ++i)
            bytes[i] += code;
    }
}
