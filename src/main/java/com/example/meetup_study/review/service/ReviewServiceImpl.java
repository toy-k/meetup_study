package com.example.meetup_study.review.service;

import com.example.meetup_study.joinedUser.exception.JoinedUserNotFoundException;
import com.example.meetup_study.mapper.ReviewMapper;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.RequestReviewDto;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.review.domain.repository.ReviewRepository;
import com.example.meetup_study.review.exception.ReviewInvalidRequestException;
import com.example.meetup_study.review.exception.ReviewNotFoundException;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ReviewDto> findReviewListByRoomId(Long roomId) {

        List<Review> reviews = reviewRepository.findByRoomId(roomId);

        List<ReviewDto> reviewDtoList = reviews.stream()
                .map(review -> reviewMapper.toReviewDto(review))
                .collect(Collectors.toList());
        return reviewDtoList;

    }


    @Transactional(readOnly = true)
    @Override
    public List<ReviewDto> findReviewListByUserId(Long userId) {

//        List<Review> reviewList = reviewRepository.findByUserId(userId);
        List<Review> reviewList = reviewRepository.findByUserId(userId);

        List<ReviewDto> reviewDtoList = reviewList.stream()
                .map(review -> reviewMapper.toReviewDto(review))
                .collect(Collectors.toList());
        return reviewDtoList;
    }

    @Override
    @Transactional
    public Optional<ReviewDto> createReview(RequestReviewDto requestReviewDto, Long userId) {

        Optional <Room> roomOpt = reviewRepository.findRoomWithJoinedUsersAndUsersById(requestReviewDto.getRoomId());
        if (!roomOpt.isPresent()) throw new RoomNotFoundException();


        User user = roomOpt.get().getJoinedUserList().stream().filter(joinedUser -> joinedUser.getUser().getId().equals(userId)).findFirst().get().getUser();

        if(user == null) throw new JoinedUserNotFoundException();

        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(roomOpt.get().getMeetupEndDate())){
            throw new ReviewInvalidRequestException("아직 모임이 끝나지 않아서 리뷰 작성할 수 없습니다.");
        }

        Optional<Review> reviewOpt = reviewRepository.findByUserIdAndRoomId(userId, requestReviewDto.getRoomId());
        if(reviewOpt.isPresent()){
            throw new ReviewInvalidRequestException("이미 리뷰를 작성하셨습니다.");
        }

        Review review = reviewRepository.save(new Review(user, roomOpt.get(), requestReviewDto.getRating(), requestReviewDto.getContent()));

        ReviewDto reviewDto = reviewMapper.toReviewDto(review);

        return Optional.of(reviewDto);
    }


    @Override
    public Boolean deleteReview(Long reviewId, Long UserId) {


        Optional<Review> reviewOpt = reviewRepository.findReviewWithRoomAndUserById(reviewId);
        if(!reviewOpt.isPresent()){
            throw new ReviewNotFoundException();
        }
        if(reviewOpt.get().getRoom() == null) {
            throw new RoomNotFoundException();
        }

        if(!reviewOpt.get().getUser().getId().equals(UserId)){
            throw new ReviewInvalidRequestException("남의 리뷰 삭제할 수 없습니다.");
        }
        if(reviewOpt.get().getIsHostReview()){
            throw new ReviewInvalidRequestException("이미 호스트가 리뷰 남겨서 삭제할 수 없습니다.");
        }

        reviewRepository.deleteById(reviewId);

        return true;
    }

    @Transactional(readOnly = true)
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

}
