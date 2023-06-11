package com.example.meetup_study.hostReview;

import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.hostReview.domain.dto.RequestHostReviewDto;
import com.example.meetup_study.hostReview.exception.HostReviewInvalidRequestException;
import com.example.meetup_study.hostReview.exception.HostReviewNotFoundException;
import com.example.meetup_study.hostUser.HostUserService;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.hostUser.exception.HostUserInvalidRequestException;
import com.example.meetup_study.hostUser.exception.HostUserNotFoundException;
import com.example.meetup_study.joinedUser.JoinedUserService;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.exception.JoinedUserNotFoundException;
import com.example.meetup_study.review.ReviewService;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.ReviewDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hostReview")
public class HostReviewController {

    private final ReviewService reviewService;
    private final JwtService jwtService;
    private final UserService userService;
    private final JoinedUserService joinedUserService;
    private final RoomService roomService;
    private final HostUserService hostUserService;
    private final HostReviewService hostReviewService;

    private String ACCESSTOKEN = "AccessToken";

    @PostMapping
    public ResponseEntity<HostReviewDto> createHostReview(@RequestBody RequestHostReviewDto requestHostReviewDto, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }


        Optional<Room> roomOpt = roomService.getRoom(requestHostReviewDto.getRoomId());
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<JoinedUser> joinedUserOpt = joinedUserService.getJoinedUserByUserIdAndRoomId(userOpt.get().getId(), requestHostReviewDto.getRoomId());

        if(!joinedUserOpt.isPresent()){
            throw new JoinedUserNotFoundException();
        }

        Optional<ReviewDto> reviewDtoOpt = reviewService.findById(requestHostReviewDto.getReviewId());
        if (!reviewDtoOpt.isPresent()) {
            throw new ReviewNotFoundException();
        }

        Optional<HostUser> hostUserOpt = hostUserService.getHostUserById(requestHostReviewDto.getRoomId());
        if (!hostUserOpt.isPresent()) {
            throw new HostUserNotFoundException();
        }

        if (hostUserOpt.get().getUser().getId() != userOpt.get().getId()) {
            throw new HostUserInvalidRequestException();
        }

        Optional<HostReview> hostReviewOpt = hostReviewService.createHostReview(requestHostReviewDto, userOpt.get().getId());

        if(!hostReviewOpt.isPresent()){
            throw new HostReviewInvalidRequestException();
        }

        Optional<HostReviewDto> hostReviewDtoOpt = hostReviewOpt.map(r-> new HostReviewDto().convertToHostReviewDto(r));

        return ResponseEntity.ok(hostReviewDtoOpt.get());

    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<HostReviewDto>> getHostReview(@PathVariable Long roomId){

        Optional<Room> roomOpt = roomService.getRoom(roomId);

        if (!roomOpt.isPresent()) {
            throw new RoomNotFoundException();
        }

        List<HostReview> hostReviewList = hostReviewService.findByRoomId(roomId);

        List<HostReviewDto>  hostReviewDtoList = new ArrayList<>();

        for(HostReview hostReview : hostReviewList){
            hostReviewDtoList.add(new HostReviewDto().convertToHostReviewDto(hostReview));
        }

        return ResponseEntity.ok(hostReviewDtoList);
    }

    @DeleteMapping
    public ResponseEntity<HostReview> deleteHostReview(@RequestParam Long hostReviewId, HttpServletRequest req){

        String accessToken = req.getAttribute(ACCESSTOKEN).toString();

        Optional<Long> userIdOpt = jwtService.extractUserId(accessToken);

        if(!userIdOpt.isPresent()){
            throw new AccessTokenInvalidRequestException();
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<HostReview> hostReviewOpt = hostReviewService.deleteHostReview(hostReviewId, userOpt.get().getId());

        if(!hostReviewOpt.isPresent()){
            throw new HostReviewInvalidRequestException();
        }

        return ResponseEntity.ok(hostReviewOpt.get());
    }

}
