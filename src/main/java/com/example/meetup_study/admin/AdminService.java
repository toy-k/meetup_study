package com.example.meetup_study.admin;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.room.domain.dto.RoomDto;

import java.util.Optional;

public interface AdminService {

    Optional<RoomDto> deleteRoom(Long id, Long userId);

    Optional<Review> deleteReview(Long reviewId, Long UserId);

    Optional<HostReview> deleteHostReview(Long hostReviewId, Long userId);
}
