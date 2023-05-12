package com.example.meetup_study.user.domain.repository;

import com.example.meetup_study.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
