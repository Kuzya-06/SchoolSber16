package ru.sber.userprofile.service;

import ru.sber.userprofile.domain.User;

import java.util.Optional;

public interface UserProfileService {
    Optional<User> getUserById(String id);

    void updateUser(User user);

    User createUser(String name, int age, String email);

    void deleteUser(User user);

}
