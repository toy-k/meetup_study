package com.example.meetup_study.Category.exception;

import com.example.meetup_study.common.exception.InvalidRequestException;

public class CategoryInvalidRequestException extends InvalidRequestException {
    public CategoryInvalidRequestException() {
        super("유효하지 않은 Category 요청입니다.");
    }
}
