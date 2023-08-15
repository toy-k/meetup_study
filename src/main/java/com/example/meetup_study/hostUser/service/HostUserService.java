package com.example.meetup_study.hostUser.service;


import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.hostUser.domain.dto.HostUserDto;

import java.util.List;
import java.util.Optional;

public interface HostUserService {


    Optional<HostUserDto> getHostUserById(Long id);

    Optional<HostUser> getHostUserByRoomId(Long roomId);

    Optional<HostUserDto> getHostUserByUserIdAndRoomId(Long userId, Long roomId);
}
