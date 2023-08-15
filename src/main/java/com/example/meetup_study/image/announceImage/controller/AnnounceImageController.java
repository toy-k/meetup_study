package com.example.meetup_study.image.announceImage.controller;

import com.example.meetup_study.common.jwt.JwtService;
import com.example.meetup_study.image.announceImage.service.AnnounceImageService;
import com.example.meetup_study.image.announceImage.domain.dto.AnnounceImageDto;
import com.example.meetup_study.image.announceImage.domain.dto.RequestAnnounceImageDto;
import com.example.meetup_study.image.announceImage.domain.dto.RequestDeleteAnnounceImageDto;
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
@RequestMapping("/api/announceImage")
public class AnnounceImageController {
    private final JwtService jwtService;
    private final AnnounceImageService announceImageService;

    private String ACCESSTOKEN = "AccessToken";

    @ApiOperation(value = "공지사항 이미지 생성", notes = "공지사항 이미지를 생성합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestAnnounceImageDto.class, required = true, paramType = "body"),
            @ApiImplicitParam(name= "file", value = "Request File", dataTypeClass = MultipartFile.class, required = true, paramType = "form")
    })
    @PutMapping
    public ResponseEntity<AnnounceImageDto> updateAnnounceImage(@RequestParam("file") MultipartFile file, @Valid @RequestBody RequestAnnounceImageDto requestAnnounceImageDto, HttpServletRequest req) {

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<AnnounceImageDto> announceImageDtoOpt = announceImageService.updateAnnounceImage(file, requestAnnounceImageDto.getAnnounceId());

        return ResponseEntity.ok(announceImageDtoOpt.get());
    }

    @ApiOperation(value = "공지사항 이미지 조회", notes = "공지사항 이미지를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "announceId", value = "공지사항 ID", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/announceId/{announceId}")
    public ResponseEntity<AnnounceImageDto> getAnnounceImage(@PathVariable Long announceId) {


        Optional<AnnounceImageDto> announceImageDtoOpt = announceImageService.getAnnounceImage(announceId);

        return ResponseEntity.ok(announceImageDtoOpt.get());
    }

    @ApiOperation(value = "공지사항 이미지 삭제", notes = "공지사항 이미지를 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestDeleteAnnounceImageDto.class, required = true, paramType = "body")
    })
    @DeleteMapping
    public ResponseEntity<AnnounceImageDto> deleteAnnounceImage(@Valid @RequestBody RequestDeleteAnnounceImageDto requestDeleteAnnounceImageDto, HttpServletRequest req) {
        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<AnnounceImageDto> announceImageDtoOpt = announceImageService.deleteAnnounceImage(requestDeleteAnnounceImageDto.getAnnounceId());

        return ResponseEntity.ok(announceImageDtoOpt.get());
    }
}
