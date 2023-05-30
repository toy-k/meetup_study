package com.example.meetup_study.hostReview;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.RequestHostReviewDto;

import java.util.Optional;

public interface HostReviewService {
    Optional<HostReview> createHostReview(RequestHostReviewDto requestHostReviewDto, Long userId);

    Optional<HostReview> deleteHostReview(Long hostReviewId, Long userId);
}
