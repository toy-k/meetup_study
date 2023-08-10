package com.example.meetup_study.hostReview.controller;

import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.hostReview.service.HostReviewService;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.hostReview.domain.dto.RequestDeleteHostReviewDto;
import com.example.meetup_study.hostReview.domain.dto.RequestHostReviewDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hostReview")
public class HostReviewController {

    private final JwtService jwtService;
    private final HostReviewService hostReviewService;

    private String ACCESSTOKEN = "AccessToken";

    @ApiOperation(value = "호스트 리뷰 생성", notes = "호스트 리뷰를 생성합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "Request body", required = true, dataTypeClass = RequestHostReviewDto.class, paramType = "body")
    })
    @PostMapping
    public ResponseEntity<HostReviewDto> createHostReview(@Valid @RequestBody RequestHostReviewDto requestHostReviewDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<HostReviewDto> hostReviewDtoOpt = hostReviewService.createHostReview(requestHostReviewDto, userIdOpt.get());

        return ResponseEntity.ok(hostReviewDtoOpt.get());

    }

    @ApiOperation(value = "호스트 리뷰 조회", notes = "호스트 리뷰를 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roomId", value = "Request body", required = true, dataTypeClass = Long.class, paramType = "path")
    })
    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<List<HostReviewDto>> getHostReview(@PathVariable Long roomId){

        List<HostReviewDto> hostReviewDtoList = hostReviewService.findByRoomId(roomId);

        return ResponseEntity.ok(hostReviewDtoList);
    }

    @ApiOperation(value = "호스트 리뷰 삭제", notes = "호스트 리뷰를 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "Request body", required = true, dataTypeClass = RequestDeleteHostReviewDto.class, paramType = "body")
    })
    @DeleteMapping
    public ResponseEntity<HostReviewDto> deleteHostReview(@Valid @RequestBody RequestDeleteHostReviewDto requestDeleteHostReviewDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<HostReviewDto> hostReviewDtoOpt = hostReviewService.deleteHostReview(requestDeleteHostReviewDto.getHostReviewId(), userIdOpt.get());

        return ResponseEntity.ok(hostReviewDtoOpt.get());
    }

}
