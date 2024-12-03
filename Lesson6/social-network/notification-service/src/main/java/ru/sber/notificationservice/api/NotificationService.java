package ru.sber.notificationservice.api;

public interface NotificationService {
    void sendNotification(String userId, String message);
}
