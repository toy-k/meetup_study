package com.example.meetup_study.hostReview;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.RequestHostReviewDto;

import java.util.List;
import java.util.Optional;

public interface HostReviewService {
    Optional<HostReview> createHostReview(RequestHostReviewDto requestHostReviewDto, Long userId);

    List<HostReview> findByRoomId(Long roomId);

    Optional<HostReview> deleteHostReview(Long hostReviewId, Long userId);
}
