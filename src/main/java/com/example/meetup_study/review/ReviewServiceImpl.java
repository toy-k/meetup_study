package com.example.meetup_study.review;

import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.RequestReviewDto;
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

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public List<Review> findByRoomId(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isPresent()) {

            List<Review> reviews = reviewRepository.findByRoomId(roomId);

            return reviews;

        }else{
            throw new RoomNotFoundException();
        }
    }

    @Override
    public List<Review> findByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Review> reviews = reviewRepository.findByUserId(userId);
            return reviews;
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Optional<Review> createReview(RequestReviewDto requestReviewDto, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Room> room = roomRepository.findById(requestReviewDto.getRoomId());

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        if(!room.isPresent()){
            throw new RoomNotFoundException();
        }

        Review review = reviewRepository.save(new Review(user.get(), room.get(), requestReviewDto.getRating(), requestReviewDto.getContent()));

        return Optional.ofNullable(review);
    }

    @Override
    public Optional<Review> deleteReview(Long reviewId, Long UserId) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        if(!review.isPresent()){
            throw new ReviewNotFoundException();
        }

        if(!review.get().getUser().getId().equals(UserId)){
            throw new ReviewInvalidRequestException("남의 리뷰 삭제할 수 없습니다.");
        }

        reviewRepository.deleteById(reviewId);
        return review;
    }

    @Override
    public Optional<Review> findById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if(review.isPresent()){
            return review;
        }else{
            throw new ReviewNotFoundException();
        }
    }
}
