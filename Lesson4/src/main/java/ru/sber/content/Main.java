package ru.sber.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * статья <a href="https://www.8host.com/blog/otpravka-http-zaprosov-get-i-post-v-java/">...</a>
 * Программа отображает на экран содержимое сайта, ссылка на который задаётся параметром url.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String url;

        while (true) {
            System.out.println("Введите URL ресурса:");
            url = scanner.nextLine();

            try {
                readContent(url);
                break;
            } catch (MalformedURLException e) {
                System.err.println("Некорректный URL: " + e.getMessage());
                System.out.println("Попробуйте снова.");
            } catch (IOException e) {
                System.err.println("Ошибка доступа к ресурсу: " + e.getMessage());
                System.out.println("Попробуйте снова.");
            } catch (Exception e) {
                System.err.println("Неизвестная ошибка: " + e.getMessage());
                System.out.println("Попробуйте снова.");
            }
        }
        scanner.close();
    }

    public static void readContent(String url) throws IOException {

        URL website = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) website.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Код ответа: " + responseCode);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
