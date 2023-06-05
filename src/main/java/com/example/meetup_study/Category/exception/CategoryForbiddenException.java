package com.example.meetup_study.Category.exception;

import com.example.meetup_study.common.exception.ForbiddenException;

public class CategoryForbiddenException extends ForbiddenException {
    public CategoryForbiddenException() {
        super("Category 작업 금지 되었습니다.");
    }
}
