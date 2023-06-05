package com.example.meetup_study.upload.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class UploadForbiddenException extends ForbiddenException {
    public UploadForbiddenException() {
        super("업로드 작업 금지 되었습니다.");
    }
}
