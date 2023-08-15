package com.example.meetup_study.joinedUser.controller;

import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.joinedUser.service.JoinedUserService;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.domain.dto.JoinedUserDto;
import com.example.meetup_study.joinedUser.domain.dto.RequestJoinedUserDto;
import com.example.meetup_study.joinedUser.exception.JoinedUserNotFoundException;
import com.example.meetup_study.mapper.JoinedUserMapper;
import com.example.meetup_study.room.service.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.user.service.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserInvalidRequestException;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    private final JwtService jwtService;

    private String ACCESSTOKEN = "AccessToken";

    @ApiOperation(value = "방 참여", notes = "방에 참여합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestJoinedUserDto.class, required = true, paramType = "body")
    })
    @PostMapping
    public ResponseEntity<JoinedUserDto> joinRoom(@Valid @RequestBody RequestJoinedUserDto requestJoinedUserDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();
        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<JoinedUserDto> joinedUserDtoOpt = joinedUserService.joinRoom(requestJoinedUserDto, userIdOpt.get());

        return ResponseEntity.ok(joinedUserDtoOpt.get());
    }

    @ApiOperation(value = "방 나가기", notes = "방에서 나갑니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestJoinedUserDto.class, required = true, paramType = "body")
    })
    @DeleteMapping
    public ResponseEntity<JoinedUserDto> leaveRoom(@Valid @RequestBody RequestJoinedUserDto requestJoinedUserDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();
        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<JoinedUserDto> joinedUserDtoOpt = joinedUserService.leaveRoom(requestJoinedUserDto,userIdOpt.get());

        return ResponseEntity.ok(joinedUserDtoOpt.get());
    }

    @ApiOperation(value = "방 참여자 조회", notes = "방에 참여한 유저들을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "roomId", value = "방 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<JoinedUserDto> getJoinedUser(@PathVariable Long id){

        Optional<JoinedUserDto> joinedUserDtoOpt =  joinedUserService.getJoinedById(id);

        return ResponseEntity.ok(joinedUserDtoOpt.get());

    }

    @ApiOperation(value = "방 참여자 조회", notes = "방에 참여한 유저를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "roomId", value = "방 아이디", dataTypeClass = Long.class, required = true, paramType = "path"),
            @ApiImplicitParam(name= "userId", value = "유저 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/ids/roomId/{roomId}/userId/{userId}")
    public ResponseEntity<JoinedUserDto> getJoinedUser(@PathVariable Long userId, @PathVariable Long roomId) {

        Optional<JoinedUserDto> joinedUserDtoOpt = joinedUserService.getJoinedUserByUserIdAndRoomId(userId, roomId);

        return ResponseEntity.ok(joinedUserDtoOpt.get());

    }

    @ApiOperation(value = "참여 방들 조회", notes = "한 유저가 참여한 방들을 조회합니다")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "userId", value = "유저 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/userId/{userId}")
    public ResponseEntity<List<JoinedUserDto>> getJoinedUserByUserId(@PathVariable Long userId){

        List<JoinedUserDto> joinedUserDtoList =  joinedUserService.getJoinedUserByUserId(userId);

        return ResponseEntity.ok(joinedUserDtoList);


    }

    @ApiOperation(value = "방 참여자들 조회", notes = "방에 참여한 유저들을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "roomId", value = "방 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<List<JoinedUserDto>> getJoinedUserByRoomId(@PathVariable Long roomId) {

        List<JoinedUserDto> joinedUserDtoList = joinedUserService.getJoinedUserByRoomId(roomId);

        return ResponseEntity.ok(joinedUserDtoList);
    }

}
