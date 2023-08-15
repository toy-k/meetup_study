package com.example.meetup_study.image.roomImage.domain.repository;

import com.example.meetup_study.image.roomImage.domain.RoomImage;
import com.example.meetup_study.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {

    @Query("SELECT DISTINCT r FROM Room r " +
            "LEFT JOIN FETCH r.category " +
            "LEFT JOIN FETCH r.hostUserList " +
            "LEFT JOIN FETCH r.roomImage " +
            "WHERE r.id = :id")
    Optional<Room> findRoomWithCategoryAndHostUsersAndImage(@Param("id") Long id);

    @Query("SELECT DISTINCT r FROM Room r " +
            "LEFT JOIN FETCH r.roomImage " +
            "WHERE r.id = :roomId")
    Optional<Room> findRoomAndRoomImageByRoomId(@Param("roomId") Long roomId);
}
