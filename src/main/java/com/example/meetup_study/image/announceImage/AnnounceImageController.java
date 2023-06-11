package com.example.meetup_study.image.announceImage;

import com.example.meetup_study.announce.AnnounceService;
import com.example.meetup_study.announce.domain.dto.AnnounceDto;
import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.image.announceImage.domain.dto.AnnounceImageDto;
import com.example.meetup_study.image.announceImage.domain.dto.RequestAnnounceImageDto;
import com.example.meetup_study.image.announceImage.domain.dto.RequestDeleteAnnounceImageDto;
import com.example.meetup_study.image.exception.ImageInvalidRequestException;
import com.example.meetup_study.image.exception.ImageNotFoundException;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announceImage")
public class AnnounceImageController {
    private final JwtService jwtService;
    private final UserService userService;
    private final AnnounceService announceService;
    private final AnnounceImageService announceImageService;

    private String ACCESSTOKEN = "AccessToken";

    @PutMapping
    public ResponseEntity<AnnounceImageDto> updateAnnounceImage(@RequestParam("file") MultipartFile file, @Valid @RequestBody RequestAnnounceImageDto requestAnnounceImageDto, HttpServletRequest req) throws Exception {

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<AnnounceDto> announceDtoOpt = announceService.getAnnounce(requestAnnounceImageDto.getAnnounceId());
        if (!announceDtoOpt.isPresent()) throw new ImageNotFoundException();


        if (file.isEmpty()) {
            throw new ImageNotFoundException();
        }

        Optional<AnnounceImageDto> announceImageDtoOpt = announceImageService.updateAnnounceImage(file, requestAnnounceImageDto.getAnnounceId());

        if(!announceImageDtoOpt.isPresent()) throw new ImageNotFoundException();

        return ResponseEntity.ok(announceImageDtoOpt.get());
    }

    @GetMapping("/announceId/{announceId}")
    public ResponseEntity<AnnounceImageDto> getAnnounceImage(@PathVariable Long announceId) {

        Optional<AnnounceDto> announceDtoOpt = announceService.getAnnounce(announceId);

        if(!announceDtoOpt.isPresent()){
            throw new ImageNotFoundException();
        }

        Optional<AnnounceImageDto> announceImageDtoOpt = announceImageService.getAnnounceImage(announceId);

        if(!announceImageDtoOpt.isPresent()) throw new ImageNotFoundException();

        return ResponseEntity.ok(announceImageDtoOpt.get());
    }
    @DeleteMapping
    public ResponseEntity<AnnounceImageDto> deleteAnnounceImage(@Valid @RequestBody RequestDeleteAnnounceImageDto requestDeleteAnnounceImageDto, HttpServletRequest req) {
        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<AnnounceDto> announceDtoOpt = announceService.getAnnounce(requestDeleteAnnounceImageDto.getAnnounceId());
        if(!announceDtoOpt.isPresent()){
            throw new ImageNotFoundException();
        }

        Optional<AnnounceImageDto> announceImageDtoOpt = announceImageService.deleteAnnounceImage(requestDeleteAnnounceImageDto.getAnnounceId());

        if(!announceImageDtoOpt.isPresent()) throw new ImageInvalidRequestException();

        return ResponseEntity.ok(announceImageDtoOpt.get());
    }
}
