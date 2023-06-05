package com.example.meetup_study.review.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class ReviewInvalidRequestException extends InvalidRequestException {
    public ReviewInvalidRequestException(String msg) {
        super("유효하지 않은 Review 요청입니다. "+msg);
    }
}
