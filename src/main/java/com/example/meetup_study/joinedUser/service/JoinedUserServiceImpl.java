package com.example.meetup_study.joinedUser.service;

import com.example.meetup_study.joinedUser.domain.JoinedUser;
import com.example.meetup_study.joinedUser.domain.dto.JoinedUserDto;
import com.example.meetup_study.joinedUser.domain.dto.RequestJoinedUserDto;
import com.example.meetup_study.joinedUser.domain.repository.JoinedUserRepository;
import com.example.meetup_study.joinedUser.exception.JoinedUserInvalidRequestException;
import com.example.meetup_study.joinedUser.exception.JoinedUserNotFoundException;
import com.example.meetup_study.mapper.JoinedUserMapper;
import com.example.meetup_study.room.service.RoomService;
import com.example.meetup_study.room.domain.Room;
import com.example.meetup_study.room.domain.enums.RoomStatus;
import com.example.meetup_study.room.exception.RoomNotFoundException;
import com.example.meetup_study.user.fakeUser.exception.UserInvalidRequestException;
import com.example.meetup_study.user.service.UserService;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JoinedUserServiceImpl implements JoinedUserService{

    private final JoinedUserRepository joinedUserRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final JoinedUserMapper joinedUserMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<JoinedUserDto> getJoinedById(Long id) {
        Optional<JoinedUser> joinedUser = joinedUserRepository.findById(id);
        if(!joinedUser.isPresent()){
             throw new JoinedUserNotFoundException();
        }
        JoinedUserDto joinedUserDto = joinedUserMapper.toJoinedUserDto(joinedUser.get());

        return Optional.of(joinedUserDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<JoinedUserDto> getJoinedUserByUserIdAndRoomId(Long userId, Long roomId) {

        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }

        Optional<Room> roomOpt = roomService.getRoom(roomId);
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Optional<JoinedUser> joinedUser = joinedUserRepository.findJoinedUserAndUserAndRoom(userId, roomId);
        if(!joinedUser.isPresent()){
             throw new JoinedUserNotFoundException();
        }

        JoinedUserDto joinedUserDto = joinedUserMapper.toJoinedUserDto(joinedUser.get());

        return Optional.of(joinedUserDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<JoinedUserDto> getJoinedUserByUserId(Long userId) {


        List<JoinedUser> joinedUsers = joinedUserRepository.findByUserId(userId);
        if(joinedUsers.isEmpty()){
             throw new JoinedUserNotFoundException();
        }

        List<JoinedUserDto> joinedUserDtoList = new ArrayList<>();

        for(JoinedUser joinedUser : joinedUsers){
            joinedUserDtoList.add(joinedUserMapper.toJoinedUserDto(joinedUser));
        }


        return joinedUserDtoList;
    }
    @Transactional(readOnly = true)
    @Override
    public List<JoinedUserDto> getJoinedUserByRoomId(Long roomId) {

        List<JoinedUser> joinedUsers = joinedUserRepository.findByRoomId(roomId);
        if(joinedUsers.isEmpty()){
             throw new JoinedUserNotFoundException();
        }

        List<JoinedUserDto> joinedUserDtoList = new ArrayList<>();

        for (JoinedUser joinedUser : joinedUsers) {
            joinedUserDtoList.add(joinedUserMapper.toJoinedUserDto(joinedUser));
        }


        return joinedUserDtoList;
    }


    @Transactional
    @Override
    public Optional<JoinedUserDto> joinRoom(RequestJoinedUserDto requestJoinedUserDto, Long userId){

        Long reqUserId = requestJoinedUserDto.getUserId();
        Long resRoomId = requestJoinedUserDto.getRoomId();

        if(userId != reqUserId){
            throw new UserInvalidRequestException("유저 아이디가 일치하지 않습니다.");
        }


        Optional<JoinedUser> joinedUserOpt = joinedUserRepository.findJoinedUserAndUserAndRoom(reqUserId, resRoomId);
        if(joinedUserOpt.isPresent()){
            throw new JoinedUserInvalidRequestException("이미 참여한 방입니다.");
        }

        Optional<User> userOpt = userService.findById(reqUserId);

        Optional<Room> roomOpt = roomService.getRoom(requestJoinedUserDto.getRoomId());
        if(!roomOpt.isPresent()){
            throw new RoomNotFoundException();
        }

        Room room = roomOpt.get();
        User user = userOpt.get();

        if(room.getCurrentJoinNumber() == room.getMaxJoinNumber()){
            throw new JoinedUserInvalidRequestException("방이 꽉 찼습니다.");
        }


       JoinedUser joinedUser  = new JoinedUser(user, room);
        user.getJoinedUserList().add(joinedUser);
        room.getJoinedUserList().add(joinedUser);
        JoinedUser joinedUserRes = joinedUserRepository.save(joinedUser);


        room.changeCurrentJoinNumber(room.getCurrentJoinNumber() + 1);

        if(room.getCurrentJoinNumber() == room.getMaxJoinNumber()){
            room.changeRoomStatus(RoomStatus.FULL);
        }

        JoinedUserDto joinedUserDto = joinedUserMapper.toJoinedUserDto(joinedUserRes);


        return Optional.of(joinedUserDto);


    }


    @Transactional
    @Override
    public Optional<JoinedUserDto> leaveRoom(RequestJoinedUserDto requestJoinedUserDto, Long userId){

        Long reqUserId = requestJoinedUserDto.getUserId();
        Long resRoomId = requestJoinedUserDto.getRoomId();

        if(userId != reqUserId){
            throw new UserInvalidRequestException("유저 아이디가 일치하지 않습니다.");
        }

        Optional<JoinedUser> joinedUserOpt = joinedUserRepository.findJoinedUserAndUserAndRoom(reqUserId, resRoomId);
        if(joinedUserOpt.isEmpty()){
            throw new JoinedUserInvalidRequestException("해당 유저가 방에 존재하지 않습니다.");
        }

        User user = joinedUserOpt.get().getUser();
        Room room = joinedUserOpt.get().getRoom();

//        user.getJoinedUserList().remove(joinedUserOpt.get());
//        room.getJoinedUserList().remove(joinedUserOpt.get());
        joinedUserRepository.delete(joinedUserOpt.get());

        if(room.getCurrentJoinNumber() == room.getMaxJoinNumber()){
            room.changeRoomStatus(RoomStatus.OPEN);
        }

        room.changeCurrentJoinNumber(room.getCurrentJoinNumber() - 1);

        JoinedUserDto joinedUserDto = joinedUserMapper.toJoinedUserDto(joinedUserOpt.get());

        return Optional.of(joinedUserDto);

    }
}
