package com.example.meetup_study.room.controller;

import com.example.meetup_study.common.jwt.JwtService;
import com.example.meetup_study.mapper.RoomMapper;
import com.example.meetup_study.room.service.RoomService;
import com.example.meetup_study.room.domain.dto.RequestDeleteRoomDto;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;
    private final JwtService jwtService;

    private String ACCESSTOKEN = "AccessToken";


    @ApiOperation(value = "방 생성", notes = "방을 생성합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestRoomDto.class, required = true, paramType = "body")
    })
    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody RequestRoomDto requestRoomDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<RoomDto> createdRoomDto = roomService.createRoom(requestRoomDto, userIdOpt.get());

        return ResponseEntity.ok(createdRoomDto.get());
    }


    @ApiOperation(value = "방 조회", notes = "방을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "id", value = "방 id", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @Cacheable(value = "room", key = "#id")
    @GetMapping("/id/{id}")
    public RoomDto getRoom(@PathVariable Long id){

        Optional<RoomDto> roomDtoOpt = roomService.getRoomAndIncrementViewCount(id);

        return (roomDtoOpt.get());
    }


    @ApiOperation(value = "방들 조회", notes = "방들을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "page", value = "페이지", dataTypeClass = Integer.class, required = true, paramType = "query"),
            @ApiImplicitParam(name= "size", value = "사이즈", dataTypeClass = Integer.class, required = true, paramType = "query")
    })
    @GetMapping("/list")
    public ResponseEntity<List<RoomDto>> getRoomList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){

        if(page < 1 || size != 10){
            page = 1;
            size = 10;
        }

        List<RoomDto> roomDtos = roomService.getRoomList(page, size);

        return ResponseEntity.ok(roomDtos);

    }

    @ApiOperation(value = "모임 시작 전 방들 조회", notes = "모임 시작 전 방들을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "page", value = "페이지", dataTypeClass = Integer.class, required = true, paramType = "query"),
            @ApiImplicitParam(name= "size", value = "사이즈", dataTypeClass = Integer.class, required = true, paramType = "query")
    })
    @GetMapping("/list/after-meetup-start")
    public ResponseEntity<List<RoomDto>> getRoomListBeforeMeetupStart(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        if(page < 1 || size != 10){
            page = 1;
            size = 10;
        }

        List<RoomDto> roomDtos = roomService.getRoomListBeforeMeetupStart(page, size);

        return ResponseEntity.ok(roomDtos);

    }

    @ApiOperation(value = "모임 시작 후 방들 조회", notes = "모임 시작 후 방들을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "page", value = "페이지", dataTypeClass = Integer.class, required = true, paramType = "query"),
            @ApiImplicitParam(name= "size", value = "사이즈", dataTypeClass = Integer.class, required = true, paramType = "query")
    })
    @GetMapping("/list/before-meetup-start")
    public ResponseEntity<List<RoomDto>> getRoomListAfterMeetupStart(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        if(page < 1 || size != 10){
            page = 1;
            size = 10;
        }

        List<RoomDto> roomDtos = roomService.getRoomListAfterMeetupStart(page, size);

        return ResponseEntity.ok(roomDtos);

    }

    @ApiOperation(value = "방 수정", notes = "방을 수정합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RoomDto.class, required = true, paramType = "body")
    })
    @CachePut(value = "room", key = "#roomDto.id")
    @PutMapping()
    public RoomDto updateRoom(@Valid @RequestBody RoomDto roomDto, HttpServletRequest req) {

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();
        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<RoomDto> updatedRoomDto = roomService.updateRoom(roomDto, userIdOpt.get());

        return (updatedRoomDto.get());
    }


    @ApiOperation(value = "방 삭제", notes = "방을 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestDeleteRoomDto.class, required = true, paramType = "body")
    })
    @CacheEvict(value = "room", key = "#requestDeleteRoomDto.id")
    @DeleteMapping
    public Boolean deleteRoom(@Valid @RequestBody RequestDeleteRoomDto requestDeleteRoomDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();
        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Boolean res = roomService.deleteRoom(requestDeleteRoomDto.getId(), userIdOpt.get());

        return (res);
    }
    //방갯수
    @ApiOperation(value = "방 갯수", notes = "방 갯수를 조회합니다.")
    @GetMapping("/count")
    public ResponseEntity<Long> getRoomCount(){
        Long roomCount = roomService.getRoomCount();
        return ResponseEntity.ok(roomCount);
    }

    //junit test
    private ResponseEntity<String> deleteAllRooms(){
        roomService.deleteAllRooms();
        return ResponseEntity.ok("delete all rooms");
    }

}
