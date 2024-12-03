package ru.sber.friendship.service.impl;

import ru.sber.friendship.service.FriendshipService;
import ru.sber.userprofile.domain.User;
import ru.sber.userprofile.service.UserProfileService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendshipServiceImpl implements FriendshipService {
    private final Map<String, List<String>> friendships = new HashMap<>();
    private final UserProfileService userService;

    public FriendshipServiceImpl(UserProfileService userService) {
        this.userService = userService;
    }

    @Override
    public boolean addFriend(String userId, String friendId) {
        friendships.computeIfAbsent(userId, k -> new ArrayList<>()).add(friendId);
        return true;
    }

    @Override
    public List<User> getFriends(String userId) {
        List<String> friendIds = friendships.getOrDefault(userId, Collections.emptyList());
        List<User> friends = new ArrayList<>();
        for (String friendId : friendIds) {
            userService.getUserById(friendId).ifPresent(friends::add);
        }
        return friends;
    }
}
