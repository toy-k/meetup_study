package com.example.meetup_study.user.fakeUser;

import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.dto.ResponseUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FakeUserServiceImpl {

    private final FakeRepository fakeRepository;

    public Optional<User> createFakeUser(User user){
        return Optional.ofNullable(fakeRepository.save(user));
    }

    public Optional<User> deleteFakeUser(User user) {
        fakeRepository.delete(user);
        return Optional.ofNullable(user);
    }

}
