package ru.sber.userprofile.repositoris;

import ru.sber.userprofile.domain.User;

import java.util.HashMap;
import java.util.Map;

public class UserProfileRepo {
    static Map<String, User> userStorage = new HashMap<>();

    public User getUserById(String id) {
        return userStorage.get(id);
    }

    public void updateUser(User user) {
        userStorage.put(user.getId(), user);
    }

    public User createUser(String name, int age, String email){
        User user = new User();
        user.setId(String.valueOf(userStorage.size() + 1));
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);
        userStorage.put(user.getId(), user);
        return user;
    }

    public void deleteUser(User user){
        userStorage.remove(user.getId());
    }
}
