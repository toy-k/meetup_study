package com.example.meetup_study.user;

import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.user.domain.dto.UserDto;
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

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id){
        log.debug("[UserController] findUserById()");

        Optional<UserDto> userDto = userService.findByIdReturnDto(id);
        if(userDto.isPresent()){
            return ResponseEntity.ok(userDto.get());
        }
        else {
            throw new IllegalArgumentException("유저가 없습니다.");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> findMeByToken(HttpServletRequest req){
        log.debug("[UserController] findMeByToken()");

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userId = jwtService.extractUserId(accessToken);

        if(userId.isPresent()){
            Optional<UserDto> userDto = userService.findByIdReturnDto(userId.get());
            if(userDto.isPresent()){
                return ResponseEntity.ok(userDto.get());
            }
            else {
                throw new IllegalArgumentException("유저가 없습니다.");
            }
        }else{
            throw new IllegalArgumentException("유저Id가 없습니다.");
        }
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMe(HttpServletRequest req, @RequestBody UserDto userDto){
        log.debug("[UserController] updateMe()");
        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userId = jwtService.extractUserId(accessToken);
        if (userId.isPresent()) {
            Optional<UserDto> updatedUserDto = userService.updateUser(userId.get(), userDto);
            if (updatedUserDto.isPresent()) {
                return ResponseEntity.ok(updatedUserDto.get());
            } else {
                throw new IllegalArgumentException("유저가 없습니다.");
            }
        }else {
            throw new IllegalArgumentException("유저Id가 없습니다.");
        }
    }


}
