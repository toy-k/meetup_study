package com.example.meetup_study.review.domain.repository;

import com.example.meetup_study.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRoomId(Long roomId);

    List<Review> findByUserId(Long userId);

    Optional<Review> findByUserIdAndRoomId(Long userId, Long roomId);
}
