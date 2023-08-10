package com.example.meetup_study.announce.domain.repository;

import com.example.meetup_study.announce.domain.Announce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceRepository extends JpaRepository<Announce, Long>{

}
