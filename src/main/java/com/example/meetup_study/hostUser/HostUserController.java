package com.example.meetup_study.hostUser;

import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.hostUser.domain.dto.HostUserDto;
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

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hostUser")
public class HostUserController {

    private final HostUserService hostUserService;
    private final UserService userService;
    private final RoomService roomService;

    @GetMapping("/id")
    public ResponseEntity<HostUserDto> getHostUser(Long id){
        Optional<HostUser> hostUseropt =  hostUserService.getHostUserById(id);

        HostUserDto hostUserDto = new HostUserDto().convertToHostUserDto(hostUseropt.get());

        return ResponseEntity.ok(hostUserDto);
    }

    @GetMapping("ids")
    public ResponseEntity<HostUserDto> getHostUser(Long userId, Long roomId) {
        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<HostUser> hostUseropt = hostUserService.getHostUserByUserIdAndRoomId(userId, roomId);

        HostUserDto hostUserDto = new HostUserDto().convertToHostUserDto(hostUseropt.get());

        return ResponseEntity.ok(hostUserDto);

    }

}
