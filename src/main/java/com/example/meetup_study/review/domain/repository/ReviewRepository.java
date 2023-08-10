package com.example.meetup_study.review.domain.repository;

import com.example.meetup_study.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRoomId(Long roomId);

    List<Review> findByUserId(Long userId);

//    @Query("SELECT r FROM Review r WHERE r.user.id = :userId")
    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.user WHERE r.user.id = :userId")
    List<Review> findByUserIdWithFetchJoin(@Param("userId") Long userId);

    Optional<Review> findByUserIdAndRoomId(Long userId, Long roomId);


}
