package com.example.meetup_study.image.userImage;

import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.image.userImage.domain.dto.UserImageDto;
import com.example.meetup_study.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userImage")
public class UserImageController {
    private final UserImageService userImageService;
    private final UserService userService;
    private final JwtService jwtService;

    private String ACCESSTOKEN = "AccessToken";

    @PostMapping
    public ResponseEntity<UserImageDto> uploadUserImagee(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws Exception {

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if (!userIdOpt.isPresent()) {
            throw new IllegalArgumentException("유저Id가 없습니다.");
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("이미지가 없습니다.");
        }

        Optional<UserImageDto> userImageDtoOpt = userImageService.uploadUserImage(file, userIdOpt.get());

        if(!userImageDtoOpt.isPresent()) throw new IllegalArgumentException("이미지 업로드에 실패했습니다.");

        return ResponseEntity.ok(userImageDtoOpt.get());
    }

    @PutMapping
    public ResponseEntity<UserImageDto> updateUserImagee(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws Exception {

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if (!userIdOpt.isPresent()) {
            throw new IllegalArgumentException("유저Id가 없습니다.");
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("이미지가 없습니다.");
        }

        Optional<UserImageDto> userImageDtoOpt = userImageService.updateUserImagee(file, userIdOpt.get());

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
    public ResponseEntity<UserImageDto> deleteUserImagee(HttpServletRequest req) {
        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if (!userIdOpt.isPresent()) {
            throw new IllegalArgumentException("유저Id가 없습니다.");
        }

        Optional<UserImageDto> userImageDtoOpt = userImageService.deleteUserImagee(userIdOpt.get());

        if(!userImageDtoOpt.isPresent()) throw new IllegalArgumentException("이미지 삭제 실패했습니다.");

        return ResponseEntity.ok(userImageDtoOpt.get());
    }
}
