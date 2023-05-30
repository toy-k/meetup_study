package com.example.meetup_study.joinedUser;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.domain.dto.JoinedUserDto;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/joinedUser")
public class JoinedUserController {

    private final JoinedUserService joinedUserService;

    @GetMapping("/id")
    public ResponseEntity<JoinedUserDto> getJoinedUser(Long id){
        Optional<JoinedUser> joinedUseropt =  joinedUserService.getJoinedUserById(id);

        if(joinedUseropt.isPresent()){
            JoinedUserDto joinedUserDto = new JoinedUserDto().convertToJoinedUserDto(joinedUseropt.get());
            return ResponseEntity.ok(joinedUserDto);

        }else{
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }



    }

    @GetMapping("ids")
    public ResponseEntity<JoinedUserDto> getJoinedUser(Long userId, Long roomId) {
        Optional<JoinedUser> joinedUseropt = joinedUserService.getJoinedUserByUserIdAndRoomId(userId, roomId);

        if (joinedUseropt.isPresent()) {
            JoinedUserDto joinedUserDto = new JoinedUserDto().convertToJoinedUserDto(joinedUseropt.get());
            return ResponseEntity.ok(joinedUserDto);

        } else {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }
    }

    @GetMapping("/userId")
    public ResponseEntity<List<JoinedUserDto>> getJoinedUserByUserId(Long userId){
        List<JoinedUser> joinedUseropt =  joinedUserService.getJoinedUserByUserId(userId);

        List<JoinedUserDto> joinedUserDtos = new ArrayList<>();

        for(JoinedUser joinedUser : joinedUseropt){
            joinedUserDtos.add(new JoinedUserDto().convertToJoinedUserDto(joinedUser));
        }

        return ResponseEntity.ok(joinedUserDtos);


    }

    @GetMapping("/roomId")
    public ResponseEntity<List<JoinedUserDto>> getJoinedUserByRoomId(Long roomId) {
        List<JoinedUser> joinedUseropt = joinedUserService.getJoinedUserByRoomId(roomId);

        List<JoinedUserDto> joinedUserDtos = new ArrayList<>();

        for (JoinedUser joinedUser : joinedUseropt) {
            joinedUserDtos.add(new JoinedUserDto().convertToJoinedUserDto(joinedUser));
        }

        return ResponseEntity.ok(joinedUserDtos);
    }
}
