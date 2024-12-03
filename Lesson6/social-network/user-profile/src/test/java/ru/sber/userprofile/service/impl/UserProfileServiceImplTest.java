package ru.sber.userprofile.service.impl;

import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sber.userprofile.domain.User;
import ru.sber.userprofile.service.UserProfileService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserProfileServiceImplTest {
    private UserProfileService service;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        service = mock(UserProfileServiceImpl.class);
        expectedUser = new User("1", "John Doe", 18, "jonh@ya.ru");
    }

    @Test
    @Name(value = "Получить пользователя по идентификатору")
    void testShouldCreateUser() {
        when(service.createUser(anyString(), anyInt(), anyString())).thenReturn(expectedUser);
        service.createUser("John Doe", 18, "jonh@ya.ru");
        assertNotNull(expectedUser);
        assertEquals("1", expectedUser.getId());
        verify(service, times(1)).createUser(anyString(), anyInt(), anyString());
    }

    @Test
    @Name(value = "Получить пользователя по идентификатору")
    void testShouldGetUserById() {
        when(service.getUserById("1")).thenReturn(Optional.of(expectedUser));

        if (service.getUserById("1").isPresent()) {
            assertEquals(expectedUser,service.getUserById("1").get());
        }

        verify(service, times(2)).getUserById("1");
    }

    @Test
    @Name(value = "Обновить пользователя")
    void shouldUpdateUser() {
        service.updateUser(expectedUser);

        verify(service, times(1)).updateUser(expectedUser);
    }

    @Test
    @Name(value = "Удалить пользователя")
    void shouldDeleteUser() {
        service.deleteUser(expectedUser);

        verify(service, times(1)).deleteUser(expectedUser);
    }
}