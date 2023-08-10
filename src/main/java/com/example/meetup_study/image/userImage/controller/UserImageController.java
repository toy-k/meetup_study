package com.example.meetup_study.image.userImage.controller;

import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.image.userImage.service.UserImageService;
import com.example.meetup_study.image.userImage.domain.dto.UserImageDto;
import com.example.meetup_study.user.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "유저 이미지 생성", notes = "유저 이미지를 생성합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "file", value = "Request File", dataTypeClass = MultipartFile.class, required = true, paramType = "form")
    })
    @PutMapping
    public ResponseEntity<UserImageDto> updateUserImagee(@RequestParam("file") MultipartFile file, HttpServletRequest req) throws Exception {

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();
        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<UserImageDto> userImageDtoOpt = userImageService.updateUserImagee(file, userIdOpt.get());

        return ResponseEntity.ok(userImageDtoOpt.get());
    }

    @ApiOperation(value = "유저 이미지 조회", notes = "유저 이미지를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "userId", value = "유저 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/userId/{userId}")
    public ResponseEntity<UserImageDto> getUserImagee(@PathVariable Long userId) {

        Optional<UserImageDto> userImageDtoOpt = userImageService.getUserImagee(userId);

        return ResponseEntity.ok(userImageDtoOpt.get());
    }


    @ApiOperation(value = "유저 이미지 삭제", notes = "유저 이미지를 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<UserImageDto> deleteUserImagee(HttpServletRequest req) {
        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<UserImageDto> userImageDtoOpt = userImageService.deleteUserImagee(userIdOpt.get());

        return ResponseEntity.ok(userImageDtoOpt.get());
    }
}
