package com.example.meetup_study.auth.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.accessToken.expiration}")
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.accessToken.header}")
    private String accessHeader;

    @Value("${jwt.refreshToken.expiration}")
    private Long refreshTokenExpirationPeriod;
    @Value("${jwt.refreshToken.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN = "AccessToken";
    private static final String REFRESH_TOKEN = "RefreshToken";
    private static final String EMAIL= "email";
    private static final String BEARER = "Bearer ";
    private static final String USERID = "userId";

//    private final UserRepository userRepository;

    @Override
    public String generateToken(String email, Long userId) {
        return null;
    }

    @Override
    public String generateRefreshToken(String email, Long userId) {
        return null;
    }

    @Override
    public void setResponseToken(HttpServletResponse res, String token) {

    }

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest req) {
        return Optional.empty();
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest req) {
        return Optional.empty();
    }

    @Override
    public Optional<String> extractEmail(String token) {
        return Optional.empty();
    }

    @Override
    public Optional<Long> extractUserId(String token) {
        return Optional.empty();
    }

    @Override
    public void updateRefreshToken(String email, String refreshToken) {

    }

    @Override
    public boolean isValidAccessToken(String token) {
        return false;
    }

    @Override
    public boolean isValidRefreshToken(String token) {
        return false;
    }
}
