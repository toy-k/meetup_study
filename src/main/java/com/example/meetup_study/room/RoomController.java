package com.example.meetup_study.room;

import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestDeleteRoomDto;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;
    private final JwtService jwtService;
    private final UserService userService;


    private String ACCESSTOKEN = "AccessToken";

    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@RequestBody RequestRoomDto requestRoomDto, HttpServletRequest req){
        log.debug("[RoomController] createRoom()");

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<User> userOpt = userService.findById(jwtService.extractUserId(accessToken).get());

        if(!userOpt.isPresent() || userOpt.get().getId() != requestRoomDto.getHostUserId()){
            throw new IllegalArgumentException("이 유저는 없거나, 방을 만들지 않았습니다.");
        }

        Optional<Room> createdRoom = roomService.createRoom(requestRoomDto, userOpt.get().getId());

        Optional<RoomDto> createdRoomDto = createdRoom.map(r -> new RoomDto().convertToRoomDto(r));

        return ResponseEntity.ok(createdRoomDto.get());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable Long id){
        log.debug("[RoomController] getRoom()");

        Optional<Room> roomOpt = roomService.getRoom(id);
        if(roomOpt.isPresent()){
            RoomDto roomDtoOpt = new RoomDto().convertToRoomDto(roomOpt.get());
            return ResponseEntity.ok(roomDtoOpt);
        }else{
            throw new IllegalArgumentException("방이 없습니다.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<RoomDto>> getRoomList(){
        log.debug("[RoomController] getRoomList()");

        List<RoomDto> roomDtos = roomService.getRoomList();

        return ResponseEntity.ok(roomDtos);

    }

    @GetMapping("/list/before-meetup-start")
    public ResponseEntity<List<RoomDto>> getRoomListBeforeMeetupStart(){
        log.debug("[RoomController] getRoomListBeforeMeetupStart()");

        List<RoomDto> roomDtos = roomService.getRoomListBeforeMeetupStart();

        return ResponseEntity.ok(roomDtos);

    }

    @GetMapping("/list/after-meetup-start")
    public ResponseEntity<List<RoomDto>> getRoomListAfterMeetupStart(){
        log.debug("[RoomController] getRoomListAfterMeetupStart()");

        List<RoomDto> roomDtos = roomService.getRoomListAfterMeetupStart();

        return ResponseEntity.ok(roomDtos);

    }

    @PutMapping()
    public ResponseEntity<RoomDto> updateRoom(@RequestBody RoomDto RoomDto, HttpServletRequest req){
        log.debug("[RoomController] updateRoom()");

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userId = jwtService.extractUserId(accessToken);
        if(!userId.isPresent()){
            throw new IllegalArgumentException("토큰에 유저가 없습니다.");
        }

        Optional<User> userOpt = userService.findById(jwtService.extractUserId(accessToken).get());

        if(!userOpt.isPresent() || userOpt.get().getId() != RoomDto.getHostUserId()){
            throw new IllegalArgumentException("이 유저는 없거나, 방을 만들지 않았습니다.");
        }

        Optional<RoomDto> updatedRoomDto = roomService.updateRoom(RoomDto, userOpt.get().getId());

        return ResponseEntity.ok(updatedRoomDto.get());
    }

    @DeleteMapping
    public ResponseEntity<RoomDto> deleteRoom(@RequestBody RequestDeleteRoomDto requestDeleteRoomDto, HttpServletRequest req){
        log.debug("[RoomController] deleteRoom()");

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userId = jwtService.extractUserId(accessToken);
        if(!userId.isPresent()){
            throw new IllegalArgumentException("토큰에 유저가 없습니다.");
        }

        Optional<User> userOpt = userService.findById(jwtService.extractUserId(accessToken).get());

        Optional<Room> roomOpt = roomService.getRoom(requestDeleteRoomDto.getId());

        if(!userOpt.isPresent() || !roomOpt.isPresent() ||userOpt.get().getId() != roomOpt.get().getHostUserId()){
            throw new IllegalArgumentException("이 유저는 없거나, 방이 없거나, 방을 만들지 않았습니다.");
        }

        Optional<RoomDto> deletedRoomDto = roomService.deleteRoom(requestDeleteRoomDto.getId(), userOpt.get().getId());

        return ResponseEntity.ok(deletedRoomDto.get());
    }

    //junit test
    private ResponseEntity<String> deleteAllRooms(){
        roomService.deleteAllRooms();
        return ResponseEntity.ok("delete all rooms");
    }
}
