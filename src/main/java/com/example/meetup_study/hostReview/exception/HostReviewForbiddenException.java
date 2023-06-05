package com.example.meetup_study.hostReview.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class HostReviewForbiddenException extends ForbiddenException {
    public HostReviewForbiddenException() {
        super("HostReview 작업 금지 되었습니다.");
    }
}
