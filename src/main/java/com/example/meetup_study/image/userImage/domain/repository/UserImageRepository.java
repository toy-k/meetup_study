package com.example.meetup_study.image.userImage.domain.repository;

import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userImage WHERE u.id = :userId")
    Optional<User> findByIdWithUserImage(@Param("userId") Long userId);

}
