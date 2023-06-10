package com.example.meetup_study.user.fakeUser;

import com.example.meetup_study.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FakeRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);
}
