package com.example.meetup_study.joinedUser;

import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.domain.dto.JoinedUserDto;
import com.example.meetup_study.joinedUser.domain.dto.RequestJoinedUserDto;
import com.example.meetup_study.joinedUser.exception.JoinedUserNotFoundException;
import com.example.meetup_study.mapper.JoinedUserMapper;
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
    private final UserService userService;
    private final RoomService roomService;
    private final JwtService jwtService;
    private final JoinedUserMapper joinedUserMapper;

    private String ACCESSTOKEN = "AccessToken";

    @ApiOperation(value = "방 참여", notes = "방에 참여합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestJoinedUserDto.class, required = true, paramType = "body")
    })
    @PostMapping
    public ResponseEntity<JoinedUserDto> joinRoom(@Valid @RequestBody RequestJoinedUserDto requestJoinedUserDto, HttpServletRequest req){

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

        JoinedUserDto joinedUserDto = joinedUserMapper.toJoinedUserDto(joinedUseropt.get());

        return ResponseEntity.ok(joinedUserDto);
    }

    @ApiOperation(value = "방 나가기", notes = "방에서 나갑니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestJoinedUserDto.class, required = true, paramType = "body")
    })
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

        JoinedUserDto joinedUserDto = joinedUserMapper.toJoinedUserDto(joinedUseropt.get());

        return ResponseEntity.ok(joinedUserDto);
    }

    @ApiOperation(value = "방 참여자 조회", notes = "방에 참여한 유저들을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "roomId", value = "방 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<JoinedUserDto> getJoinedUser(@PathVariable Long id){
        Optional<JoinedUser> joinedUseropt =  joinedUserService.getJoinedUserById(id);

        if(joinedUseropt.isPresent()){
            JoinedUserDto joinedUserDto = joinedUserMapper.toJoinedUserDto(joinedUseropt.get());
            return ResponseEntity.ok(joinedUserDto);

        }else{
            throw new JoinedUserNotFoundException();
        }

    }

    @ApiOperation(value = "방 참여자 조회", notes = "방에 참여한 유저를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "roomId", value = "방 아이디", dataTypeClass = Long.class, required = true, paramType = "path"),
            @ApiImplicitParam(name= "userId", value = "유저 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/ids/roomId/{roomId}/userId/{userId}")
    public ResponseEntity<JoinedUserDto> getJoinedUser(@PathVariable Long userId, @PathVariable Long roomId) {
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
            JoinedUserDto joinedUserDto = joinedUserMapper.toJoinedUserDto(joinedUseropt.get());
            return ResponseEntity.ok(joinedUserDto);

        } else {
            throw new JoinedUserNotFoundException();
        }
    }

    @ApiOperation(value = "참여 방들 조회", notes = "한 유저가 참여한 방들을 조회합니다")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "userId", value = "유저 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/userId/{userId}")
    public ResponseEntity<List<JoinedUserDto>> getJoinedUserByUserId(@PathVariable Long userId){

        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        List<JoinedUser> joinedUseropt =  joinedUserService.getJoinedUserByUserId(userId);

        List<JoinedUserDto> joinedUserDtos = new ArrayList<>();

        for(JoinedUser joinedUser : joinedUseropt){
            joinedUserDtos.add(joinedUserMapper.toJoinedUserDto(joinedUser));
        }

        return ResponseEntity.ok(joinedUserDtos);


    }

    @ApiOperation(value = "방 참여자들 조회", notes = "방에 참여한 유저들을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "roomId", value = "방 아이디", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<List<JoinedUserDto>> getJoinedUserByRoomId(@PathVariable Long roomId) {

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        List<JoinedUser> joinedUseropt = joinedUserService.getJoinedUserByRoomId(roomId);

        List<JoinedUserDto> joinedUserDtos = new ArrayList<>();

        for (JoinedUser joinedUser : joinedUseropt) {
            joinedUserDtos.add(joinedUserMapper.toJoinedUserDto(joinedUser));
        }

        return ResponseEntity.ok(joinedUserDtos);
    }

}
