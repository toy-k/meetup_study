package com.example.meetup_study.user.domain.repository;

import com.example.meetup_study.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<User> findById(@Param("userId") Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userImage WHERE u.id = :userId")
    Optional<User> findByIdWithUserImage(@Param("userId") Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userImage WHERE u.username = :username")
    Optional<User> findByUsernameWithUserImage(@Param("username") String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userImage")
    List<User> findAllUserWithUserImage();

}
