package com.example.meetup_study.user.fakeUser.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super("유효하지 않은 유저입니다.");
    }
}
