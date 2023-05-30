package com.example.meetup_study.hostUser;

import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.hostUser.domain.dto.HostUserDto;
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

    @GetMapping("/id")
    public ResponseEntity<HostUserDto> getHostUser(Long id){
        Optional<HostUser> hostUseropt =  hostUserService.getHostUserById(id);

        if(hostUseropt.isPresent()){
            HostUserDto hostUserDto = new HostUserDto().convertToHostUserDto(hostUseropt.get());
            return ResponseEntity.ok(hostUserDto);

        }else{
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }



    }

    @GetMapping("ids")
    public ResponseEntity<HostUserDto> getHostUser(Long userId, Long roomId) {
        Optional<HostUser> hostUseropt = hostUserService.getHostUserByUserIdAndRoomId(userId, roomId);

        if (hostUseropt.isPresent()) {
            HostUserDto hostUserDto = new HostUserDto().convertToHostUserDto(hostUseropt.get());
            return ResponseEntity.ok(hostUserDto);

        } else {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }
    }

}
