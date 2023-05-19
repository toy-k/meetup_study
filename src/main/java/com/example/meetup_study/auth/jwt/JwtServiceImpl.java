package com.example.meetup_study.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.meetup_study.user.domain.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secretKey}")
    private String SECRETKEY;

    @Value("${jwt.accessToken.expiration}")
    private Long ACCESSTOKENEXPIRATION;
    @Value("${jwt.accessToken.header}")
    private String ACCESSTOKENHEADER;

    @Value("${jwt.refreshToken.expiration}")
    private Long REFRESHTOKENEXPIRATION;
    @Value("${jwt.refreshToken.header}")
    private String REFRESHTOKENHEADER;

    private static final String ACCESS_TOKEN = "AccessToken";
    private static final String REFRESH_TOKEN = "RefreshToken";
    private static final String EMAIL= "email";
    private static final String BEARER = "Bearer ";
    private static final String USERID = "userId";

    private final UserRepository userRepository;

    @Override
    public String generateAccessToken(String email, Long userId) {

        Date now = new Date();

        return JWT.create()
                .withSubject(ACCESS_TOKEN)
                .withExpiresAt(new Date(now.getTime() + ACCESSTOKENEXPIRATION))
                .withClaim(EMAIL, email)
                .withClaim(USERID, userId)
                .sign(Algorithm.HMAC256(SECRETKEY));
    }

    @Override
    public String generateRefreshToken(String email, Long userId) {

        Date now = new Date();

        return JWT.create()
                .withSubject(REFRESH_TOKEN)
                .withExpiresAt(new Date(now.getTime() + REFRESHTOKENEXPIRATION))
                .withClaim(EMAIL, email)
                .withClaim(USERID, userId)
                .sign(Algorithm.HMAC256(SECRETKEY));
    }

    @Override
    public void setResponseAccessToken(HttpServletResponse res, String accessToken) {

        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(Math.toIntExact(ACCESSTOKENEXPIRATION / 1000));
        accessTokenCookie.setPath("/");
        res.addCookie(accessTokenCookie);

        res.setStatus(HttpServletResponse.SC_OK);


    }

    @Override
    public void setResponseRefreshToken(HttpServletResponse res, String refreshToken){

        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(Math.toIntExact(REFRESHTOKENEXPIRATION / 1000));
        refreshTokenCookie.setPath("/");
        res.addCookie(refreshTokenCookie);

        res.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest req) {
        try{
            return Optional.ofNullable(req.getHeader(ACCESSTOKENHEADER))
                    .filter(token -> token.startsWith(BEARER))
                    .map(token -> token.substring(BEARER.length()));
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
            throw new JwtException("잘못된 JWT 시그니처");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            throw new JwtException("유효하지 않은 JWT 토큰");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new JwtException("토큰 기한 만료");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            throw new JwtException("지원되지 않는 JWT 토큰");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new JwtException("JWT token compact of handler are invalid.");
        }
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest req) {

        try {
            return Optional.ofNullable(req.getHeader(REFRESHTOKENHEADER))
                    .filter(token -> token.startsWith(BEARER))
                    .map(token -> token.substring(BEARER.length()));
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
            throw new JwtException("잘못된 JWT 시그니처");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            throw new JwtException("유효하지 않은 JWT 토큰");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new JwtException("토큰 기한 만료");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            throw new JwtException("지원되지 않는 JWT 토큰");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new JwtException("JWT token compact of handler are invalid.");
        }
    }

    @Override
    public Optional<String> extractEmail(String token) {

        try{
            return Optional.ofNullable(JWT.require(Algorithm.HMAC256(SECRETKEY))
                    .build()
                    .verify(token)
                    .getClaim(EMAIL)
                    .asString());
        }catch (Exception e){
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }

    @Override
    public Optional<Long> extractUserId(String token) {

        try{
            return Optional.ofNullable(JWT.require(Algorithm.HMAC256(SECRETKEY))
                    .build()
                    .verify(token)
                    .getClaim(USERID)
                    .asLong());
        }catch (Exception e){
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }

    @Override
    public void updateRefreshToken(Long id, String refreshToken) {

        userRepository.findById(id)
                .ifPresentOrElse(
                        user->user.updateRefreshToken(refreshToken),
                        ()->new RuntimeException("유효하지 않은 유저입니다.")
                );
     }

    @Override
    public boolean isValidAccessToken(String accessToken) {

        try{
            JWT.require(Algorithm.HMAC256(SECRETKEY))
                    .build()
                    .verify(accessToken);
            return true;
        } catch (SecurityException e) {
            log.error("Invalid JWT signature.");
            throw new JwtException("잘못된 JWT 시그니처 입니다.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
            throw new JwtException("유효하지 않은 JWT 토큰 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
            throw new JwtException("토큰 기한 만료 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
            throw new JwtException("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid.");
            throw new JwtException("유효하지 않은 JWT 토큰 형식입니다.");
        } catch (JWTDecodeException e){
            log.error("JWT token decode error.");
            throw new JwtException("JWT 토큰 디코딩 에러입니다.");
        }
    }

    @Override
    public boolean isValidRefreshToken(String refreshToken) {

        try{
            JWT.require(Algorithm.HMAC256(SECRETKEY))
                    .build()
                    .verify(refreshToken);
            return true;
        } catch (SecurityException e) {
            log.error("Invalid JWT signature.");
            throw new JwtException("잘못된 JWT 시그니처 입니다.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
            throw new JwtException("유효하지 않은 JWT 토큰 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
            throw new JwtException("토큰 기한 만료 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
            throw new JwtException("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid.");
            throw new JwtException("유효하지 않은 JWT 토큰 형식입니다.");
        } catch (JWTDecodeException e){
            log.error("JWT token decode error.");
            throw new JwtException("JWT 토큰 디코딩 에러입니다.");
        }
    }
}
