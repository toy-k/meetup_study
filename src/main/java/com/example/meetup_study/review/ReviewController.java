package com.example.meetup_study.review;

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



        Optional<Review> reviewOpt = reviewService.findByUserIdAndRoomId(userOpt.get().getId(), requestReviewDto.getRoomId());

        if(reviewOpt.isPresent()){
            throw new ReviewInvalidRequestException("이미 리뷰를 작성하셨습니다.");
        }

        Optional<Review> createdReview = reviewService.createReview(requestReviewDto, userOpt.get().getId());

        Optional<ReviewDto> createdReviewDto = createdReview.map(r -> new ReviewDto().convertToReviewDto(r));

        return ResponseEntity.ok(createdReviewDto.get());
    }

    @GetMapping("/roomId/{roomId}")
    public ResponseEntity<List<ReviewDto>> getReview(@PathVariable Long roomId){

        Optional<Room> roomOpt = roomService.getRoom(roomId);

        if (!roomOpt.isPresent()) {
            throw new RoomNotFoundException();
        }


        List<Review> reviewList = reviewService.findByRoomId(roomId);

        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for(Review review : reviewList){
            reviewDtoList.add(new ReviewDto().convertToReviewDto(review));
        }

        return ResponseEntity.ok(reviewDtoList);
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewByUserId(@PathVariable Long userId){

        Optional<User> userOpt = userService.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException();
        }

        List<Review> reviewList = reviewService.findByUserId(userId);

        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for(Review review : reviewList){
            reviewDtoList.add(new ReviewDto().convertToReviewDto(review));
        }

        return ResponseEntity.ok(reviewDtoList);
    }

    @DeleteMapping
    public ResponseEntity<Review> deleteReview(@Valid @RequestBody RequestDeleteReviewDto requestDeleteReviewDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<Review> reviewOpt = reviewService.findById(requestDeleteReviewDto.getReviewId());
        if(!reviewOpt.isPresent()){
            throw new ReviewNotFoundException();
        }

        Long roomId = reviewOpt.get().getRoom().getId();

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if (!roomOpt.isPresent()) {
            throw new RoomNotFoundException();
        }


        Optional<Review> review = reviewService.deleteReview(requestDeleteReviewDto.getReviewId(), userOpt.get().getId());

        return ResponseEntity.ok(review.get());
    }

}
