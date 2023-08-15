package com.example.meetup_study.announce.domain.repository;

import com.example.meetup_study.announce.domain.Announce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnnounceRepository extends JpaRepository<Announce, Long>{

    @Query("SELECT DISTINCT a FROM Announce a " +
            "LEFT JOIN FETCH a.user " +
            "WHERE a.id = :announceId")
    Optional<Announce> findAnnounceAndUserByAnnounceId(@Param("announceId") Long announceId);

    @Query("SELECT DISTINCT a FROM Announce a")
    Page<Announce> findAllWithUser(Pageable pageable);
}
