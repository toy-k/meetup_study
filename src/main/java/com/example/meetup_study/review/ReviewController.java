package com.example.meetup_study.review;

import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.joinedUser.JoinedUserService;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.RequestReviewDto;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtService jwtService;
    private final UserService userService;
    private final JoinedUserService joinedUserService;

    private String ACCESSTOKEN = "AccessToken";

    //joineduser로 조건확인, 룸 상태확인(end)
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody RequestReviewDto requestReviewDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<User> userOpt = userService.findById(jwtService.extractUserId(accessToken).get());

        Optional<JoinedUser> joinedUserOpt = joinedUserService.getJoinedUserByUserIdAndRoomId(userOpt.get().getId(), requestReviewDto.getRoomId());

        if(!userOpt.isPresent() || !joinedUserOpt.isPresent()){
            throw new IllegalArgumentException("이 유저는 없거나, 방에 참여하지 않았습니다.");
        }

        Optional<Review> createdReview = reviewService.createReview(requestReviewDto, userOpt.get().getId());

        Optional<ReviewDto> createdReviewDto = createdReview.map(r -> new ReviewDto().convertToReviewDto(r));

        return ResponseEntity.ok(createdReviewDto.get());
    }

    @GetMapping("/roomId")
    public ResponseEntity<List<ReviewDto>> getReview(Long roomId){

        List<Review> reviewList = reviewService.findByRoomId(roomId);

        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for(Review review : reviewList){
            reviewDtoList.add(new ReviewDto().convertToReviewDto(review));
        }

        return ResponseEntity.ok(reviewDtoList);
    }

    @GetMapping("/userId")
    public ResponseEntity<List<ReviewDto>> getReviewByUserId(Long userId){

        List<Review> reviewList = reviewService.findByUserId(userId);

        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for(Review review : reviewList){
            reviewDtoList.add(new ReviewDto().convertToReviewDto(review));
        }

        return ResponseEntity.ok(reviewDtoList);
    }

    @DeleteMapping
    public ResponseEntity<Review> deleteReview(Long reviewId, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<User> userOpt = userService.findById(jwtService.extractUserId(accessToken).get());

        if(!userOpt.isPresent()){
            throw new IllegalArgumentException("이 유저는 없거나, 방에 참여하지 않았습니다.");
        }

        Optional<Review> review = reviewService.deleteReview(reviewId, userOpt.get().getId());

        return ResponseEntity.ok(review.get());
    }

}
