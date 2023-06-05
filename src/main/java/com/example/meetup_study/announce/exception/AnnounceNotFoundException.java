package com.example.meetup_study.announce.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class AnnounceNotFoundException extends NotFoundException {

    public AnnounceNotFoundException() {
        super("유효하지 않은 Announce 입니다.");
    }
}
