package com.example.meetup_study.image.userImage.service;

import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.image.userImage.domain.dto.UserImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserImageService {

   Optional<UserImageDto> updateUserImagee(MultipartFile file, Long userId);

    Optional<UserImageDto> getUserImagee(Long userId);

    //    Optional<byte[]> getUserImageee(Long userId);

    Optional<UserImageDto> deleteUserImagee(Long userId);

}
