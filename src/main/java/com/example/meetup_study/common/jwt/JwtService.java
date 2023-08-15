package com.example.meetup_study.common.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface JwtService {

    String generateAccessToken(String email, Long userId);
    String generateRefreshToken(String email, Long userId);
    void setResponseAccessToken(HttpServletResponse res, String accessToken);//acc, ref
    void setResponseRefreshToken(HttpServletResponse res, String refreshTtoken);//acc, ref
    Optional<String> extractAccessToken(HttpServletRequest req);
    Optional<String> extractRefreshToken(HttpServletRequest req);
    Optional<String> extractEmail(String token);
    Optional<Long> extractUserId(String token);
    void updateRefreshToken(Long id, String refreshToken);
    boolean isValidAccessToken(String accessToken);
    boolean isValidRefreshToken(String refreshToken);
}
