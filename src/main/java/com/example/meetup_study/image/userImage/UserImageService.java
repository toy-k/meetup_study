package com.example.meetup_study.image.userImage;

import com.example.meetup_study.image.userImage.domain.UserImage;

import java.util.Optional;

public interface UserImageService {
    Optional<UserImage> createUserImage(String path, Long userId);
    Optional<UserImage> getUserImage(Long userId);
    Optional<UserImage> updateUserImage(String path, Long userId);
    Optional<UserImage> deleteUserImage(Long userId);
}
