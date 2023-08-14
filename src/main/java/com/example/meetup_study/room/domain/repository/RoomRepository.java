package com.example.meetup_study.room.domain.repository;

import com.example.meetup_study.room.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @EntityGraph(attributePaths = { "hostUserList", "hostUserList.user" })
    Optional<Room> findById(Long id);

    @Query("SELECT DISTINCT r FROM Room r WHERE r.id = :id")
    Optional<Room> findRoomById(@Param("id") Long id);

    @Query("SELECT DISTINCT r FROM Room r " +
            "LEFT JOIN FETCH r.category " +
            "LEFT JOIN FETCH r.hostUserList " +
            "LEFT JOIN FETCH r.roomImage " +
            "WHERE r.id = :id")
    Optional<Room> findRoomWithCategoryAndHostUsersAndImage(@Param("id") Long id);

    @Query("SELECT DISTINCT r FROM Room r ")
    Page<Room> findAllWithCategoryAndHostUsersAndImage(Pageable pageable);

    Page<Room> findByMeetupStartDateBefore(LocalDateTime dateTime, PageRequest pageRequest);

    Page<Room> findByMeetupStartDateAfter(LocalDateTime dateTime, PageRequest pageRequest);

}