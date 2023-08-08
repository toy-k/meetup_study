package com.example.meetup_study.image.roomImage;

import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.hostUser.HostUserService;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.image.exception.ImageInvalidRequestException;
import com.example.meetup_study.image.exception.ImageNotFoundException;
import com.example.meetup_study.image.roomImage.domain.dto.RequestDeleteRoomImageDto;
import com.example.meetup_study.image.roomImage.domain.dto.RequestRoomImageDto;
import com.example.meetup_study.image.roomImage.domain.dto.RoomImageDto;
import com.example.meetup_study.room.service.RoomService;
import com.example.meetup_study.room.domain.Room;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roomImage")
public class RoomImageController {

    private final RoomImageService roomImageService;
    private final RoomService roomService;
    private final JwtService jwtService;
    private final UserService userService;
    private final HostUserService hostUserService;

    private String ACCESSTOKEN = "AccessToken";

    @ApiOperation(value = "방 이미지 등록", notes = "방 이미지를 등록합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "이미지 파일", required = true, dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "body", value = "Request Body", dataTypeClass = RequestRoomImageDto.class, required = true, paramType = "body")
    })
    @PutMapping
    public ResponseEntity<RoomImageDto> updateRoomImage(@RequestParam("file") MultipartFile file, @Valid @RequestBody RequestRoomImageDto requestRoomImageDto, HttpServletRequest req) throws Exception {

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<Room> roomOpt = roomService.getRoom(requestRoomImageDto.getRoomId());
        if (!roomOpt.isPresent()) throw new RoomNotFoundException();


        Optional<HostUser> hostUserOpt = hostUserService.getHostUserByRoomId(requestRoomImageDto.getRoomId());
        if (!hostUserOpt.isPresent()) throw new UserNotFoundException();
        if (hostUserOpt.get().getUser().getId() != userIdOpt.get()) throw new UserInvalidRequestException("해당 방의 방장이 아닙니다.");

        if (file.isEmpty()) {
            throw new ImageNotFoundException();
        }

        Optional<RoomImageDto> roomImageDtoOpt = roomImageService.updateRoomImage(file, requestRoomImageDto.getRoomId());

        if(!roomImageDtoOpt.isPresent()) throw new ImageNotFoundException();

        return ResponseEntity.ok(roomImageDtoOpt.get());
    }

    @ApiOperation(value = "방 이미지 조회", notes = "방 이미지를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "방 ID", required = true, dataType = "long", paramType = "path")
    })
    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<RoomImageDto> getRoomImage(@PathVariable Long roomId) {

        Optional<Room> roomOpt = roomService.getRoom(roomId);

        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<RoomImageDto> roomImageDtoOpt = roomImageService.getRoomImage(roomId);

        if(!roomImageDtoOpt.isPresent()) throw new ImageNotFoundException();

        return ResponseEntity.ok(roomImageDtoOpt.get());
    }

    @ApiOperation(value = "방 이미지 삭제", notes = "방 이미지를 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "Request Body", dataTypeClass = RequestDeleteRoomImageDto.class, required = true, paramType = "body")
    })
    @DeleteMapping
    public ResponseEntity<RoomImageDto> deleteRoomImage(@Valid @RequestBody RequestDeleteRoomImageDto requestDeleteRoomImageDto, HttpServletRequest req) {
        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<Room> roomOpt = roomService.getRoom(requestDeleteRoomImageDto.getRoomId());
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<RoomImageDto> roomImageDtoOpt = roomImageService.deleteRoomImage(requestDeleteRoomImageDto.getRoomId());

        if(!roomImageDtoOpt.isPresent()) throw new ImageInvalidRequestException();

        return ResponseEntity.ok(roomImageDtoOpt.get());
    }

}
