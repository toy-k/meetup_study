package com.example.meetup_study.image.userImage.domain.repository;

import com.example.meetup_study.image.userImage.domain.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

}
