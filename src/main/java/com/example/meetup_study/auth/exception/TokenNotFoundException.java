package com.example.meetup_study.auth.exception;

import com.example.meetup_study.common.exception.NotFoundException;

public class TokenNotFoundException extends NotFoundException {
    public TokenNotFoundException() {
        super("엑세스 토큰과 리프레쉬 토큰 둘 다 유효하지않습니다. 재로그인 해주세요.");
    }
}
