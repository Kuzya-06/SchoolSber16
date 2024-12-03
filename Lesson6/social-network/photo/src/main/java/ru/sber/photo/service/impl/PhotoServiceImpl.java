package ru.sber.photo.service.impl;

import lombok.RequiredArgsConstructor;
import ru.sber.photo.domain.Photo;
import ru.sber.photo.service.PhotoService;
import ru.sber.userprofile.service.UserProfileService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final Map<String, List<Photo>> photoStorage = new HashMap<>();
    private final UserProfileService userService;

    @Override
    public Photo uploadPhoto(String userId, String url, String description) {
        userService.getUserById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found"));

        Photo photo = new Photo();
        photo.setId(String.valueOf(photoStorage.size() + 1));
        photo.setUserId(userId);
        photo.setUrl(url);
        photo.setDescription(description);

        photoStorage.computeIfAbsent(userId, k -> new ArrayList<>()).add(photo);
        return photo;
    }

    @Override
    public Photo getPhotoById(String photoId) {
        List<Photo> photos = photoStorage.get(photoId);
        if(photos==null){
            return null;
        } else return photos.get(0);
    }


    @Override
    public List<Photo> getPhotosByUser(String userId) {
        return photoStorage.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public void deletePhoto(String photoId) {
        Photo photo = photoStorage.get(photoId).get(0);

        if (photo != null) {
            photoStorage.remove(photoId);
        }
    }
}
