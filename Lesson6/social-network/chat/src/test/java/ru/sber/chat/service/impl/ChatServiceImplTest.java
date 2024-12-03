package ru.sber.chat.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.sber.chat.domain.Message;
import ru.sber.chat.service.ChatService;
import ru.sber.notificationservice.api.NotificationService;
import ru.sber.userprofile.domain.User;
import ru.sber.userprofile.service.UserProfileService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatServiceImplTest {
    private UserProfileService userService;
    private NotificationService notificationService;
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserProfileService.class);
        notificationService = Mockito.mock(NotificationService.class);
        chatService = new ChatServiceImpl(userService, notificationService);
    }

    @Test
    void sendMessage_shouldSendSuccessfully() {
        String senderId = "1";
        String recipientId = "2";
        String content = "Привет, как дела?";

        User sender = new User();
        sender.setId(senderId);
        sender.setName("Алиса");

        User recipient = new User();
        recipient.setId(recipientId);
        recipient.setName("Вова");

        when(userService.getUserById(senderId)).thenReturn(Optional.of(sender));
        when(userService.getUserById(recipientId)).thenReturn(Optional.of(recipient));

        Message message = chatService.sendMessage(senderId, recipientId, content);

        assertNotNull(message);
        assertEquals(senderId, message.getSenderId());
        assertEquals(recipientId, message.getRecipientId());
        assertEquals(content, message.getContent());

        verify(notificationService, times(1))
                .sendNotification(eq(recipientId), anyString());
    }

    @Test
    void sendMessage_shouldThrowExceptionIfSenderNotFound() {
        String senderId = "1";
        String recipientId = "2";
        String content = "Привет, как дела?";

        when(userService.getUserById(senderId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                chatService.sendMessage(senderId, recipientId, content));
    }

    @Test
    void getChatHistory_shouldReturnMessagesBetweenUsers() {
        String userId1 = "1";
        String userId2 = "2";

        User user1 = new User();
        user1.setId(userId1);
        user1.setName("Алиса");

        User user2 = new User();
        user2.setId(userId2);
        user2.setName("Вова");

        when(userService.getUserById(userId1)).thenReturn(Optional.of(user1));
        when(userService.getUserById(userId2)).thenReturn(Optional.of(user2));

        chatService.sendMessage(userId1, userId2, "Привет Вова!");
        chatService.sendMessage(userId2, userId1, "Привет Алиса!");
        chatService.sendMessage(userId1, userId2, "Как дела?");

        List<Message> history = chatService.getChatHistory(userId1, userId2);

        assertEquals(3, history.size());
        assertEquals("Привет Вова!", history.get(0).getContent());
        assertEquals("Привет Алиса!", history.get(1).getContent());
        assertEquals("Как дела?", history.get(2).getContent());
    }
}