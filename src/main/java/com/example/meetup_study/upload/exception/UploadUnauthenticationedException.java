package com.example.meetup_study.upload.exception;

import com.example.meetup_study.common.exception.UnauthenticatedException;

public class UploadUnauthenticationedException extends UnauthenticatedException {
    public UploadUnauthenticationedException() {
        super("업로드 작업을 수행 인증 되지 않았습니다");
    }
}
