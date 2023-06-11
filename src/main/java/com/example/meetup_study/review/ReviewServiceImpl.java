package com.example.meetup_study.review;

import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.RequestReviewDto;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.review.domain.repository.ReviewRepository;
import com.example.meetup_study.review.exception.ReviewInvalidRequestException;
import com.example.meetup_study.review.exception.ReviewNotFoundException;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public List<ReviewDto> findByRoomId(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isPresent()) {

            List<Review> reviews = reviewRepository.findByRoomId(roomId);

            List<ReviewDto> reviewDtoList = reviews.stream()
                    .map(review -> new ReviewDto().convertToReviewDto(review))
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
            List<Review> reviews = reviewRepository.findByUserId(userId);

            List<ReviewDto> reviewDtoList = reviews.stream()
                    .map(review -> new ReviewDto().convertToReviewDto(review))
                    .collect(Collectors.toList());
            return reviewDtoList;
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Optional<ReviewDto> createReview(RequestReviewDto requestReviewDto, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Room> room = roomRepository.findById(requestReviewDto.getRoomId());

        Review review = reviewRepository.save(new Review(user.get(), room.get(), requestReviewDto.getRating(), requestReviewDto.getContent()));

        ReviewDto reviewDto = new ReviewDto().convertToReviewDto(review);

        return Optional.of(reviewDto);
    }

    @Override
    public Optional<ReviewDto> deleteReview(Long reviewId, Long UserId) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        if(!review.get().getUser().getId().equals(UserId)){
            throw new ReviewInvalidRequestException("남의 리뷰 삭제할 수 없습니다.");
        }
        if(review.get().getIsHostReview()){
            throw new ReviewInvalidRequestException("이미 호스트가 리뷰 남겨서 삭제할 수 없습니다.");
        }

        reviewRepository.deleteById(reviewId);

        ReviewDto reviewDto = new ReviewDto().convertToReviewDto(review.get());

        return Optional.of(reviewDto);
    }

    @Override
    public Optional<ReviewDto> findById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if(review.isPresent()){

            ReviewDto reviewDto = new ReviewDto().convertToReviewDto(review.get());

            return Optional.of(reviewDto);

        }else{
            throw new ReviewNotFoundException();
        }
    }

    @Override
    public Optional<ReviewDto> findByUserIdAndRoomId(Long userId, Long roomId) {
        Optional<Review> review = reviewRepository.findByUserIdAndRoomId(userId, roomId);

        if(review.isPresent()){
            ReviewDto reviewDto = new ReviewDto().convertToReviewDto(review.get());

            return Optional.of(reviewDto);
        }else{
            return Optional.empty();
        }
    }
}
