package ru.sber;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Scanner;

public class WeatherCurrentApp {

    private static final String API_KEY = "a15aa151cbcc43c38cb195248240912";
    private static final String BASE_URL = "https://api.weatherapi.com/v1/current.json";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите название города: ");
        String city = scanner.nextLine();

        try {

            String jsonResponse = fetchWeatherData(city);

            WeatherResponse weatherResponse = parseWeatherData(jsonResponse);

            printInfo(weatherResponse);
        } catch (IOException e) {
            System.err.println("Ошибка при получении данных о погоде: " + e.getMessage());
        }
    }


    /**
     * Отправляем запрос и получаем данные
     *
     * @param city
     * @return
     * @throws IOException
     */
    private static String fetchWeatherData(String city) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String url = BASE_URL + "?key=" + API_KEY + "&q=" + city + "&aqi=no" + "&lang=en";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка HTTP: " + response.code());
            }
            return response.body().string();
        }
    }


    /**
     * Десериализуем ответ
     *
     * @param jsonResponse
     * @return
     * @throws IOException
     */
    private static WeatherResponse parseWeatherData(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonResponse, WeatherResponse.class);
    }

    /**
     * Вывод в консоль (печать)
     *
     * @param weatherResponse
     */
    private static void printInfo(WeatherResponse weatherResponse) {
        System.out.println("Дата обновления: " + weatherResponse.getCurrent().getLastUpdated());
        System.out.print("Город: " + weatherResponse.getLocation().getName() + ", ");
        System.out.print("Регион: " + weatherResponse.getLocation().getRegion() + ", ");
        System.out.print("Страна: " + weatherResponse.getLocation().getCountry() + ", ");
        System.out.print("Широта: " + weatherResponse.getLocation().getLat() + ", ");
        System.out.print("Долгота: " + weatherResponse.getLocation().getLon() + ", ");
        System.out.println("Временная зона: " + weatherResponse.getLocation().getTimeZone().getDisplayName() + ".");

        System.out.println("Температура: " + weatherResponse.getCurrent().getTempC() + " °C");
        System.out.println("Облачность: " + weatherResponse.getCurrent().getCondition().getText());
        System.out.println("Icon: " + weatherResponse.getCurrent().getCondition().getIcon());

        System.out.println("Влажность: " + weatherResponse.getCurrent().getHumidity());
        System.out.println("Ветер: " + weatherResponse.getCurrent().getWindDirectionEnum().getDescription());

        double windKph = weatherResponse.getCurrent().getWindKph();
        String format = String.format("%.2f", windKph * 0.277778);
        System.out.print("Ветер: " + format + " м/с (" + windKph + " км/ч), ");
        System.out.println(weatherResponse.getCurrent().getWindForce().getDescription() + " (" + weatherResponse.getCurrent().getWindForce().getBeaufortScale() + " балл(ов) Бофорта)");

        System.out.println("Точка росы: " + weatherResponse.getCurrent().getDewpointC() + " °C");
    }
}
