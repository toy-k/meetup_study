package com.example.meetup_study.user.service;

import com.example.meetup_study.mapper.UserMapper;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.dto.RequestUserDto;
import com.example.meetup_study.user.domain.dto.UserDto;
import com.example.meetup_study.user.domain.repository.UserRepository;
import com.example.meetup_study.user.fakeUser.exception.UserInvalidRequestException;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDto> findByIdWithUserImage(Long id) {

        Optional<User> userOpt = userRepository.findByIdWithUserImage(id);

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }
        Optional<UserDto> userDtoOpt = userOpt.map(user -> userMapper.toUserDto(user));

        return userDtoOpt;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserDto> findByUsernameWithUserImage(String username) {
        Optional<User> userOpt = userRepository.findByUsernameWithUserImage(username);

        if(!userOpt.isPresent()){
            throw new UserNotFoundException();
        }
        Optional<UserDto> userDtoOpt = userOpt.map(user -> userMapper.toUserDto(user));

        return userDtoOpt;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAllUserWithUserImage() {
        List<User> userList = userRepository.findAllUserWithUserImage();

//        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = userList.stream().map(user -> userMapper.toUserDto(user)).collect(Collectors.toList());
        return userDtoList;
    }


    @Transactional
    @Override
    public Optional<UserDto> updateUser(Long id, RequestUserDto requestUserDto) {

        Optional<User> userOpt = userRepository.findById(id);

        Optional<UserDto> userDtoOpt = userOpt.map(user->{
            if (requestUserDto.getDescription()!= null) user.changeDescription(requestUserDto.getDescription());
            return userMapper.toUserDto(user);
        });
        return userDtoOpt;

    }
}
