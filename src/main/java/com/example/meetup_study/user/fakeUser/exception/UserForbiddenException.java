package com.example.meetup_study.user.fakeUser.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class UserForbiddenException extends ForbiddenException {
    public UserForbiddenException() {
        super("유저는 이 작업 금지 되었습니다.");
    }
}
