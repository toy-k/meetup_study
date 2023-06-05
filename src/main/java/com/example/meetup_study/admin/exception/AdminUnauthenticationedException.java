package com.example.meetup_study.admin.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class AdminUnauthenticationedException extends UnauthenticatedException {
    public AdminUnauthenticationedException() {
        super("Admin 작업을 수행 인증 되지 않았습니다");
    }
}
