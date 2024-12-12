package ru.sber;

public enum WindDirection {
    N("Северный"),
    NE("Северо-восточный"),
    E("Восточный"),
    SE("Юго-восточный"),
    S("Южный"),
    SW("Юго-западный"),
    W("Западный"),
    NW("Северо-западный"),
    NNE("Север-северо-восточный"),
    ENE("Восток-северо-восточный"),
    ESE("Восток-юго-восточный"),
    SSE("Юг-юго-восточный"),
    SSW("Юг-юго-западный"),
    WSW("Запад-юго-западный"),
    WNW("Запад-северо-западный"),
    NNW("Север-северо-западный");

    private final String description;

    WindDirection(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static WindDirection fromString(String value) {
        for (WindDirection direction : values()) {
            if (direction.name().equalsIgnoreCase(value)) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Неизвестное направление ветра: " + value);
    }
}