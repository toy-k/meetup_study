package com.example.meetup_study.joinedUser.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class JoinedUserNotFoundException extends NotFoundException {

    public JoinedUserNotFoundException() {
        super("유효하지 않은 JoinedUser 입니다.");
    }
}
