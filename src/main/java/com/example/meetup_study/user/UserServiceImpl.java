package com.example.meetup_study.user;

import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.image.userImage.domain.repository.UserImageRepository;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.dto.RequestUserDto;
import com.example.meetup_study.user.domain.dto.UserDto;
import com.example.meetup_study.user.domain.repository.UserRepository;
import com.example.meetup_study.user.fakeUser.exception.UserInvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserDto> findByIdReturnDto(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        Optional<UserDto> userDtoOpt = userOpt.map(user -> new UserDto().converToUserDto(user));

        return userDtoOpt;
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        Optional<UserDto> userDtoOpt = userOpt.map(user -> new UserDto().converToUserDto(user));

        return userDtoOpt;
    }

    @Override
    public List<UserDto> findAllUser() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = userList.stream().map(user -> new UserDto().converToUserDto(user)).collect(Collectors.toList());

        return userDtoList;
    }


    @Transactional
    @Override
    public Optional<UserDto> updateUser(Long id, RequestUserDto requestUserDto) {

        Optional<User> userOpt = userRepository.findById(id);
        if(!userOpt.isPresent()) throw new UserInvalidRequestException();

        Optional<UserDto> userDtoOpt = userOpt.map(user->{
            if (requestUserDto.getUsername()!= null) user.changeUsername(requestUserDto.getUsername());
            if (requestUserDto.getDescription()!= null) user.changeDescription(requestUserDto.getDescription());

            return new UserDto().converToUserDto(user);
        });

        return userDtoOpt;

    }
}
