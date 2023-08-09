package com.example.meetup_study.review.service;

import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.RequestReviewDto;
import com.example.meetup_study.review.domain.dto.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Optional<ReviewDto> createReview(RequestReviewDto requestReviewDto, Long userId);

    List<ReviewDto> findByRoomId(Long roomId);

    List<ReviewDto> findByUserId(Long userId);

    Optional<ReviewDto> deleteReview(Long reviewId, Long UserId);

    Optional<ReviewDto> findById(Long reviewId);

    Optional<ReviewDto> findByUserIdAndRoomId(Long userId, Long roomId);
}
