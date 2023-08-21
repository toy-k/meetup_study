package com.example.meetup_study.hostUser.service;

import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.hostUser.domain.dto.HostUserDto;
import com.example.meetup_study.hostUser.domain.repository.HostUserRepository;
import com.example.meetup_study.hostUser.exception.HostUserNotFoundException;
import com.example.meetup_study.mapper.HostUserMapper;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.room.service.RoomService;
import com.example.meetup_study.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HostUserServiceImpl implements HostUserService {

    private final HostUserRepository hostUserRepository;
    private final HostUserMapper hostUserMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<HostUserDto> getHostUserById(Long id) {
        Optional<HostUser> hostUserOpt = hostUserRepository.findHostUserAndUserAndRoomByHostUserId(id);
        if (!hostUserOpt.isPresent()) {
            throw new HostUserNotFoundException();
        }

        HostUserDto hostUserDto = hostUserMapper.toHostUserDto(hostUserOpt.get());

        return Optional.of(hostUserDto);

    }

    @Transactional(readOnly = true)
    @Override
    public Optional<HostUser> getHostUserByRoomId(Long roomId) {
        Optional<HostUser> hostUser = hostUserRepository.findHostUserAndUserAndRoomByRoomId(roomId);
        if (!hostUser.isPresent()) {
            throw new HostUserNotFoundException();
        }
        return hostUser;

    }

    @Transactional(readOnly = true)
    @Override
    public Optional<HostUserDto> getHostUserByUserIdAndRoomId(Long userId, Long roomId) {

        Optional<HostUser> hostUserOpt = hostUserRepository.findHostUserAndUserAndRoomByUserIdAndRoomId(userId, roomId);
        if (!hostUserOpt.isPresent()) {
            throw new HostUserNotFoundException();
        }

        HostUserDto hostUserDto = hostUserMapper.toHostUserDto(hostUserOpt.get());

        return Optional.of(hostUserDto);

    }

}