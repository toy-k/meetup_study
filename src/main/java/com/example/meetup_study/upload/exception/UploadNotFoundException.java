package com.example.meetup_study.upload.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class UploadNotFoundException extends NotFoundException {

    public UploadNotFoundException() {
        super("유효하지 않은 업로드 입니다.");
    }
}
