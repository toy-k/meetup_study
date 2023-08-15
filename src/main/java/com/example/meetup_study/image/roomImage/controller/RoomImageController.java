package com.example.meetup_study.image.roomImage.controller;

import com.example.meetup_study.common.jwt.JwtService;
import com.example.meetup_study.image.roomImage.service.RoomImageService;
import com.example.meetup_study.image.roomImage.domain.dto.RequestDeleteRoomImageDto;
import com.example.meetup_study.image.roomImage.domain.dto.RequestRoomImageDto;
import com.example.meetup_study.image.roomImage.domain.dto.RoomImageDto;
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
    private final JwtService jwtService;

    private String ACCESSTOKEN = "AccessToken";

    @ApiOperation(value = "방 이미지 등록", notes = "방 이미지를 등록합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "이미지 파일", required = true, dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "body", value = "Request Body", dataTypeClass = RequestRoomImageDto.class, required = true, paramType = "body")
    })
    @PutMapping
    public ResponseEntity<RoomImageDto> updateRoomImage(@RequestParam("file") MultipartFile file, @Valid @RequestBody RequestRoomImageDto requestRoomImageDto, HttpServletRequest req) {

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<RoomImageDto> roomImageDtoOpt = roomImageService.updateRoomImage(file, requestRoomImageDto.getRoomId(), userIdOpt.get());

        return ResponseEntity.ok(roomImageDtoOpt.get());
    }

    @ApiOperation(value = "방 이미지 조회", notes = "방 이미지를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "방 ID", required = true, dataType = "long", paramType = "path")
    })
    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<RoomImageDto> getRoomImage(@PathVariable Long roomId) {

        Optional<RoomImageDto> roomImageDtoOpt = roomImageService.getRoomImage(roomId);

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

        Optional<RoomImageDto> roomImageDtoOpt = roomImageService.deleteRoomImage(requestDeleteRoomImageDto.getRoomId());

        return ResponseEntity.ok(roomImageDtoOpt.get());
    }

}
