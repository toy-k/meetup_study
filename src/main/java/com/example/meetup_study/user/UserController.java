package com.example.meetup_study.user;

import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.user.domain.dto.UserDto;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    private String ACCESSTOKEN = "AccessToken";

    @ApiOperation(value = "유저 정보 조회", notes = "유저 정보 조회")
    @GetMapping("/id/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id){

        Optional<UserDto> userDto = userService.findByIdReturnDto(id);
        if(userDto.isPresent()){
            return ResponseEntity.ok(userDto.get());
        }
        else {
            throw new UserNotFoundException();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> findMeByToken(HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userId = jwtService.extractUserId(accessToken);

        if(userId.isPresent()){
            Optional<UserDto> userDto = userService.findByIdReturnDto(userId.get());
            if(userDto.isPresent()){
                return ResponseEntity.ok(userDto.get());
            }
            else {
                throw new UserNotFoundException();
            }
        }else{
            throw new AccessTokenInvalidRequestException();
        }
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMe(HttpServletRequest req, @RequestBody UserDto userDto){
        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userId = jwtService.extractUserId(accessToken);
        if (userId.isPresent()) {
            Optional<UserDto> updatedUserDto = userService.updateUser(userId.get(), userDto);
            if (updatedUserDto.isPresent()) {
                return ResponseEntity.ok(updatedUserDto.get());
            } else {
                throw new UserNotFoundException();
            }
        }else {
            throw new AccessTokenInvalidRequestException();
        }
    }


}
