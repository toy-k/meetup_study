package com.example.meetup_study.hostUser;


import com.example.meetup_study.hostUser.domain.HostUser;

import java.util.List;
import java.util.Optional;

public interface HostUserService {
    Optional<HostUser> getHostUserById(Long id);

    Optional<HostUser> getHostUserByRoomId(Long roomId);

    Optional<HostUser> getHostUserByUserIdAndRoomId(Long userId, Long roomId);
}
