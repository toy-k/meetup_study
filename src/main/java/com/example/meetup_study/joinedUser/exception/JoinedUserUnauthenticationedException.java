package com.example.meetup_study.joinedUser.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class JoinedUserUnauthenticationedException extends UnauthenticatedException {
    public JoinedUserUnauthenticationedException() {
        super("JoinedUser 작업을 수행 인증 되지 않았습니다");
    }
}
