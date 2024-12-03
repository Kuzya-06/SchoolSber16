package ru.sber.friendship.service.impl;

import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.sber.userprofile.domain.User;
import ru.sber.userprofile.service.UserProfileService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class FriendshipServiceImplTest {
    private UserProfileService userService;
    private FriendshipServiceImpl friendshipService;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserProfileService.class);
        friendshipService = new FriendshipServiceImpl(userService);
    }

    @Test
    @Name(value = "Добавил друга - успешно")
    void addFriendShouldAddFriendSuccessfully() {
        String userId = "1";
        String friendId = "2";

        assertTrue(friendshipService.addFriend(userId, friendId));
    }

    @Test
    @Name(value = "Получил друзей - успешно")
    void getFriendsShouldReturnFriendsList() {
        String userId = "1";
        String friendId = "2";
        User friend = new User();
        friend.setId(friendId);
        friend.setName("Женя");

        when(userService.getUserById(friendId)).thenReturn(Optional.of(friend));

        friendshipService.addFriend(userId, friendId);
        assertEquals(1, friendshipService.getFriends(userId).size());
        assertEquals("Женя", friendshipService.getFriends(userId).get(0).getName());
    }
}
