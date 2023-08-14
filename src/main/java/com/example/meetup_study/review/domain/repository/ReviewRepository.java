package com.example.meetup_study.review.domain.repository;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.review.domain.Review;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.user.domain.User;
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


    @Query("SELECT r FROM Room r " +
            "JOIN FETCH r.joinedUserList ju " +
            "JOIN FETCH ju.user u " +
            "WHERE r.id = :roomId")
    Optional<Room> findRoomWithJoinedUsersAndUsersById(Long roomId);

}
