package com.example.meetup_study.joinedUser.service;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.domain.dto.JoinedUserDto;
import com.example.meetup_study.joinedUser.domain.dto.RequestJoinedUserDto;

import java.util.List;
import java.util.Optional;

public interface JoinedUserService {
    Optional<JoinedUserDto> getJoinedUserById(Long id);

    Optional<JoinedUserDto> getJoinedUserByUserIdAndRoomId(Long userId, Long roomId);

    List<JoinedUserDto> getJoinedUserByRoomId(Long roomId);

    List<JoinedUserDto> getJoinedUserByUserId(Long userId);

    Optional<JoinedUserDto> joinRoom(RequestJoinedUserDto requestJoinedUserDto, Long userId);

    Optional<JoinedUserDto> leaveRoom(RequestJoinedUserDto requestJoinedUserDto, Long userId);
}
