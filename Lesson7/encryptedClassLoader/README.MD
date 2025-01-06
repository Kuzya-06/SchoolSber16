## Задание
Написать EncryptedClassloader.  
Данный класслоадер умеет загружать классы из файлов дешифруя их, если они были зашифрованы. Ваша задача 
переопределить метод findClass(). В нем лоадер считывает зашифрованный массив байт, дешифрует его и превращает в 
класс (с помощью метода defineClass).  

На вход класслодер принимает ключ шифрования, рутовую папку, в которой будет искать классы, родительский класслодер. 
Логика шифрования/дешифрования с использованием ключа может быть любой на ваш вкус (например, каждый считаный байт 
класса увеличить на определение число).  

# Как работать с этим приложением
Примечание:  
В классе Main есть шифратор и дешифратор.  
Есть две папки temp1 и temp2 в которых есть уже зашифрованные файлы Sample.class с разным содержимым.

1. Можно просто запустить приложение, предварительно скомпилировать  
```bash
mvn clean install
```

2. Либо переносить из папки temp1 или temp2 файл Sample.class в папку instance
   (encryptedClassLoader\src\main\java\org\example\instance).  
   В первом случае будет выведено
   ```Привет от класса Sample (temp1) :)```  
   Во втором случае будет выведено
   ```Привет от класса Sample (temp2)! :)```

## класс Main
```java
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
```

## класс EncryptedClassLoader
```java
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
```

## класс Encryptor
```java
package org.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


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
```