package com.example.meetup_study.room.domain.repository;

import com.example.meetup_study.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByMeetupStartDateBefore(LocalDateTime dateTime);

    List<Room> findByMeetupStartDateAfter(LocalDateTime dateTime);

}
