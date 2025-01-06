package org.example;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String... args) throws FileNotFoundException {
        // replace with your path
        final String dir = "D:\\SchoolJavaSber\\project\\SchoolSber16\\Lesson7\\encrypted-classloader\\target\\classes\\org\\example";
        final byte key = 1;


        // Encrypt normal class
        try (
                final FileInputStream in = new FileInputStream(new File(dir, "NormalClass.class"));
                final FileOutputStream out = new FileOutputStream(new File(dir, "EncryptedClass.class"))
        ) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            final var buffer = new byte[4096];
            for (int length; (length = in.read(buffer)) != -1; ) {
                for(int i = 0; i < length; ++i)
                    buffer[i] += key;
                result.write(buffer, 0, length);
            }
            final byte[] outbuf = result.toByteArray();
            out.write(outbuf, 0, outbuf.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // decrypt recently encrypted class

        final EncryptedClassLoader encryptedClassLoader = new EncryptedClassLoader(key, new File(dir),
                App.class.getClassLoader());

        try {
            final var EncryptedClass = encryptedClassLoader.findClass("EncryptedClass.class");
            final var ctor = EncryptedClass.getConstructor();
            final Runnable object = (Runnable) ctor.newInstance();
            object.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
