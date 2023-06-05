package com.example.meetup_study.hostReview;

import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.hostReview.domain.dto.RequestHostReviewDto;
import com.example.meetup_study.hostUser.HostUserService;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.joinedUser.JoinedUserService;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.review.ReviewService;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
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
            throw new IllegalArgumentException("유저가 없습니다.");
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        Optional<Room> roomOpt = roomService.getRoom(requestHostReviewDto.getRoomId());

        if (!roomOpt.isPresent()) {
            throw new IllegalArgumentException("방이 없습니다.");
        }

        Optional<JoinedUser> joinedUserOpt = joinedUserService.getJoinedUserByUserIdAndRoomId(userOpt.get().getId(), requestHostReviewDto.getRoomId());

        if(!userOpt.isPresent() || !joinedUserOpt.isPresent()){
            throw new IllegalArgumentException("이 유저는 없거나, 방에 참여하지 않았습니다.");
        }

        Optional<Review> reviewOpt = reviewService.findById(requestHostReviewDto.getReviewId());
        if (!reviewOpt.isPresent()) {
            throw new IllegalArgumentException("리뷰가 없습니다.");
        }

        Optional<HostUser> hostUserOpt = hostUserService.getHostUserById(requestHostReviewDto.getRoomId());
        if (!hostUserOpt.isPresent() || hostUserOpt.get().getUser().getId() != userOpt.get().getId()) {
            throw new IllegalArgumentException("방장이 아닙니다");
        }

        Optional<HostReview> hostReviewOpt = hostReviewService.createHostReview(requestHostReviewDto, userOpt.get().getId());

        if(!hostReviewOpt.isPresent()){
            throw new IllegalArgumentException("리뷰를 생성하지 못했습니다.");
        }

        Optional<HostReviewDto> hostReviewDtoOpt = hostReviewOpt.map(r-> new HostReviewDto().convertToHostReviewDto(r));

        return ResponseEntity.ok(hostReviewDtoOpt.get());

    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<HostReviewDto>> getHostReview(@PathVariable Long roomId){

        Optional<Room> roomOpt = roomService.getRoom(roomId);

        if (!roomOpt.isPresent()) {
            throw new IllegalArgumentException("방이 없습니다.");
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
            throw new IllegalArgumentException("유저가 없습니다.");
        }

        Optional<User> userOpt = userService.findById(userIdOpt.get());

        Optional<HostReview> hostReviewOpt = hostReviewService.deleteHostReview(hostReviewId, userOpt.get().getId());

        if(!hostReviewOpt.isPresent()){
            throw new IllegalArgumentException("리뷰를 삭제하지 못했습니다.");
        }

        return ResponseEntity.ok(hostReviewOpt.get());
    }

}
