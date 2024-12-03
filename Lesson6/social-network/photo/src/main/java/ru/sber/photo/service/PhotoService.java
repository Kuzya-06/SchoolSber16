package ru.sber.photo.service;

import ru.sber.photo.domain.Photo;

import java.util.List;

public interface PhotoService {
    Photo uploadPhoto(String userId, String url, String description);

    Photo getPhotoById(String photoId);

    List<Photo> getPhotosByUser(String userId);

    void deletePhoto(String photoId);
}
