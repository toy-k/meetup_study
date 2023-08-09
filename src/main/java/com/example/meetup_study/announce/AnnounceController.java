package com.example.meetup_study.announce;

import com.example.meetup_study.announce.domain.dto.AnnounceDto;
import com.example.meetup_study.announce.domain.dto.RequestAnnounceDto;
import com.example.meetup_study.announce.domain.dto.RequestDeleteAnnounceDto;
import com.example.meetup_study.announce.exception.AnnounceInvalidRequestException;
import com.example.meetup_study.announce.exception.AnnounceNotFoundException;
import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.user.service.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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


    @ApiOperation(value = "공지사항 생성", notes = "공지사항을 생성합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestAnnounceDto.class, required = true, paramType = "body")
    })
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

    @ApiOperation(value = "공지사항 조회", notes = "공지사항을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "id", value = "공지사항 id", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<AnnounceDto> getAnnounce(@PathVariable Long id){

        Optional<AnnounceDto> announceDtoOpt = announceService.getAnnounce(id);
        if(announceDtoOpt.isPresent()){
            return ResponseEntity.ok(announceDtoOpt.get());
        }else{
            throw new AnnounceNotFoundException();
        }
    }
    @ApiOperation(value = "공지사항 리스트 조회", notes = "공지사항 리스트를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "page", value = "페이지", dataTypeClass = Integer.class, required = true, paramType = "query"),
            @ApiImplicitParam(name= "size", value = "사이즈", dataTypeClass = Integer.class, required = true, paramType = "query")
    })
    @GetMapping("/list")
    public ResponseEntity<List<AnnounceDto>> getAnnounceList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){

        if(page < 1 || size != 10){
            page = 1;
            size = 10;
        }

        List<AnnounceDto> announceDtos = announceService.getAnnounceList(page, size);

        return ResponseEntity.ok(announceDtos);
    }


    @ApiOperation(value = "공지사항 수정", notes = "공지사항을 수정합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = AnnounceDto.class, required = true, paramType = "body")
    })
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

    @ApiOperation(value = "공지사항 삭제", notes = "공지사항을 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestDeleteAnnounceDto.class, required = true, paramType = "body")
    })
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

    @ApiOperation(value = "공지사항 갯수", notes = "공지사항 갯수를 조회합니다.")
    @GetMapping("/count")
    public ResponseEntity<Long> getAnnounceCount(){
        Long announceCount = announceService.getAnnounceCount();
        return ResponseEntity.ok(announceCount);
    }
}
