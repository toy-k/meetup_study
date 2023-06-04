package com.example.meetup_study.announce;

import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.announce.domain.dto.AnnounceDto;
import com.example.meetup_study.announce.domain.dto.RequestAnnounceDto;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestDeleteRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/announce")
@RequiredArgsConstructor
public class AnnounceController {

    private final AnnounceService announceService;
    private final JwtService jwtService;
    private final UserService userService;

    private String ACCESSTOKEN = "AccessToken";


    @PostMapping
    public ResponseEntity<AnnounceDto> createAnnounce(@RequestBody RequestAnnounceDto requestAnnounceDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if (!userIdOpt.isPresent()) {
            throw new IllegalArgumentException("유저Id가 없습니다.");
        }
        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent() || userOpt.get().getId() != requestAnnounceDto.getUserId()){
            throw new IllegalArgumentException("이 유저는 없거나, 방을 만들지 않았습니다.");
        }

        Optional<Announce> createdAnnounce = announceService.createAnnounce(requestAnnounceDto);

        Optional<AnnounceDto> createdAnnounceDto = createdAnnounce.map(r -> new AnnounceDto().convertToAnnounceDto(r));

        return ResponseEntity.ok(createdAnnounceDto.get());
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<AnnounceDto> getAnnounce(@PathVariable Long id){

        Optional<Announce> announceOpt = announceService.getAnnounce(id);
        if(announceOpt.isPresent()){
            AnnounceDto announceDtoOpt = new AnnounceDto().convertToAnnounceDto(announceOpt.get());
            return ResponseEntity.ok(announceDtoOpt);
        }else{
            throw new IllegalArgumentException("방이 없습니다.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<AnnounceDto>> getAnnounceList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){

        if(page < 1 || size < 1){
            page = 1;
            size = 10;
        }

        List<AnnounceDto> announceDtos = announceService.getAnnounceList(page, size);

        return ResponseEntity.ok(announceDtos);
    }


    @PutMapping()
    public ResponseEntity<AnnounceDto> updateAnnounce(@RequestBody AnnounceDto AnnounceDto, HttpServletRequest req) throws AccessDeniedException {

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new IllegalArgumentException("토큰에 유저가 없습니다.");
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent() || userOpt.get().getId() != AnnounceDto.getUserId()){
            throw new AccessDeniedException("이 유저는 없거나, 방을 만들지 않았습니다.");
        }

        Optional<AnnounceDto> updatedAnnounceDto = announceService.updateAnnounce(AnnounceDto, userOpt.get().getId());

        return ResponseEntity.ok(updatedAnnounceDto.get());
    }

    @DeleteMapping
    public ResponseEntity<AnnounceDto> deleteAnnounce(@RequestBody Long id, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);
        if(!userIdOpt.isPresent()){
            throw new IllegalArgumentException("토큰에 유저가 없습니다.");
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        Optional<Announce> announceOpt = announceService.getAnnounce(id);

        if(!userOpt.isPresent() || !announceOpt.isPresent() ||userOpt.get().getId() != announceOpt.get().getUser().getId()){
            throw new IllegalArgumentException("이 유저는 없거나, 방이 없거나, 방을 만들지 않았습니다.");
        }

        Optional<AnnounceDto> deletedAnnounceDto = announceService.deleteAnnounce(id, userOpt.get().getId());

        return ResponseEntity.ok(deletedAnnounceDto.get());
    }
}
