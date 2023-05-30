package com.example.meetup_study.hostUser;

import com.example.meetup_study.hostUser.domain.HostUser;
import com.example.meetup_study.hostUser.domain.repository.HostUserRepository;
import com.example.meetup_study.hostUser.domain.HostUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HostUserServiceImpl implements HostUserService {

    private final HostUserRepository hostUserRepository;

    @Override
    public Optional<HostUser> getHostUserById(Long id) {
        Optional<HostUser> hostUser = hostUserRepository.findById(id);
        if (!hostUser.isPresent()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }
        return hostUser;

    }

    @Override
    public Optional<HostUser> getHostUserByUserIdAndRoomId(Long userId, Long roomId) {
        Optional<HostUser> hostUser = hostUserRepository.findByUserIdAndRoomId(userId, roomId);
        if (!hostUser.isPresent()) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }
        return hostUser;

    }

}