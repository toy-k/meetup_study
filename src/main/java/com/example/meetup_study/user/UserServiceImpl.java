package com.example.meetup_study.user;

import com.example.meetup_study.image.userImage.domain.UserImage;
import com.example.meetup_study.image.userImage.domain.repository.UserImageRepository;
import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.dto.UserDto;
import com.example.meetup_study.user.domain.repository.UserRepository;
import com.example.meetup_study.user.fakeUser.exception.UserInvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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

    @Transactional
    @Override
    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        if(!id.equals(userDto.getId())) throw new UserInvalidRequestException("id가 일치하지 않습니다.");

        Optional<User> userOpt = userRepository.findById(id);
        if(!userOpt.isPresent()) throw new UserInvalidRequestException();

        Optional<UserDto> userDtoOpt = userOpt.map(user->{
            if (userDto.getUsername()!= null) user.changeUsername(userDto.getUsername());
            if (userDto.getDescription()!= null) user.changeDescription(userDto.getDescription());

            return new UserDto().converToUserDto(user);
        });

        return userDtoOpt;

    }
}
