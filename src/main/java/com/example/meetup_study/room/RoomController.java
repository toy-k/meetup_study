package com.example.meetup_study.room;

import com.example.meetup_study.Category.CategoryService;
import com.example.meetup_study.Category.domain.Category;
import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestDeleteRoomDto;
import com.example.meetup_study.room.domain.dto.RequestRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
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
import lombok.extern.slf4j.Slf4j;
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
    private final UserService userService;
    private final CategoryService categoryService;


    private String ACCESSTOKEN = "AccessToken";


    @ApiOperation(value = "방 생성", notes = "방을 생성합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestRoomDto.class, required = true, paramType = "body")
    })
    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody RequestRoomDto requestRoomDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        if(userOpt.get().getId() != requestRoomDto.getHostUserId()){
            throw new RoomInvalidRequestException("이 유저는 없거나, 방을 만들지 않았습니다.");
        }

        if(requestRoomDto.getMeetupEndDate().isBefore(requestRoomDto.getMeetupStartDate())){
            throw new RoomInvalidRequestException("시작 날짜가 끝나는 날짜보다 늦습니다.");
        }

        if(requestRoomDto.getPrice() < 0){
            throw new RoomInvalidRequestException("가격이 0보다 작습니다.");
        }

        requestRoomDto.setCurrentJoinNumber(1);
        requestRoomDto.setViewCount(1L);


        Optional<RoomDto> createdRoomDto = roomService.createRoom(requestRoomDto);

        return ResponseEntity.ok(createdRoomDto.get());
    }

    @ApiOperation(value = "방 조회", notes = "방을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "id", value = "방 id", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable Long id){

        Optional<Room> roomOpt = roomService.getRoom(id);
        RoomDto roomDtoOpt = new RoomDto().convertToRoomDto(roomOpt.get());
        return ResponseEntity.ok(roomDtoOpt);
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
    @PutMapping()
    public ResponseEntity<RoomDto> updateRoom(@Valid @RequestBody RoomDto roomDto, HttpServletRequest req) throws AccessDeniedException {


        String accessToken = req.getAttribute(ACCESSTOKEN).toString();


        Optional<Long> userId = jwtService.extractUserId(accessToken);
        if(!userId.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userId.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        if(userOpt.get().getId() != roomDto.getHostUserId()){
            throw new RoomInvalidRequestException("이 유저는 없거나, 방을 만들지 않았습니다.");
        }

        if(roomDto.getMeetupEndDate().isBefore(roomDto.getMeetupStartDate())){
            throw new RoomInvalidRequestException("시작 날짜가 끝나는 날짜보다 늦습니다.");
        }

        Optional<Category> categoryOpt = categoryService.getCategory(roomDto.getCategory());
        if(!categoryOpt.isPresent()){
            throw new RoomInvalidRequestException("카테고리가 없습니다.");
        }

        if(roomDto.getPrice() < 0){
            throw new RoomInvalidRequestException("가격이 0보다 작습니다.");
        }


        Optional<RoomDto> updatedRoomDto = roomService.updateRoom(roomDto, userOpt.get().getId());

        return ResponseEntity.ok(updatedRoomDto.get());
    }

    @ApiOperation(value = "방 삭제", notes = "방을 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestDeleteRoomDto.class, required = true, paramType = "body")
    })
    @DeleteMapping
    public ResponseEntity<RoomDto> deleteRoom(@RequestBody RequestDeleteRoomDto requestDeleteRoomDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userId = jwtService.extractUserId(accessToken);

        if(!userId.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userId.get());

        Optional<Room> roomOpt = roomService.getRoom(requestDeleteRoomDto.getId());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }
        if(!roomOpt.isPresent()) {
            throw new RoomNotFoundException();
        }
        if(userOpt.get().getId() != roomOpt.get().getHostUserList().get(0).getUser().getId()) {
            throw new UserInvalidRequestException("이 유저는  방을 만들지 않았습니다.");
        }

        Optional<RoomDto> deletedRoomDto = roomService.deleteRoom(requestDeleteRoomDto.getId(), userOpt.get().getId());

        return ResponseEntity.ok(deletedRoomDto.get());
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
