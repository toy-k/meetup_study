package com.example.meetup_study.room.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class RoomNotFoundException extends NotFoundException {

    public RoomNotFoundException() {
        super("유효하지 않은 Room 입니다.");
    }
}
