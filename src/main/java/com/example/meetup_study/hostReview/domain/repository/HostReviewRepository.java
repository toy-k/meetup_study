package com.example.meetup_study.hostReview.domain.repository;

import com.example.meetup_study.hostReview.domain.HostReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostReviewRepository extends JpaRepository<HostReview, Long> {
    List<HostReview> findByRoomId(Long roomId);
}
