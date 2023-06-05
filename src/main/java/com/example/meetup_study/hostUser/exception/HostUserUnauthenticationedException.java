package com.example.meetup_study.hostUser.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class HostUserUnauthenticationedException extends UnauthenticatedException {
    public HostUserUnauthenticationedException() {
        super("HostUser 작업을 수행 인증 되지 않았습니다");
    }
}
