package com.example.meetup_study.user.controller;

import com.example.meetup_study.common.jwt.JwtService;
import com.example.meetup_study.user.service.UserService;
import com.example.meetup_study.user.domain.dto.RequestUserDto;
import com.example.meetup_study.user.domain.dto.UserDto;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    private String ACCESSTOKEN = "AccessToken";

    @ApiOperation(value = "특정 유저 정보 조회(id)", notes = "")
    @ApiImplicitParam(name = "id", value = "유저 id", required = true, dataType = "long", paramType = "path")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 404, message = "유저를 찾을 수 없습니다.", response = UserNotFoundException.class),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id){

        Optional<UserDto> userDto = userService.findByIdWithUserImage(id);
        return ResponseEntity.ok(userDto.get());
    }

    @ApiOperation(value = "특정 유저 정보 조회(usernmae)", notes = "")
    @ApiImplicitParam(name = "username", value = "유저 username", required = true, dataType = "String", paramType = "path")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 404, message = "유저를 찾을 수 없습니다.", response = UserNotFoundException.class),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> findUserByUsername(@PathVariable String username){

        Optional<UserDto> userDto = userService.findByUsernameWithUserImage(username);
        return ResponseEntity.ok(userDto.get());
    }

    //user list
    @ApiOperation(value = "유저 리스트 조회", notes = "")
    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> findAllUser(){

        List<UserDto> userList = userService.findAllUserWithUserImage();
        return ResponseEntity.ok(userList);
    }


    @ApiOperation(value = "내 정보 조회", notes = "")
    @GetMapping("/me")
    public ResponseEntity<UserDto> findMeByToken(HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();
        Optional<Long> userId = jwtService.extractUserId(accessToken);

        Optional<UserDto> userDto = userService.findByIdWithUserImage(userId.get());
        return ResponseEntity.ok(userDto.get());
    }

    @ApiOperation(value = "내 정보 수정", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "Request body", dataTypeClass = RequestUserDto.class, required = true, paramType = "body"),
    })
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMe(HttpServletRequest req, @RequestBody RequestUserDto requestUserDto){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();
        Optional<Long> userId = jwtService.extractUserId(accessToken);

        Optional<UserDto> updatedUserDto = userService.updateUser(userId.get(), requestUserDto);
        return ResponseEntity.ok(updatedUserDto.get());
    }


}
