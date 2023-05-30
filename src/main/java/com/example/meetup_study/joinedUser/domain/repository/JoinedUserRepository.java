package com.example.meetup_study.joinedUser.domain.repository;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinedUserRepository extends JpaRepository<JoinedUser, Long> {
    Optional<JoinedUser> findByUserIdAndRoomId(Long userId, Long roomId);

    List<JoinedUser> findByRoomId(Long roomId);

    List<JoinedUser> findByUserId(Long userId);
}
