package com.example.meetup_study.joinedUser.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class JoinedUserForbiddenException extends ForbiddenException {
    public JoinedUserForbiddenException() {
        super("JoinedUser 작업 금지 되었습니다.");
    }
}
