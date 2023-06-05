package com.example.meetup_study.admin;

import com.example.meetup_study.Category.CategoryService;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.hostReview.HostReviewService;
import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.review.ReviewService;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestDeleteRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final RoomService roomService;
    private final JwtService jwtService;
    private final UserService userService;
    private final ReviewService reviewService;

    private String ACCESSTOKEN = "AccessToken";

    @DeleteMapping("/room")
    public ResponseEntity<RoomDto> deleteRoom(@RequestBody RequestDeleteRoomDto requestDeleteRoomDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userId = jwtService.extractUserId(accessToken);

        if(!userId.isPresent()){
            throw new IllegalArgumentException("토큰에 유저가 없습니다.");
        }

        Optional<User> userOpt = userService.findById(userId.get());

        Optional<Room> roomOpt = roomService.getRoom(requestDeleteRoomDto.getId());

        if(!userOpt.isPresent() || !roomOpt.isPresent()){
            throw new IllegalArgumentException("이 유저는 없거나, 방이 없거나, 방을 만들지 않았습니다.");
        }

        Optional<RoomDto> deletedRoomDto = adminService.deleteRoom(requestDeleteRoomDto.getId(), userOpt.get().getId());

        return ResponseEntity.ok(deletedRoomDto.get());
    }

    @DeleteMapping("/review")
    public ResponseEntity<Review> deleteReview(Long reviewId, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new IllegalArgumentException("유저가 없습니다.");
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new IllegalArgumentException("이 유저는 없거나, 방에 참여하지 않았습니다.");
        }

        Optional<Review> reviewOpt = reviewService.findById(reviewId);

        Long roomId = reviewOpt.get().getRoom().getId();

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if (!roomOpt.isPresent()) {
            throw new IllegalArgumentException("방이 없습니다.");
        }


        Optional<Review> review = adminService.deleteReview(reviewId, userOpt.get().getId());

        return ResponseEntity.ok(review.get());
    }

    @DeleteMapping
    public ResponseEntity<HostReview> deleteHostReview(@RequestParam Long hostReviewId, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new IllegalArgumentException("유저가 없습니다.");
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        Optional<HostReview> hostReviewOpt = adminService.deleteHostReview(hostReviewId, userOpt.get().getId());

        if(!hostReviewOpt.isPresent()){
            throw new IllegalArgumentException("리뷰를 삭제하지 못했습니다.");
        }

        return ResponseEntity.ok(hostReviewOpt.get());
    }



}
