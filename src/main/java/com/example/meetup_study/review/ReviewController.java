package com.example.meetup_study.review;

import com.example.meetup_study.announce.domain.dto.RequestAnnounceDto;
import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.joinedUser.JoinedUserService;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.exception.JoinedUserNotFoundException;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.RequestDeleteReviewDto;
import com.example.meetup_study.review.domain.dto.RequestReviewDto;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.review.exception.ReviewInvalidRequestException;
import com.example.meetup_study.review.exception.ReviewNotFoundException;
import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.user.UserService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<Room> roomOpt = roomService.getRoom(requestReviewDto.getRoomId());

        if (!roomOpt.isPresent()) {
            throw new RoomNotFoundException();
        }

        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(roomOpt.get().getMeetupEndDate())){
            throw new ReviewInvalidRequestException("아직 모임이 끝나지 않아서 리뷰 작성할 수 없습니다.");
        }


        Optional<JoinedUser> joinedUserOpt = joinedUserService.getJoinedUserByUserIdAndRoomId(userOpt.get().getId(), requestReviewDto.getRoomId());

        if(!joinedUserOpt.isPresent()){
            throw new JoinedUserNotFoundException();
        }



        Optional<ReviewDto> reviewDtoOpt = reviewService.findByUserIdAndRoomId(userOpt.get().getId(), requestReviewDto.getRoomId());

        if(reviewDtoOpt.isPresent()){
            throw new ReviewInvalidRequestException("이미 리뷰를 작성하셨습니다.");
        }

        Optional<ReviewDto> createdReviewDto = reviewService.createReview(requestReviewDto, userOpt.get().getId());


        return ResponseEntity.ok(createdReviewDto.get());
    }

    @ApiOperation(value = "방 리뷰들 조회", notes = "방 리뷰들을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "roomId", value = "roomId", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<List<ReviewDto>> getReview(@PathVariable Long roomId){

        Optional<Room> roomOpt = roomService.getRoom(roomId);

        if (!roomOpt.isPresent()) {
            throw new RoomNotFoundException();
        }


        List<ReviewDto> reviewDtoList = reviewService.findByRoomId(roomId);

        return ResponseEntity.ok(reviewDtoList);
    }

    @ApiOperation(value = "유저의 리뷰들 조회", notes = "유저의 리뷰들을 조회합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "userId", value = "userId", dataTypeClass = Long.class, required = true, paramType = "path")
    })
    @GetMapping("/userId/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewByUserId(@PathVariable Long userId){

        Optional<User> userOpt = userService.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException();
        }

        List<ReviewDto> reviewDtoList = reviewService.findByUserId(userId);

        return ResponseEntity.ok(reviewDtoList);
    }

    @ApiOperation(value = "리뷰 삭제", notes = "리뷰를 삭제합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name= "body", value = "Request Body", dataTypeClass = RequestDeleteReviewDto.class, required = true, paramType = "body")
    })
    @DeleteMapping
    public ResponseEntity<ReviewDto> deleteReview(@Valid @RequestBody RequestDeleteReviewDto requestDeleteReviewDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<ReviewDto> reviewDtoOpt = reviewService.findById(requestDeleteReviewDto.getReviewId());
        if(!reviewDtoOpt.isPresent()){
            throw new ReviewNotFoundException();
        }

        Long roomId = reviewDtoOpt.get().getRoomId();

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if (!roomOpt.isPresent()) {
            throw new RoomNotFoundException();
        }


        Optional<ReviewDto> reviewDto = reviewService.deleteReview(requestDeleteReviewDto.getReviewId(), userOpt.get().getId());

        return ResponseEntity.ok(reviewDto.get());
    }

}
