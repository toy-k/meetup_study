package com.example.meetup_study.hostUser.controller;

import com.example.meetup_study.hostUser.service.HostUserService;
import com.example.meetup_study.hostUser.domain.dto.HostUserDto;
import com.example.meetup_study.hostUser.domain.dto.RequestHostUserDto;
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

    @ApiOperation(value = "호스트 유저 조회", notes = "호스트 유저를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "호스트 유저 id", required = true, dataTypeClass = Long.class, paramType = "path")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<HostUserDto> getHostUser(@PathVariable Long id){
        Optional<HostUserDto> hostUserDtoOpt =  hostUserService.getHostUserById(id);

        return ResponseEntity.ok(hostUserDtoOpt.get());
    }

    @ApiOperation(value = "호스트 유저 조회", notes = "호스트 유저를 조회합니다.")
    @ApiImplicitParams({
    })
    @GetMapping("/ids")
    public ResponseEntity<HostUserDto> getHostUser(@Valid @RequestBody RequestHostUserDto requestHostUserDto) {

        Optional<HostUserDto> hostUserDtoOpt = hostUserService.getHostUserByUserIdAndRoomId(requestHostUserDto.getUserId(), requestHostUserDto.getRoomId());

        return ResponseEntity.ok(hostUserDtoOpt.get());

    }

}
