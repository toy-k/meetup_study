package com.example.meetup_study.joinedUser;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.domain.repository.JoinedUserRepository;
import com.example.meetup_study.room.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.RoomStatus;
import com.example.meetup_study.room.domain.repository.RoomRepository;
import com.example.meetup_study.user.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JoinedUserServiceImpl implements JoinedUserService{

    private final JoinedUserRepository joinedUserRepository;
    private final UserService userService;
    private final RoomService roomService;

    @Override
    public Optional<JoinedUser> getJoinedUserById(Long id) {
        Optional<JoinedUser> joinedUser = joinedUserRepository.findById(id);
        if(!joinedUser.isPresent()){
            throw new IllegalArgumentException("해당 JoinedUser가 존재하지 않습니다.");
        }
        return joinedUser;
    }

    @Override
    public Optional<JoinedUser> getJoinedUserByUserIdAndRoomId(Long userId, Long roomId) {
        Optional<JoinedUser> joinedUser = joinedUserRepository.findByUserIdAndRoomId(userId, roomId);
        if(!joinedUser.isPresent()){
            throw new IllegalArgumentException("해당 JoinedUser가 존재하지 않습니다.");
        }
        return joinedUser;
    }

    @Override
    public List<JoinedUser> getJoinedUserByUserId(Long userId) {
        List<JoinedUser> joinedUsers = joinedUserRepository.findByUserId(userId);
        if(joinedUsers.isEmpty()){
            throw new IllegalArgumentException("해당 JoinedUser가 존재하지 않습니다.");
        }
        return joinedUsers;
    }
    @Override
    public List<JoinedUser> getJoinedUserByRoomId(Long roomId) {
        List<JoinedUser> joinedUsers = joinedUserRepository.findByRoomId(roomId);
        if(joinedUsers.isEmpty()){
            throw new IllegalArgumentException("해당 JoinedUser가 존재하지 않습니다.");
        }
        return joinedUsers;
    }


    @Transactional
    @Override
    public Optional<JoinedUser> joinRoom(Long userId, Long roomId){

        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()){
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()){
            throw new IllegalArgumentException("해당 방이 존재하지 않습니다.");
        }

        Room room = roomOpt.get();

       JoinedUser joinedUser  = new JoinedUser(userOpt.get(), roomOpt.get());
        userOpt.get().getJoinedUserList().add(joinedUser);
        room.getJoinedUserList().add(joinedUser);
        JoinedUser joinedUserOpt = joinedUserRepository.save(joinedUser);


        if(room.getCurrentJoinNumber() == room.getMaxJoinNumber()){
            throw new IllegalArgumentException("방이 꽉 찼습니다.");
        }

        room.changeCurrentJoinNumber(room.getCurrentJoinNumber() + 1);

        if(room.getCurrentJoinNumber() == room.getMaxJoinNumber()){
            room.changeRoomStatus(RoomStatus.FULL);
        }


        return Optional.of(joinedUserOpt);


    }


    @Transactional
    @Override
    public Optional<JoinedUser> leaveRoom(Long userId, Long roomId){
        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()){
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()){
            throw new IllegalArgumentException("해당 방이 존재하지 않습니다.");
        }

        Room room = roomOpt.get();

        Optional<JoinedUser> joinedUserOpt = joinedUserRepository.findByUserIdAndRoomId(userId, roomId);
        if(!joinedUserOpt.isPresent()){
            throw new IllegalArgumentException("해당 유저가 방에 존재하지 않습니다.");
        }

        //제거
        userOpt.get().getJoinedUserList().remove(joinedUserOpt.get());
        room.getJoinedUserList().remove(joinedUserOpt.get());
        joinedUserRepository.delete(joinedUserOpt.get());

        if(room.getCurrentJoinNumber() == room.getMaxJoinNumber()){
            room.changeRoomStatus(RoomStatus.OPEN);
        }

        room.changeCurrentJoinNumber(room.getCurrentJoinNumber() - 1);


        return joinedUserOpt;

    }
}
