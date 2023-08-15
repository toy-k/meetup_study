package com.example.meetup_study.review.controller;

import com.example.meetup_study.common.jwt.JwtService;
import com.example.meetup_study.joinedUser.service.JoinedUserService;
import com.example.meetup_study.review.service.ReviewService;
import com.example.meetup_study.review.domain.dto.RequestDeleteReviewDto;
import com.example.meetup_study.review.domain.dto.RequestReviewDto;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.room.service.RoomService;
import com.example.meetup_study.user.service.UserService;
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
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtService jwtService;
    private final UserService userService;
    private final JoinedUserService joinedUserService;
    private final RoomService roomService;

    private String ACCESSTOKEN = "AccessToken";

    //joineduser로 조건확인, 룸 상태확인(end)


    @ApiOperation(value = "리뷰 생성", notes = "리뷰를 생성합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestReviewDto.class, required = true, paramType = "body")
    })
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody RequestReviewDto requestReviewDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();
        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        Optional<ReviewDto> createdReviewDto = reviewService.createReview(requestReviewDto, userIdOpt.get());

        return ResponseEntity.ok(createdReviewDto.get());
    }

    @ApiOperation(value = "방 리뷰들 조회", notes = "방 리뷰들을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "roomId", value = "roomId", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<List<ReviewDto>> getReviewList(@PathVariable Long roomId){

        List<ReviewDto> reviewDtoList = reviewService.findReviewListByRoomId(roomId);

        return ResponseEntity.ok(reviewDtoList);
    }

    @ApiOperation(value = "유저의 리뷰들 조회", notes = "유저의 리뷰들을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "userId", value = "userId", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/userId/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewByUserId(@PathVariable Long userId){

        List<ReviewDto> reviewDtoList = reviewService.findReviewListByUserId(userId);

        return ResponseEntity.ok(reviewDtoList);
    }

    @ApiOperation(value = "리뷰 삭제", notes = "리뷰를 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestDeleteReviewDto.class, required = true, paramType = "body")
    })
    @DeleteMapping
    public ResponseEntity<Boolean> deleteReview(@Valid @RequestBody RequestDeleteReviewDto requestDeleteReviewDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);


        Boolean res= reviewService.deleteReview(requestDeleteReviewDto.getReviewId(), userIdOpt.get());

        return ResponseEntity.ok(res);
    }

}
