package com.example.meetup_study.joinedUser.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class JoinedUserInvalidRequestException extends InvalidRequestException {
    public JoinedUserInvalidRequestException(String msg) {
        super("유효하지 않은 JoinedUser 요청입니다." + msg);
    }
}
