package com.example.meetup_study.auth.jwt;

import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            (reqUri.contains(PASS_URL_7) && !reqUri.equals(PASS_URL_7))
        ){
            log.debug("[JwtAuthenticationProcessingFilter] pass");
            filterChain.doFilter(request, response);
            return;
        }


        log.debug("[JwtAuthenticationProcessingFilter] ==============================");


        filterChain.doFilter(request, response);
        
    }
}
