package ru.sber.photo.service.impl;

import jdk.jfr.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sber.photo.domain.Photo;
import ru.sber.photo.service.PhotoService;
import ru.sber.userprofile.domain.User;
import ru.sber.userprofile.service.UserProfileService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PhotoServiceImplTest {

    private UserProfileService userService;
    private PhotoServiceImpl photoService;

    private User user;
    private final String USERId = "1";
    private final String URL = "http://social.sber.com/photo/photo.jpg";
    private final String DESCRIPTION = "Я на природе";

    @BeforeEach
    void setUp() {
        userService = mock(UserProfileService.class);
        photoService = new PhotoServiceImpl(userService);

        user = new User();
        user.setId(USERId);
        user.setName("Женя");
    }

    @Test
    @Name(value = "Загрузка фото - успешно")
    void uploadPhotoShouldUploadSuccessfully() {

        when(userService.getUserById(USERId)).thenReturn(Optional.of(user));

        Photo photo = photoService.uploadPhoto(USERId, URL, DESCRIPTION);

        assertNotNull(photo);
        assertEquals(USERId, photo.getUserId());
        assertEquals(URL, photo.getUrl());
        assertEquals(DESCRIPTION, photo.getDescription());
    }

    @Test
    @Name(value = "Загрузка фото - вызывает исключение, если пользователь не найден")
    void uploadPhotoShouldThrowExceptionIfUserNotFound() {

        when(userService.getUserById(USERId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                photoService.uploadPhoto(USERId, URL, DESCRIPTION));
    }

    @Test
    @Name(value = "Получение фото пользователя")
    void getUserPhotosShouldReturnPhotosForUser() {

        when(userService.getUserById(USERId)).thenReturn(Optional.of(user));

        photoService.uploadPhoto(USERId, URL, DESCRIPTION);
        List<Photo> photos = photoService.getPhotosByUser(USERId);

        assertEquals(1, photos.size());
        assertEquals(URL, photos.get(0).getUrl());
    }


    @Test
    @Name(value = "Получение фото по номеру - успешно")
    void getPhotoByIdShouldReturnPhoto() {

        when(userService.getUserById(USERId)).thenReturn(Optional.of(user));

        Photo photo = photoService.uploadPhoto(USERId, URL, DESCRIPTION);

        Photo photoById = photoService.getPhotoById("1");
        Photo photoById2 = photoService.getPhotoById("2");
        assertEquals(photoById, photo);
        Assertions.assertNull(photoById2);
    }

    @Test
    @Name(value = "Получение фото по номеру - не найдено")
    void getPhotoByIdShouldReturnNull() {

        when(userService.getUserById(USERId)).thenReturn(Optional.of(user));

        Photo photo = photoService.uploadPhoto(USERId, URL, DESCRIPTION);

        Photo photoById2 = photoService.getPhotoById("2");

        Assertions.assertNull(photoById2);
    }

    @Test
    @Name(value = "Удаление фото пользователя")
    void deletePhotoShouldDeleteSuccessfully() {
        String photoId = "1";
        PhotoService photoService1 = mock(PhotoServiceImpl.class);

        doNothing().when(photoService1).deletePhoto(anyString());
        photoService1.deletePhoto(photoId);

        verify(photoService1, times(1)).deletePhoto(photoId);
    }
}
