package com.example.meetup_study.review.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class ReviewForbiddenException extends ForbiddenException {
    public ReviewForbiddenException() {
        super("Review 작업 금지 되었습니다.");
    }
}
