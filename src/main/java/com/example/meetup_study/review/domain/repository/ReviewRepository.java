package com.example.meetup_study.review.domain.repository;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.user WHERE r.user.id = :userId AND r.room.id = :roomId")
    Optional<Review> findByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.user WHERE r.room.id = :roomId")
    List<Review> findByRoomId(@Param("roomId") Long roomId);


    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.user WHERE r.user.id = :userId")
    List<Review> findByUserId(@Param("userId") Long userId);


    @Query("SELECT r FROM Room r " +
            "JOIN FETCH r.joinedUserList ju " +
            "JOIN FETCH ju.user u " +
            "WHERE r.id = :roomId")
    Optional<Room> findRoomWithJoinedUsersAndUsersById(@Param("roomId") Long roomId);

    @Query("SELECT r FROM Review r " +
            "JOIN FETCH r.room " +
            "JOIN FETCH r.user " +
            "WHERE r.id = :reviewId")
    Optional<Review> findReviewWithRoomAndUserById(@Param("reviewId") Long reviewId);
}
