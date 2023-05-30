package com.example.meetup_study.joinedUser;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.domain.repository.JoinedUserRepository;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JoinedUserServiceImpl implements JoinedUserService{

    private final JoinedUserRepository joinedUserRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<JoinedUser> getJoinedUserById(Long id) {
        Optional<JoinedUser> joinedUser = joinedUserRepository.findById(id);
        if(!joinedUser.isPresent()){
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }
        return joinedUser;
    }

    @Override
    public Optional<JoinedUser> getJoinedUserByUserIdAndRoomId(Long userId, Long roomId) {
        Optional<JoinedUser> joinedUser = joinedUserRepository.findByUserIdAndRoomId(userId, roomId);
        if(!joinedUser.isPresent()){
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }
        return joinedUser;
    }

    @Override
    public List<JoinedUser> getJoinedUserByRoomId(Long roomId) {
        List<JoinedUser> joinedUsers = joinedUserRepository.findByRoomId(roomId);
        if(joinedUsers.isEmpty()){
            throw new IllegalArgumentException("해당 방에 유저가 존재하지 않습니다.");
        }
        return joinedUsers;
    }

    @Override
    public List<JoinedUser> getJoinedUserByUserId(Long userId) {
        List<JoinedUser> joinedUsers = joinedUserRepository.findByUserId(userId);
        if(joinedUsers.isEmpty()){
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }
        return joinedUsers;
    }
}
