package com.example.meetup_study.user.fakeUser;

import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.dto.ResponseUserDto;
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
        return Optional.ofNullable(fakeUserRepository.save(user));
    }

    public Optional<User> deleteFakeUser(User user) {
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
