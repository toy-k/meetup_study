package com.example.meetup_study.auth.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface JwtService {

    String generateToken(String email, Long userId);
    String generateRefreshToken(String email, Long userId);
    void setResponseToken(HttpServletResponse res, String token);//acc, ref
    Optional<String> extractAccessToken(HttpServletRequest req);
    Optional<String> extractRefreshToken(HttpServletRequest req);
    Optional<String> extractEmail(String token);
    Optional<Long> extractUserId(String token);
    void updateRefreshToken(String email, String refreshToken);
    boolean isValidAccessToken(String token);
    boolean isValidRefreshToken(String token);
}
