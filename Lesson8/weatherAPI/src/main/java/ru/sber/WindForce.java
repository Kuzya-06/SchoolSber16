package ru.sber;

public enum WindForce {
    CALM(0, 0.0, 0.2, "Штиль"),
    LIGHT_AIR(1, 0.3, 1.5, "Тихий"),
    LIGHT_BREEZE(2, 1.6, 3.3, "Лёгкий"),
    GENTLE_BREEZE(3, 3.4, 5.4, "Слабый"),
    MODERATE_BREEZE(4, 5.5, 7.9, "Умеренный"),
    FRESH_BREEZE(5, 8.0, 10.7, "Свежий"),
    STRONG_BREEZE(6, 10.8, 13.8, "Сильный"),
    HIGH_WIND(7, 13.9, 17.1, "Крепкий"),
    GALE(8, 17.2, 20.7, "Очень крепкий"),
    STRONG_GALE(9, 20.8, 24.4, "Шторм"),
    STORM(10, 24.5, 28.4, "Сильный шторм"),
    VIOLENT_STORM(11, 28.5, 32.6, "Жестокий шторм"),
    HURRICANE(12, 32.7, Double.MAX_VALUE, "Ураган");

    private final int beaufortScale;       // Балл по шкале Бофорта
    private final double minSpeed;        // Минимальная скорость ветра (м/с)
    private final double maxSpeed;        // Максимальная скорость ветра (м/с)
    private final String description;     // Описание силы ветра

    WindForce(int beaufortScale, double minSpeed, double maxSpeed, String description) {
        this.beaufortScale = beaufortScale;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.description = description;
    }

    public int getBeaufortScale() {
        return beaufortScale;
    }

    public double getMinSpeed() {
        return minSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public String getDescription() {
        return description;
    }

    public static WindForce fromSpeed(double speed) {
        for (WindForce force : values()) {
            if (speed >= force.minSpeed && speed <= force.maxSpeed) {
                return force;
            }
        }
        throw new IllegalArgumentException("Скорость ветра вне допустимого диапазона: " + speed);
    }
}

