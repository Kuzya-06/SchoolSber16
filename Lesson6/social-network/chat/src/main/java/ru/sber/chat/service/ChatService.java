package ru.sber.chat.service;


import ru.sber.chat.domain.Message;

import java.util.List;

public interface ChatService {
    Message sendMessage(String senderId, String recipientId, String content);
    List<Message> getChatHistory(String userId1, String userId2);
}
