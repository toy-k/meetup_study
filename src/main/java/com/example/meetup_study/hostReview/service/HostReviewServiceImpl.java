package com.example.meetup_study.hostReview.service;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.hostReview.domain.dto.RequestHostReviewDto;
import com.example.meetup_study.hostReview.domain.repository.HostReviewRepository;
import com.example.meetup_study.hostReview.exception.HostReviewInvalidRequestException;
import com.example.meetup_study.hostReview.exception.HostReviewNotFoundException;
import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.hostUser.exception.HostUserInvalidRequestException;
import com.example.meetup_study.hostUser.exception.HostUserNotFoundException;
import com.example.meetup_study.hostUser.service.HostUserService;
import com.example.meetup_study.joinedUser.domain.dto.JoinedUserDto;
import com.example.meetup_study.joinedUser.exception.JoinedUserNotFoundException;
import com.example.meetup_study.joinedUser.service.JoinedUserService;
import com.example.meetup_study.mapper.HostReviewMapper;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.repository.ReviewRepository;
import com.example.meetup_study.review.exception.ReviewNotFoundException;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.room.service.RoomService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HostReviewServiceImpl implements HostReviewService{

    private final HostReviewRepository hostReviewRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final HostReviewMapper hostReviewMapper;
    private final JoinedUserService joinedUserService;
    private final HostUserService hostUserService;
    private final RoomService roomService;

    @Transactional
    @Override
    public Optional<HostReviewDto> createHostReview(RequestHostReviewDto requestHostReviewDto, Long userId) {


        Optional<Room> roomOpt = roomService.getRoom(requestHostReviewDto.getRoomId());
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }
        Long roomId = roomOpt.get().getId();

        Optional<JoinedUserDto> joinedUserDtoOpt = joinedUserService.getJoinedUserByUserIdAndRoomId(userId, roomId);
        if(!joinedUserDtoOpt.isPresent()){
            throw new JoinedUserNotFoundException();
        }

        Optional<Review> reviewOpt = reviewRepository.findById(requestHostReviewDto.getReviewId());
        if (!reviewOpt.isPresent()) {
            throw new ReviewNotFoundException();
        }
        Optional<HostUser> hostUserOpt = hostUserService.getHostUserByRoomId(requestHostReviewDto.getRoomId());
        if (!hostUserOpt.isPresent()) {
            throw new HostUserNotFoundException();
        }
        if (hostUserOpt.get().getUser().getId() != userId) {
            throw new HostUserInvalidRequestException();
        }

        Optional<User> userOpt = userRepository.findById(userId);

        if(reviewOpt.get().getIsHostReview() == true){
            throw new HostReviewInvalidRequestException("이미 답글 달았습니다");
        }

        HostReview hostReview = hostReviewRepository.save(new HostReview(userOpt.get(), roomOpt.get(), requestHostReviewDto.getContent(), requestHostReviewDto.getReviewId()));

        reviewOpt.get().changeIsHostReview(true);

        HostReviewDto hostReviewDto = hostReviewMapper.toHostReviewDto(hostReview);

        return Optional.ofNullable(hostReviewDto);
    }

    @Override
    public List<HostReviewDto> findByRoomId(Long roomId) {
        Optional<Room> roomOpt = roomService.getRoom(roomId);

        if(roomOpt.isPresent()) {

            List<HostReview> hostReviews = hostReviewRepository.findByRoomId(roomId);

            List<HostReviewDto> hostReviewDtoList = hostReviews.stream()
                    .map(hostReview -> hostReviewMapper.toHostReviewDto(hostReview))
                    .collect(Collectors.toList());

            return hostReviewDtoList;

        }else{
            throw new RoomNotFoundException();
        }
    }

    @Transactional
    @Override
    public Optional<HostReviewDto> deleteHostReview(Long hostReviewId, Long userId) {

        Optional<HostReview> hostReviewOpt = hostReviewRepository.findById(hostReviewId);

        if(!hostReviewOpt.isPresent()){
            throw new HostReviewNotFoundException();
        }

        if(!hostReviewOpt.get().getUser().getId().equals(userId)){
            throw new HostReviewInvalidRequestException();
        }

        Optional<Review> reviewOpt = reviewRepository.findById(hostReviewOpt.get().getReviewId());
        if (!reviewOpt.isPresent()) {
            throw new HostReviewNotFoundException();
        }

        hostReviewRepository.deleteById(hostReviewId);
        reviewOpt.get().changeIsHostReview(false);

        HostReviewDto hostReviewDto = hostReviewMapper.toHostReviewDto(hostReviewOpt.get());

        return Optional.ofNullable(hostReviewDto);
    }
}
