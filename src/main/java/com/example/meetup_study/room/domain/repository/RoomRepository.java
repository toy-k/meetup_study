package com.example.meetup_study.room.domain.repository;

import com.example.meetup_study.room.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findByMeetupStartDateBefore(LocalDateTime dateTime, PageRequest pageRequest);

    Page<Room> findByMeetupStartDateAfter(LocalDateTime dateTime, PageRequest pageRequest);

}
