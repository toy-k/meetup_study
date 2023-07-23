package com.example.meetup_study.room.domain.repository;

import com.example.meetup_study.room.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findByMeetupStartDateBefore(LocalDateTime dateTime, PageRequest pageRequest);

    Page<Room> findByMeetupStartDateAfter(LocalDateTime dateTime, PageRequest pageRequest);



    //@Query("SELECT r FROM Room r LEFT JOIN FETCH r.hostUserList hu LEFT JOIN FETCH hu.user WHERE r.id = :id")
    //left outer join  == entitygraph == left join fetch , fetch join == inner joim
    // 연관 모든 데이터 호출시 entitygraph , 특정 데이터만 원할때 fetch join
    // 쓰기지연 덕분에 룸만 호출할지 연관 데이터호출할지 쿼리 나중에 결정

    @EntityGraph(attributePaths = { "hostUserList", "hostUserList.user" })
    Optional<Room> findById(Long id);

}
