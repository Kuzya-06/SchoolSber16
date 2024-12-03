package ru.sber.notificationservice.api.impl;

import ru.sber.notificationservice.api.NotificationService;

public class MockNotificationServiceImpl implements NotificationService {
    @Override
    public void sendNotification(String userId, String message) {
        System.out.println("Уведомление для пользователя " + userId + ": " + message);
    }
}
