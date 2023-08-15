package com.example.meetup_study.hostUser.domain.repository;

import com.example.meetup_study.hostUser.domain.HostUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HostUserRepository extends JpaRepository<HostUser, Long> {

    @Query("SELECT DISTINCT hu FROM HostUser hu " +
            "LEFT JOIN FETCH hu.user " +
            "LEFT JOIN FETCH hu.room " +
            "WHERE hu.id = :id")
    Optional<HostUser> findHostUserAndUserAndRoomByHostUserId(@Param("id") Long id);

    @Query("SELECT DISTINCT hu FROM HostUser hu " +
            "LEFT JOIN FETCH hu.user " +
            "LEFT JOIN FETCH hu.room " +
            "WHERE hu.room.id = :roomId")
    Optional<HostUser> findHostUserAndUserAndRoomByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT DISTINCT hu FROM HostUser hu " +
            "LEFT JOIN FETCH hu.user " +
            "LEFT JOIN FETCH hu.room " +
            "WHERE hu.user.id = :userId AND hu.room.id = :roomId"
    )
    Optional<HostUser> findHostUserAndUserAndRoomByUserIdAndRoomId(@Param("userId") Long userId,@Param("roomId") Long roomId);

    Optional<HostUser> findByRoomId(Long roomId);
}
