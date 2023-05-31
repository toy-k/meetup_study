package com.example.meetup_study.image.userImage;

import com.example.meetup_study.image.userImage.domain.dto.UserImageDto;
import com.example.meetup_study.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userImage")
public class UserImageController {
    private final UserImageService userImageService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserImageDto> uploadUserImagee(@RequestParam("file") MultipartFile file, Long userId) throws Exception {

        Optional<UserImageDto> userImageDtoOpt = userImageService.uploadUserImage(file, userId);

        if(!userImageDtoOpt.isPresent()) throw new IllegalArgumentException("이미지 업로드에 실패했습니다.");

        return ResponseEntity.ok(userImageDtoOpt.get());
    }

    @PutMapping
    public ResponseEntity<UserImageDto> updateUserImagee(@RequestParam("file") MultipartFile file, Long userId) throws Exception {

        Optional<UserImageDto> userImageDtoOpt = userImageService.updateUserImagee(file, userId);

        if(!userImageDtoOpt.isPresent()) throw new IllegalArgumentException("이미지 업로드에 실패했습니다.");

        return ResponseEntity.ok(userImageDtoOpt.get());
    }

    @GetMapping
    public ResponseEntity<UserImageDto> getUserImagee(Long userId) {

        Optional<UserImageDto> userImageDtoOpt = userImageService.getUserImagee(userId);

        if(!userImageDtoOpt.isPresent()) throw new IllegalArgumentException("이미지 불러오기 실패했습니다.");

        return ResponseEntity.ok(userImageDtoOpt.get());
    }

    @DeleteMapping
    public ResponseEntity<UserImageDto> deleteUserImagee(Long userId) {

        Optional<UserImageDto> userImageDtoOpt = userImageService.deleteUserImagee(userId);

        if(!userImageDtoOpt.isPresent()) throw new IllegalArgumentException("이미지 삭제 실패했습니다.");

        return ResponseEntity.ok(userImageDtoOpt.get());
    }
}
