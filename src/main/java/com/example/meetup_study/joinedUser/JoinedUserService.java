package com.example.meetup_study.joinedUser;

import com.example.meetup_study.joinedUser.domain.JoinedUser;

import java.util.List;
import java.util.Optional;

public interface JoinedUserService {
    Optional<JoinedUser> getJoinedUserById(Long id);

    Optional<JoinedUser> getJoinedUserByUserIdAndRoomId(Long userId, Long roomId);

    List<JoinedUser> getJoinedUserByRoomId(Long roomId);

    List<JoinedUser> getJoinedUserByUserId(Long userId);
}
