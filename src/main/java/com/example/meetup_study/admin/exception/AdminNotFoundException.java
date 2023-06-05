package com.example.meetup_study.admin.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class AdminNotFoundException extends NotFoundException {

    public AdminNotFoundException() {
        super("유효하지 않은 Admin 입니다.");
    }
}
