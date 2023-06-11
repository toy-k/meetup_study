package com.example.meetup_study.admin;

import com.example.meetup_study.Category.CategoryService;
import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.hostReview.HostReviewService;
import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.hostReview.domain.dto.RequestDeleteHostReviewDto;
import com.example.meetup_study.hostReview.exception.HostReviewInvalidRequestException;
import com.example.meetup_study.hostReview.exception.HostReviewNotFoundException;
import com.example.meetup_study.review.ReviewService;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.RequestDeleteReviewDto;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.dto.RequestDeleteRoomDto;
import com.example.meetup_study.room.domain.dto.RoomDto;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
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
    private final RoomService roomService;
    private final JwtService jwtService;
    private final UserService userService;
    private final ReviewService reviewService;

    private String ACCESSTOKEN = "AccessToken";

    @DeleteMapping("/room")
    public ResponseEntity<RoomDto> deleteRoom(@Valid @RequestBody RequestDeleteRoomDto requestDeleteRoomDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userId = jwtService.extractUserId(accessToken);

        if(!userId.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userId.get());
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<Room> roomOpt = roomService.getRoom(requestDeleteRoomDto.getId());

        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<RoomDto> deletedRoomDto = adminService.deleteRoom(requestDeleteRoomDto.getId(), userOpt.get().getId());

        return ResponseEntity.ok(deletedRoomDto.get());
    }

    @DeleteMapping("/review")
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



        Long roomId = reviewDtoOpt.get().getRoomId();

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<ReviewDto> reviewDto = adminService.deleteReview(requestDeleteReviewDto.getReviewId(), userOpt.get().getId());

        return ResponseEntity.ok(reviewDto.get());
    }

    @DeleteMapping("/hostReview")
    public ResponseEntity<HostReviewDto> deleteHostReview(@Valid @RequestBody RequestDeleteHostReviewDto requestDeleteHostReviewDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<HostReviewDto> hostReviewDtoOpt = adminService.deleteHostReview(requestDeleteHostReviewDto.getHostReviewId(), userOpt.get().getId());

        if(!hostReviewDtoOpt.isPresent()){
            throw new HostReviewNotFoundException();
        }

        return ResponseEntity.ok(hostReviewDtoOpt.get());
    }



}
