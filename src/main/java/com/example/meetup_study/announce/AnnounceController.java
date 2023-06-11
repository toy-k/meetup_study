package com.example.meetup_study.announce;

import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.announce.domain.dto.AnnounceDto;
import com.example.meetup_study.announce.domain.dto.RequestAnnounceDto;
import com.example.meetup_study.announce.domain.dto.RequestDeleteAnnounceDto;
import com.example.meetup_study.announce.exception.AnnounceInvalidRequestException;
import com.example.meetup_study.announce.exception.AnnounceNotFoundException;
import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestDeleteRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    public ResponseEntity<AnnounceDto> createAnnounce(@Valid @RequestBody RequestAnnounceDto requestAnnounceDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        if(userOpt.get().getId() != requestAnnounceDto.getUserId()){
            throw new AnnounceInvalidRequestException();
        }

        Optional<AnnounceDto> createdAnnounceDto = announceService.createAnnounce(requestAnnounceDto);

        return ResponseEntity.ok(createdAnnounceDto.get());
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<AnnounceDto> getAnnounce(@PathVariable Long id){

        Optional<AnnounceDto> announceDtoOpt = announceService.getAnnounce(id);
        if(announceDtoOpt.isPresent()){
            return ResponseEntity.ok(announceDtoOpt.get());
        }else{
            throw new AnnounceNotFoundException();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<AnnounceDto>> getAnnounceList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){

        if(page < 1 || size != 10){
            page = 1;
            size = 10;
        }

        List<AnnounceDto> announceDtos = announceService.getAnnounceList(page, size);

        return ResponseEntity.ok(announceDtos);
    }


    @PutMapping
    public ResponseEntity<AnnounceDto> updateAnnounce(@RequestBody AnnounceDto AnnounceDto, HttpServletRequest req) throws AccessDeniedException {

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        if(userOpt.get().getId() != AnnounceDto.getUserId()){
            throw new AnnounceInvalidRequestException();
        }

        Optional<AnnounceDto> updatedAnnounceDto = announceService.updateAnnounce(AnnounceDto, userOpt.get().getId());

        return ResponseEntity.ok(updatedAnnounceDto.get());
    }

    @DeleteMapping
    public ResponseEntity<AnnounceDto> deleteAnnounce(@Valid @RequestBody RequestDeleteAnnounceDto requestDeleteAnnounceDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);
        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<AnnounceDto> announceDtoOpt = announceService.getAnnounce(requestDeleteAnnounceDto.getAnnounceId());

        if(!announceDtoOpt.isPresent() ){
            throw new AnnounceNotFoundException();
        }

        if(userOpt.get().getId() != announceDtoOpt.get().getUserId()){
            throw new AnnounceInvalidRequestException();
        }

        Optional<AnnounceDto> deletedAnnounceDto = announceService.deleteAnnounce(requestDeleteAnnounceDto.getAnnounceId(), userOpt.get().getId());

        return ResponseEntity.ok(deletedAnnounceDto.get());
    }
}
