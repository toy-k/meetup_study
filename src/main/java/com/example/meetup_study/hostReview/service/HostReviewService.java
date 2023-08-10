package com.example.meetup_study.hostReview.service;

import com.example.meetup_study.hostReview.domain.HostReview;
import com.example.meetup_study.hostReview.domain.dto.HostReviewDto;
import com.example.meetup_study.hostReview.domain.dto.RequestHostReviewDto;

import java.util.List;
import java.util.Optional;

public interface HostReviewService {
    Optional<HostReviewDto> createHostReview(RequestHostReviewDto requestHostReviewDto, Long userId);

    List<HostReviewDto> findByRoomId(Long roomId);

    Optional<HostReviewDto> deleteHostReview(Long hostReviewId, Long userId);
}
