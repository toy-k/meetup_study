package com.example.meetup_study.hostUser.domain.repository;

import com.example.meetup_study.hostUser.domain.HostUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HostUserRepository extends JpaRepository<HostUser, Long> {
    Optional<HostUser> findByUserIdAndRoomId(Long userId, Long roomId);

}
