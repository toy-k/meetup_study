package com.example.meetup_study.joinedUser;

import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.domain.dto.JoinedUserDto;
import com.example.meetup_study.joinedUser.domain.dto.RequestJoinedUserDto;
import com.example.meetup_study.joinedUser.exception.JoinedUserNotFoundException;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.exception.RoomInvalidRequestException;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserInvalidRequestException;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/joinedUser")
public class JoinedUserController {

    private final JoinedUserService joinedUserService;
    private final UserService userService;
    private final RoomService roomService;
    private final JwtService jwtService;

    private String ACCESSTOKEN = "AccessToken";

    @PostMapping
    public ResponseEntity<JoinedUserDto> joinRoom(@Valid @RequestBody RequestJoinedUserDto requestJoinedUserDto, HttpServletRequest req){

        System.out.println("AAA =" + req.getAttribute(ACCESSTOKEN));

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();


        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        if(userOpt.get().getId() != requestJoinedUserDto.getUserId()){
            throw new UserInvalidRequestException("유저 아이디가 일치하지 않습니다.");
        }

        Optional<JoinedUser> joinedUseropt = joinedUserService.joinRoom(requestJoinedUserDto.getUserId(), requestJoinedUserDto.getRoomId());

        JoinedUserDto joinedUserDto = new JoinedUserDto().convertToJoinedUserDto(joinedUseropt.get());

        return ResponseEntity.ok(joinedUserDto);
    }

    @DeleteMapping
    public ResponseEntity<JoinedUserDto> leaveRoom(@Valid @RequestBody RequestJoinedUserDto requestJoinedUserDto, HttpServletRequest req){
        String accessToken = req.getAttribute(ACCESSTOKEN).toString();


        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        if(userOpt.get().getId() != requestJoinedUserDto.getUserId()){
            throw new UserInvalidRequestException("유저 아이디가 일치하지 않습니다.");
        }

        Optional<JoinedUser> joinedUseropt = joinedUserService.leaveRoom(requestJoinedUserDto.getUserId(),requestJoinedUserDto.getRoomId());

        JoinedUserDto joinedUserDto = new JoinedUserDto().convertToJoinedUserDto(joinedUseropt.get());

        return ResponseEntity.ok(joinedUserDto);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JoinedUserDto> getJoinedUser(@PathVariable Long id){
        Optional<JoinedUser> joinedUseropt =  joinedUserService.getJoinedUserById(id);

        if(joinedUseropt.isPresent()){
            JoinedUserDto joinedUserDto = new JoinedUserDto().convertToJoinedUserDto(joinedUseropt.get());
            return ResponseEntity.ok(joinedUserDto);

        }else{
            throw new JoinedUserNotFoundException();
        }

    }

    @GetMapping("/ids")
    public ResponseEntity<JoinedUserDto> getJoinedUser(@RequestParam Long userId, @RequestParam Long roomId) {
        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<JoinedUser> joinedUseropt = joinedUserService.getJoinedUserByUserIdAndRoomId(userId, roomId);

        if (joinedUseropt.isPresent()) {
            JoinedUserDto joinedUserDto = new JoinedUserDto().convertToJoinedUserDto(joinedUseropt.get());
            return ResponseEntity.ok(joinedUserDto);

        } else {
            throw new JoinedUserNotFoundException();
        }
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<List<JoinedUserDto>> getJoinedUserByUserId(@PathVariable Long userId){

        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        List<JoinedUser> joinedUseropt =  joinedUserService.getJoinedUserByUserId(userId);

        List<JoinedUserDto> joinedUserDtos = new ArrayList<>();

        for(JoinedUser joinedUser : joinedUseropt){
            joinedUserDtos.add(new JoinedUserDto().convertToJoinedUserDto(joinedUser));
        }

        return ResponseEntity.ok(joinedUserDtos);


    }

    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<List<JoinedUserDto>> getJoinedUserByRoomId(@PathVariable Long roomId) {

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        List<JoinedUser> joinedUseropt = joinedUserService.getJoinedUserByRoomId(roomId);

        List<JoinedUserDto> joinedUserDtos = new ArrayList<>();

        for (JoinedUser joinedUser : joinedUseropt) {
            joinedUserDtos.add(new JoinedUserDto().convertToJoinedUserDto(joinedUser));
        }

        return ResponseEntity.ok(joinedUserDtos);
    }

}
