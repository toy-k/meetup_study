package com.example.meetup_study.user.fakeUser.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class UserUnauthenticationedException extends UnauthenticatedException {
    public UserUnauthenticationedException() {
        super("유저는 이 작업을 수행 인증 되지 않았습니다");
    }
}
