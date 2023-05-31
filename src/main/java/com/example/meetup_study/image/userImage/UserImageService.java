package com.example.meetup_study.image.userImage;

import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.image.userImage.domain.dto.UserImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserImageService {

    //path
    Optional<UserImage> createUserImage(String path, Long userId);
    Optional<UserImage> getUserImage(Long userId);
    Optional<UserImage> updateUserImage(String path, Long userId);
    Optional<UserImage> deleteUserImage(Long userId);

    //byte CREUD
    Optional<UserImageDto> uploadUserImage(MultipartFile file, Long userId) throws Exception;
    Optional<UserImageDto> updateUserImagee(MultipartFile file, Long userId);

    Optional<UserImageDto> getUserImagee(Long userId);
    Optional<UserImageDto> deleteUserImagee(Long userId);

}
