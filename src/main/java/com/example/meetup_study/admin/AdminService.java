package com.example.meetup_study.admin;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.review.domain.dto.ReviewDto;
import com.example.meetup_study.room.domain.dto.RoomDto;

import java.util.Optional;

public interface AdminService {

    Optional<RoomDto> deleteRoom(Long id, Long userId);

    Optional<ReviewDto> deleteReview(Long reviewId, Long UserId);

    Optional<HostReviewDto> deleteHostReview(Long hostReviewId, Long userId);
}
