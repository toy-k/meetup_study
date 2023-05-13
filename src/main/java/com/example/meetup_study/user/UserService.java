package com.example.meetup_study.user;

import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.dto.UserDto;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);
    Optional<UserDto> findByIdReturnDto(Long id);
    Optional<UserDto> updateUser(Long id, UserDto userDto);
}
