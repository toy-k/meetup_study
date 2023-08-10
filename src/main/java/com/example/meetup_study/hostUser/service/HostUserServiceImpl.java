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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HostUserServiceImpl implements HostUserService {

    private final HostUserRepository hostUserRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final HostUserMapper hostUserMapper;

    @Override
    public Optional<HostUserDto> getHostUserById(Long id) {
        Optional<HostUser> hostUserOpt = hostUserRepository.findById(id);
        if (!hostUserOpt.isPresent()) {
            throw new HostUserNotFoundException();
        }

        HostUserDto hostUserDto = hostUserMapper.toHostUserDto(hostUserOpt.get());

        return Optional.of(hostUserDto);

    }

    @Override
    public Optional<HostUser> getHostUserByRoomId(Long roomId) {
        Optional<HostUser> hostUser = hostUserRepository.findByRoomId(roomId);
        if (!hostUser.isPresent()) {
            throw new HostUserNotFoundException();
        }
        return hostUser;

    }

    @Override
    public Optional<HostUserDto> getHostUserByUserIdAndRoomId(Long userId, Long roomId) {

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<HostUser> hostUserOpt = hostUserRepository.findByUserIdAndRoomId(userId, roomId);
        if (!hostUserOpt.isPresent()) {
            throw new HostUserNotFoundException();
        }

        HostUserDto hostUserDto = hostUserMapper.toHostUserDto(hostUserOpt.get());

        return Optional.of(hostUserDto);

    }

}