package ru.sber.friendship.service;

import ru.sber.userprofile.domain.User;

import java.util.List;

public interface FriendshipService {
    boolean addFriend(String userId, String friendId);
    List<User> getFriends(String userId);
}
