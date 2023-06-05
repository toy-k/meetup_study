package com.example.meetup_study.joinedUser;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.domain.dto.JoinedUserDto;
import com.example.meetup_study.joinedUser.exception.JoinedUserNotFoundException;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("/id")
    public ResponseEntity<JoinedUserDto> getJoinedUser(Long id){
        Optional<JoinedUser> joinedUseropt =  joinedUserService.getJoinedUserById(id);

        if(joinedUseropt.isPresent()){
            JoinedUserDto joinedUserDto = new JoinedUserDto().convertToJoinedUserDto(joinedUseropt.get());
            return ResponseEntity.ok(joinedUserDto);

        }else{
            throw new JoinedUserNotFoundException();
        }



    }

    @GetMapping("ids")
    public ResponseEntity<JoinedUserDto> getJoinedUser(Long userId, Long roomId) {
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

    @GetMapping("/userId")
    public ResponseEntity<List<JoinedUserDto>> getJoinedUserByUserId(Long userId){

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

    @GetMapping("/roomId")
    public ResponseEntity<List<JoinedUserDto>> getJoinedUserByRoomId(Long roomId) {

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

    @GetMapping("/join")
    public ResponseEntity<JoinedUserDto> joinRoom(Long userId, Long roomId){

        Optional<JoinedUser> joinedUseropt = joinedUserService.joinRoom(userId, roomId);

        JoinedUserDto joinedUserDto = new JoinedUserDto().convertToJoinedUserDto(joinedUseropt.get());

        return ResponseEntity.ok(joinedUserDto);
    }

    @GetMapping("/leave")
    public ResponseEntity<JoinedUserDto> leaveRoom(Long userId, Long roomId){

        Optional<JoinedUser> joinedUseropt = joinedUserService.leaveRoom(userId, roomId);

        JoinedUserDto joinedUserDto = new JoinedUserDto().convertToJoinedUserDto(joinedUseropt.get());

        return ResponseEntity.ok(joinedUserDto);
    }
}
