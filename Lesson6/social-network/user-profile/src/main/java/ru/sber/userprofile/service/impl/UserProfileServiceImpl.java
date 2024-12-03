package ru.sber.userprofile.service.impl;

import ru.sber.userprofile.domain.User;
import ru.sber.userprofile.repositoris.UserProfileRepo;
import ru.sber.userprofile.service.UserProfileService;

import java.util.Optional;

public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepo repo;

    public UserProfileServiceImpl(UserProfileRepo repo) {
        this.repo = repo;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return Optional.ofNullable(repo.getUserById(id));
    }

    @Override
    public void updateUser(User user) {
        repo.updateUser(user);
    }

    @Override
   public User createUser(String name, int age, String email){
        return repo.createUser(name, age, email);
    }

    @Override
    public void deleteUser(User user){
        repo.deleteUser(user);
    }
}
