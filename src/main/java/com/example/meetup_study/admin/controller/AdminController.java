package com.example.meetup_study.admin.controller;

import com.example.meetup_study.admin.service.AdminService;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.hostReview.domain.dto.RequestDeleteHostReviewDto;
import com.example.meetup_study.review.domain.dto.RequestDeleteReviewDto;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.room.domain.dto.RequestDeleteRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final JwtService jwtService;
    private final UserService userService;

    private String ACCESSTOKEN = "AccessToken";

    @ApiOperation(value = "방 삭제", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "Request Body", dataTypeClass = RequestDeleteRoomDto.class, required = true, paramType = "body")
    })
    @DeleteMapping("/room")
    public ResponseEntity<RoomDto> deleteRoom(@Valid @RequestBody RequestDeleteRoomDto requestDeleteRoomDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userId = jwtService.extractUserId(accessToken);

        Optional<RoomDto> deletedRoomDto = adminService.deleteRoom(requestDeleteRoomDto.getId(), userId.get());

        return ResponseEntity.ok(deletedRoomDto.get());
    }

    @ApiOperation(value = "리뷰 삭제", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "Request Body", dataTypeClass = RequestDeleteReviewDto.class, required = true, paramType = "body")
    })
    @DeleteMapping("/review")
    public ResponseEntity<ReviewDto> deleteReview(@Valid @RequestBody RequestDeleteReviewDto requestDeleteReviewDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);


        Optional<ReviewDto> reviewDto = adminService.deleteReview(requestDeleteReviewDto.getReviewId(), userIdOpt.get());

        return ResponseEntity.ok(reviewDto.get());
    }

    @ApiOperation(value = "호스트 리뷰 삭제", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "Request Body", dataTypeClass = RequestDeleteHostReviewDto.class, required = true, paramType = "body")
    })
    @DeleteMapping("/hostReview")
    public ResponseEntity<HostReviewDto> deleteHostReview(@Valid @RequestBody RequestDeleteHostReviewDto requestDeleteHostReviewDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<HostReviewDto> hostReviewDtoOpt = adminService.deleteHostReview(requestDeleteHostReviewDto.getHostReviewId(), userIdOpt.get());

        return ResponseEntity.ok(hostReviewDtoOpt.get());
    }



}
