package ru.sber.notificationservice.api.impl;

import org.junit.jupiter.api.Test;
import ru.sber.notificationservice.api.NotificationService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MockNotificationServiceImplTest {

    @Test
    void sendNotification() {
        String userId = "1";
        String message = "Привет";
        NotificationService service = mock(MockNotificationServiceImpl.class);

        doCallRealMethod().when(service).sendNotification(anyString(), anyString());

        service.sendNotification(userId, message);

        verify(service, times(1)).sendNotification(userId, message);
    }
}