package com.example.meetup_study.upload.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class UploadInvalidRequestException extends InvalidRequestException {
    public UploadInvalidRequestException(String msg) {
        super("유효하지 않은 업로드 요청입니다. "+msg);
    }
}
