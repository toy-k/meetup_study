package com.example.meetup_study.hostReview.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class HostReviewInvalidRequestException extends InvalidRequestException {
    public HostReviewInvalidRequestException(String msg) {
        super("유효하지 않은 HostReview 요청입니다. "+msg);
    }
    public HostReviewInvalidRequestException() {
        super("유효하지 않은 HostReview 요청입니다.");
    }
}
