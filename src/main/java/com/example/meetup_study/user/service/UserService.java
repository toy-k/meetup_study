package com.example.meetup_study.user.service;

import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.dto.RequestUserDto;
import com.example.meetup_study.user.domain.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Optional<UserDto> findByIdWithUserImage(Long id);

    Optional<UserDto> findByUsernameWithUserImage(String username);

    List<UserDto> findAllUserWithUserImage();


    Optional<UserDto> updateUser(Long id, RequestUserDto requestUserDto);
}
