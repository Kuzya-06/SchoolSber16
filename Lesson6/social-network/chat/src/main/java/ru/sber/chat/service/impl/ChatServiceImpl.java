package ru.sber.chat.service.impl;

import lombok.RequiredArgsConstructor;
import ru.sber.chat.domain.Message;
import ru.sber.chat.service.ChatService;
import ru.sber.notificationservice.api.NotificationService;
import ru.sber.userprofile.service.UserProfileService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final UserProfileService userService;
    private final NotificationService notificationService;
    private final List<Message> messageStorage = new ArrayList<>();


    @Override
    public Message sendMessage(String senderId, String recipientId, String content) {

        userService.getUserById(senderId).orElseThrow(() ->
                new IllegalArgumentException("Sender not found"));
        userService.getUserById(recipientId).orElseThrow(() ->
                new IllegalArgumentException("Recipient not found"));

        Message message = new Message();
        message.setId(String.valueOf(messageStorage.size() + 1));
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        messageStorage.add(message);

        notificationService.sendNotification(recipientId, "Новое сообщение от пользователя " + senderId);

        return message;
    }

    @Override
    public List<Message> getChatHistory(String userId1, String userId2) {
        return messageStorage.stream()
                .filter(msg ->
                        (msg.getSenderId().equals(userId1) && msg.getRecipientId().equals(userId2)) ||
                                (msg.getSenderId().equals(userId2) && msg.getRecipientId().equals(userId1)))
                .sorted((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()))
                .collect(Collectors.toList());
    }
}
