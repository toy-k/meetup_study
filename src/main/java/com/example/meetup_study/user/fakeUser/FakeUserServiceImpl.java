package com.example.meetup_study.user.fakeUser;

import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.dto.ResponseUserDto;
import com.example.meetup_study.user.fakeUser.exception.UserInvalidRequestException;
import com.example.meetup_study.user.fakeUser.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FakeUserServiceImpl {

    private final FakeRepository fakeUserRepository;

    public Optional<User> createFakeUser(User user){

        if(fakeUserRepository.existsByEmail(user.getEmail())){
            throw new UserInvalidRequestException("중복 유저 회원가입 요청 실패입니다.");
        }

        return Optional.ofNullable(fakeUserRepository.save(user));
    }

    public Optional<User> deleteFakeUser(User user) {
        if(!fakeUserRepository.existsByEmail(user.getEmail())){
            throw new UserNotFoundException();
        }
        fakeUserRepository.delete(user);
        return Optional.ofNullable(user);
    }
    public Optional<User> updateRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
        return Optional.ofNullable(fakeUserRepository.save(user));
    }

    public List<User> findAll(){
        return fakeUserRepository.findAll();
    }

}
