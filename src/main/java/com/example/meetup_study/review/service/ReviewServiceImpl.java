package com.example.meetup_study.review.service;

import com.example.meetup_study.joinedUser.domain.dto.JoinedUserDto;
import com.example.meetup_study.joinedUser.service.JoinedUserService;
import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.exception.JoinedUserNotFoundException;
import com.example.meetup_study.mapper.ReviewMapper;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.RequestReviewDto;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.review.domain.repository.ReviewRepository;
import com.example.meetup_study.review.exception.ReviewInvalidRequestException;
import com.example.meetup_study.review.exception.ReviewNotFoundException;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.room.service.RoomService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;
    private final JoinedUserService joinedUserService;
    private final RoomService roomService;

    @Override
    public List<ReviewDto> findByRoomId(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isPresent()) {

            List<Review> reviews = reviewRepository.findByRoomId(roomId);

            List<ReviewDto> reviewDtoList = reviews.stream()
                    .map(review -> reviewMapper.toReviewDto(review))
                    .collect(Collectors.toList());
            return reviewDtoList;

        }else{
            throw new RoomNotFoundException();
        }
    }

    @Override
    public List<ReviewDto> findByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
//            List<Review> reviews = reviewRepository.findByUserId(userId);
            List<Review> reviews = user.get().getReviewList();

            List<ReviewDto> reviewDtoList = reviews.stream()
                    .map(review -> reviewMapper.toReviewDto(review))
                    .collect(Collectors.toList());
            return reviewDtoList;
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    @Transactional
    public Optional<ReviewDto> createReview(RequestReviewDto requestReviewDto, Long userId) {

        Optional<Room> roomOpt = roomService.getRoom(requestReviewDto.getRoomId());
        if (!roomOpt.isPresent()) {
            throw new RoomNotFoundException();
        }

        Optional<JoinedUserDto> joinedUserDtoOpt = joinedUserService.getJoinedUserByUserIdAndRoomId(userId, requestReviewDto.getRoomId());
        if(!joinedUserDtoOpt.isPresent()){
            throw new JoinedUserNotFoundException();
        }

        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(roomOpt.get().getMeetupEndDate())){
            throw new ReviewInvalidRequestException("아직 모임이 끝나지 않아서 리뷰 작성할 수 없습니다.");
        }

        Optional<ReviewDto> reviewDtoOpt = this.findByUserIdAndRoomId(userId, requestReviewDto.getRoomId());
        if(reviewDtoOpt.isPresent()){
            throw new ReviewInvalidRequestException("이미 리뷰를 작성하셨습니다.");
        }

        Optional<User> user = userRepository.findById(userId);

        Review review = reviewRepository.save(new Review(user.get(), roomOpt.get(), requestReviewDto.getRating(), requestReviewDto.getContent()));

        ReviewDto reviewDto = reviewMapper.toReviewDto(review);

        return Optional.of(reviewDto);
    }

    @Override
    public Optional<ReviewDto> deleteReview(Long reviewId, Long UserId) {

        Optional<ReviewDto> reviewDtoOpt = this.findById(reviewId);
        if(!reviewDtoOpt.isPresent()){
            throw new ReviewNotFoundException();
        }

        Long roomId = reviewDtoOpt.get().getRoomId();
        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if (!roomOpt.isPresent()) {
            throw new RoomNotFoundException();
        }

        if(!reviewDtoOpt.get().getUserId().equals(UserId)){
            throw new ReviewInvalidRequestException("남의 리뷰 삭제할 수 없습니다.");
        }
        if(reviewDtoOpt.get().getIsHostReview()){
            throw new ReviewInvalidRequestException("이미 호스트가 리뷰 남겨서 삭제할 수 없습니다.");
        }

        reviewRepository.deleteById(reviewId);

        return reviewDtoOpt;
    }

    @Override
    public Optional<ReviewDto> findById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if(review.isPresent()){

            ReviewDto reviewDto = reviewMapper.toReviewDto(review.get());

            return Optional.of(reviewDto);

        }else{
            throw new ReviewNotFoundException();
        }
    }

    @Override
    public Optional<ReviewDto> findByUserIdAndRoomId(Long userId, Long roomId) {
        Optional<Review> review = reviewRepository.findByUserIdAndRoomId(userId, roomId);

        if(review.isPresent()){
            ReviewDto reviewDto = reviewMapper.toReviewDto(review.get());

            return Optional.of(reviewDto);
        }else{
            return Optional.empty();
        }
    }
}
