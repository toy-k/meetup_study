package com.example.meetup_study.image.announceImage.domain.repository;

import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.image.announceImage.domain.AnnounceImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnnounceImageRepository extends JpaRepository<AnnounceImage, Long>{

    @Query("SELECT DISTINCT a FROM Announce a " +
            "LEFT JOIN FETCH a.announceImage " +
            "WHERE a.id = :announceId")
    Optional<Announce> findAnnounceImageAndAnnounceByAnnounceId(@Param("announceId") Long announceId);


}
