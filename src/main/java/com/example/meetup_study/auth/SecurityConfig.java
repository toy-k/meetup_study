package com.example.meetup_study.auth;

import com.example.meetup_study.auth.jwt.JwtAuthenticationProcessingFilter;
import com.example.meetup_study.auth.jwt.JwtService;
import com.example.meetup_study.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DefaultOAuth2UserService customOauth2UserService;
    private final AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable();

        http
                .authorizeRequests()
                .antMatchers("/api/room", "/api/user/me").access("hasRole('ROLE_USER')")
                .anyRequest().permitAll()
                .and() //localhost:8080/login
                    .oauth2Login().userInfoEndpoint().userService(customOauth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        http
                .addFilterAfter(jwtAuthenticationProcessingFilter(), LogoutFilter.class);
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {

        JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
        return jwtAuthenticationProcessingFilter;
        }
}
