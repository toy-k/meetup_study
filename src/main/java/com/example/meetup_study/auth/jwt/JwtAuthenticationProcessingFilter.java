package com.example.meetup_study.auth.jwt;

import com.example.meetup_study.user.domain.User;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private String PASS_URL_1 = "/login";
    private String PASS_URL_2 = "/oauth2/authorization/google";
    private String PASS_URL_3 = "/login/oauth2/code/google";
    private String PASS_URL_4 = "/";
    private String PASS_URL_5 = "/fakeuser";
    private String PASS_URL_6 = "/api/user";
    private String PASS_URL_7 = "/api/room";
    private String PASS_URL_8 = "/test";

    private final String AUTHORIZATION = "Authorization";
    private final String BEARER = "Bearer ";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("[JwtAuthenticationProcessingFilter] doFilterInternal()");


        String reqUri = request.getRequestURI();
        log.debug("[JwtAuthenticationProcessingFilter] reqUri: {}", reqUri);

        if(reqUri.equals(PASS_URL_1) ||
            reqUri.equals(PASS_URL_2) ||
            reqUri.equals(PASS_URL_3) ||
            reqUri.equals(PASS_URL_4) ||
            reqUri.contains(PASS_URL_5) ||
            (reqUri.contains(PASS_URL_6) && !reqUri.equals(PASS_URL_6 + "/me")) ||
            (reqUri.contains(PASS_URL_7) && !reqUri.equals(PASS_URL_7)) ||
            reqUri.contains(PASS_URL_8)
        ){
            log.debug("[JwtAuthenticationProcessingFilter] pass");
            filterChain.doFilter(request, response);
            return;
        }


        log.debug("[JwtAuthenticationProcessingFilter] ==============================");


        String accessToken = jwtService.extractAccessToken(request).filter(jwtService::isValidAccessToken).orElse(null);
        String refreshToken = null;

        //accesstoken 이 널이면
        if(accessToken == null){
            refreshToken = jwtService.extractRefreshToken(request).filter(jwtService::isValidRefreshToken).orElse(null);
        }

        log.debug("accessToken : {}", accessToken);
        log.debug("refreshToken : {}", refreshToken);


        if (accessToken == null) {
            if(refreshToken == null) {
                throw new RuntimeException("엑세스토큰과 리프레쉬토큰 둘 다 없습니다.");
            }else{
                this.generateAccessToken(request, response, refreshToken);
            }
        } else if (accessToken != null) {
            try {
                this.isValidAccessToken(request);
            } catch (Exception ex) {
                throw new RuntimeException("유효한 엑세스토큰이 없습니다.");
            }
        }



        filterChain.doFilter(request, response);
        
    }

    private void generateAccessToken(HttpServletRequest req, HttpServletResponse res, String refreshToken){
        log.debug("[JwtAuthenticationProcessingFilter] generateAccessToken()");

        Optional<Long> userId = jwtService.extractUserId(refreshToken);
        if(!userId.isPresent()){
            throw new RuntimeException("유저 아이디가 없습니다.");
        }
        User user = userRepository.findById(userId.get()).orElseThrow(() -> new RuntimeException("유저가 없습니다."));

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getId());

        req.setAttribute(AUTHORIZATION, BEARER + accessToken);

        jwtService.setResponseAccessToken(res, accessToken);

        this.saveAuthentication(user);
    }

    public void isValidAccessToken(HttpServletRequest req){
        log.debug("[JwtAuthenticationProcessingFilter] isValidAccessToken()");

        Optional<String> accessTokenOpt = jwtService.extractAccessToken(req);
        if(accessTokenOpt.isPresent()){
            String accessToken = accessTokenOpt.get();
            if(jwtService.isValidAccessToken(accessToken)){
                Optional<String> emailOpt = jwtService.extractEmail(accessToken);
                if(emailOpt.isPresent()) {
                    String email = emailOpt.get();
                    Optional<User> userOpt = userRepository.findByEmail(email);
                    if(userOpt.isPresent()){
                        saveAuthentication(userOpt.get());
                    }
                }
            }

        }

        
    }

    public void saveAuthentication(User user){
        log.debug("[JwtAuthenticationProcessingFilter] saveAuthentication()");

        String pwd = user.getPassword();
        if(pwd == null){
            pwd = UUID.randomUUID().toString();
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(pwd)
                .roles(user.getRoleType().toString())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

}
