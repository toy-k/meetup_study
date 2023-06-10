package com.example.meetup_study.review;

import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.RequestReviewDto;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Optional<Review> createReview(RequestReviewDto requestReviewDto, Long userId);

    List<Review> findByRoomId(Long roomId);

    List<Review> findByUserId(Long userId);

    Optional<Review> deleteReview(Long reviewId, Long UserId);

    Optional<Review> findById(Long reviewId);

    Optional<Review> findByUserIdAndRoomId(Long userId, Long roomId);
}
