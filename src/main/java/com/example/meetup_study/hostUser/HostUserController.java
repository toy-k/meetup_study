package com.example.meetup_study.hostUser;

import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.hostUser.domain.dto.HostUserDto;
import com.example.meetup_study.hostUser.domain.dto.RequestHostUserDto;
import com.example.meetup_study.mapper.HostUserMapper;
import com.example.meetup_study.room.service.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.user.service.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hostUser")
public class HostUserController {

    private final HostUserService hostUserService;
    private final UserService userService;
    private final RoomService roomService;
    private final HostUserMapper hostUserMapper;

    @ApiOperation(value = "호스트 유저 조회", notes = "호스트 유저를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "호스트 유저 id", required = true, dataTypeClass = Long.class, paramType = "path")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<HostUserDto> getHostUser(@PathVariable Long id){
        Optional<HostUser> hostUseropt =  hostUserService.getHostUserById(id);

        HostUserDto hostUserDto = hostUserMapper.toHostUserDto(hostUseropt.get());

        return ResponseEntity.ok(hostUserDto);
    }

    @ApiOperation(value = "호스트 유저 조회", notes = "호스트 유저를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "호스트 유저 userId", required = true, dataTypeClass = Long.class, paramType = "path"),
            @ApiImplicitParam(name = "roomId", value = "호스트 유저 roomId", required = true, dataTypeClass = Long.class, paramType = "path")
    })
    @GetMapping("/ids")
    public ResponseEntity<HostUserDto> getHostUser(@Valid @RequestBody RequestHostUserDto requestHostUserDto) {
        Optional<User> userOpt = userService.findById(requestHostUserDto.getUserId());
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<Room> roomOpt = roomService.getRoom(requestHostUserDto.getRoomId());
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<HostUser> hostUseropt = hostUserService.getHostUserByUserIdAndRoomId(requestHostUserDto.getUserId(), requestHostUserDto.getRoomId());

        HostUserDto hostUserDto = hostUserMapper.toHostUserDto(hostUseropt.get());

        return ResponseEntity.ok(hostUserDto);

    }

}
