package com.example.meetup_study.joinedUser.domain.repository;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface JoinedUserRepository extends JpaRepository<JoinedUser, Long> {

    @Query("SELECT ju FROM JoinedUser ju WHERE ju.id = :id")
    Optional<JoinedUser> findById(@Param("id") Long id);

    @Query("SELECT DISTINCT ju FROM JoinedUser ju " +
            "LEFT JOIN FETCH ju.user " +
            "LEFT JOIN FETCH ju.room " +
            "WHERE ju.user.id = :userId AND ju.room.id = :roomId"
    )
    Optional<JoinedUser> findJoinedUserAndUserAndRoom(@Param("userId") Long userId, @Param("roomId") Long roomId);

    @Query("SELECT ju FROM JoinedUser ju " +
            "LEFT JOIN FETCH ju.user " +
            "LEFT JOIN FETCH ju.room " +
            "WHERE ju.room.id = :roomId"
    )
    List<JoinedUser> findByRoomId(Long roomId);

    @Query("SELECT ju FROM JoinedUser ju " +
            "LEFT JOIN FETCH ju.user " +
            "LEFT JOIN FETCH ju.room " +
            "WHERE ju.user.id = :userId"
    )
    List<JoinedUser> findByUserId(Long userId);
}
