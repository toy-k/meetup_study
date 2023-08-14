package com.example.meetup_study.review.service;

import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.RequestReviewDto;
import com.example.meetup_study.review.domain.dto.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Optional<ReviewDto> createReview(RequestReviewDto requestReviewDto, Long userId);

    List<ReviewDto> findReviewListByRoomId(Long roomId);

    List<ReviewDto> findReviewListByUserId(Long userId);

    Boolean deleteReview(Long reviewId, Long UserId);

    Optional<ReviewDto> findById(Long reviewId);

}
