package com.example.meetup_study.hostUser.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class HostUserNotFoundException extends NotFoundException {

    public HostUserNotFoundException() {
        super("유효하지 않은 HostUser입니다.");
    }
}
