package com.example.meetup_study.user.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class FakeUserUnauthenticationedException extends UnauthenticatedException {
    public FakeUserUnauthenticationedException() {
        super("가짜 유저는 이 작업을 수행 인증 되지 않았습니다");
    }
}
