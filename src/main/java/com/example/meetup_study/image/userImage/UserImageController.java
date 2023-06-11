package com.example.meetup_study.image.userImage;

import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.image.exception.ImageInvalidRequestException;
import com.example.meetup_study.image.exception.ImageNotFoundException;
import com.example.meetup_study.image.userImage.domain.dto.UserImageDto;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


    @PutMapping
    public ResponseEntity<UserImageDto> updateUserImagee(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws Exception {

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        if (file.isEmpty()) {
            throw new ImageNotFoundException();
        }

        Optional<UserImageDto> userImageDtoOpt = userImageService.updateUserImagee(file, userIdOpt.get());

        if(!userImageDtoOpt.isPresent()) throw new ImageInvalidRequestException();

        return ResponseEntity.ok(userImageDtoOpt.get());
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<UserImageDto> getUserImagee(@PathVariable Long userId) {

        Optional<User> userOpt = userService.findById(userId);

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<UserImageDto> userImageDtoOpt = userImageService.getUserImagee(userId);

        if(!userImageDtoOpt.isPresent()) throw new ImageNotFoundException();

        return ResponseEntity.ok(userImageDtoOpt.get());
    }


//byte 응답 = 이미지 브라우저에 띄움

//    @GetMapping("/userId/{userId}")
//    public ResponseEntity<byte[]> getUserImageee(@PathVariable Long userId) {
//
//        Optional<User> userOpt = userService.findById(userId);
//
//        if (!userOpt.isPresent()) {
//            throw new UserNotFoundException();
//        }
//
//        Optional<byte[]> imageBytesOpt = userImageService.getUserImageee(userId);
//
//        if (!imageBytesOpt.isPresent()) {
//            throw new ImageNotFoundException();
//        }
//
//        byte[] imageBytes = imageBytesOpt.get();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_JPEG);
//
//        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//    }

    @DeleteMapping
    public ResponseEntity<UserImageDto> deleteUserImagee(HttpServletRequest req) {
        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<UserImageDto> userImageDtoOpt = userImageService.deleteUserImagee(userIdOpt.get());

        if(!userImageDtoOpt.isPresent()) throw new ImageInvalidRequestException();

        return ResponseEntity.ok(userImageDtoOpt.get());
    }
}
