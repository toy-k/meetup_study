package com.example.meetup_study.user.fakeUser.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class UserInvalidRequestException extends InvalidRequestException {
    public UserInvalidRequestException() {
        super("유효하지 않은 유저 요청입니다.");
    }
    public UserInvalidRequestException(String msg) {
        super("유효하지 않은 유저 요청입니다. "+msg);
    }

}
