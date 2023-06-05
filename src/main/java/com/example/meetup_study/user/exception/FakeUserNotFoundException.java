package com.example.meetup_study.user.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class FakeUserNotFoundException extends NotFoundException {

    public FakeUserNotFoundException() {
        super("유효하지 않은 가짜 유저입니다.");
    }
}
