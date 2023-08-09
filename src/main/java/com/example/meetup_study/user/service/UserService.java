package com.example.meetup_study.user.service;

import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.dto.RequestUserDto;
import com.example.meetup_study.user.domain.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    Optional<UserDto> findByIdReturnDto(Long id);

    Optional<UserDto> findByUsername(String username);

    List<UserDto> findAllUser();


    Optional<UserDto> updateUser(Long id, RequestUserDto requestUserDto);
}
