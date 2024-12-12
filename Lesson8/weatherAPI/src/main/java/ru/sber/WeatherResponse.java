package ru.sber;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.TimeZone;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true) // Игнорируем лишние поля
public class WeatherResponse {
    @JsonProperty("location")
    private LocationWeather location;

    @JsonProperty("current")
    private CurrentWeather current;


    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocationWeather {
        @JsonProperty("name")
        private String name;
        @JsonProperty("region")
        private String region;
        @JsonProperty("country")
        private String country;
        @JsonProperty("lat")
        private double lat;
        @JsonProperty("lon")
        private double lon;
        @JsonProperty("tz_id")
        private TimeZone timeZone;
    }


    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentWeather {

        @JsonProperty("last_updated")
        private String lastUpdated;
        @JsonProperty("temp_c")
        private double tempC;

        @JsonProperty("condition")
        private WeatherCondition condition;

        @JsonProperty("wind_kph")
        private double windKph;
        @JsonProperty("humidity")
        private int humidity;
        @JsonProperty("wind_dir")
        private String windDir;
        @JsonProperty("dewpoint_c")
        private double dewpointC;

        public WindDirection getWindDirectionEnum() {
            return WindDirection.fromString(windDir);
        }

        public double getWindSpeedMps() {
            return windKph / 3.6; // Преобразование км/ч в м/с
        }

        public WindForce getWindForce() {
            return WindForce.fromSpeed(getWindSpeedMps());
        }
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherCondition {
        @JsonProperty("text")
        private String text;

        @JsonProperty("icon")
        private String icon;
    }
}
