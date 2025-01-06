package org.example;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class Main {
    private static final Log log = LogFactory.getLog(Main.class);

    public static void main(String[] args) {
        String encryptionKey = "secretKey";
        String sourceFilePath = "D:\\SchoolJavaSber\\project\\SchoolSber16\\Lesson7\\encryptedClassLoader\\target" +
                "\\classes\\org\\example\\instance\\";
        String targetFilePath = "D:\\SchoolJavaSber\\project\\SchoolSber16\\Lesson7\\encryptedClassLoader\\src" +
                "\\main\\java\\org\\example\\instance\\";
        String nameClass = "Sample.class";

        // Создаем Encryptor для шифрования
        try {
            Encryptor encryptor = new Encryptor(new File(sourceFilePath), new File(targetFilePath), encryptionKey);
            log.info("encryptor = " + encryptor);
            encryptor.encryptAndSaveClass(nameClass);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Создаем EncryptedClassLoader для дешифрования
        try {
            EncryptedClassLoader decryption = new EncryptedClassLoader(encryptionKey, new File(targetFilePath),
                    Main.class.getClassLoader());
            log.info("decryption = " + decryption);
            Class<?> loadedClass = decryption.findClass(nameClass);

            // Создаем экземпляр класса и вызываем метод
            Object instance = loadedClass.getDeclaredConstructor().newInstance();
            loadedClass.getMethod("printMessage").invoke(instance);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException | SecurityException e) {
            e.printStackTrace();
        }
    }

}
