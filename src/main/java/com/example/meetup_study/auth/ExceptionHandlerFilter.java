package com.example.meetup_study.auth;

import com.example.meetup_study.auth.exception.AccessTokenInvalidRequestException;
import com.example.meetup_study.auth.exception.TokenNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            filterChain.doFilter(request, response);
        }catch (TokenNotFoundException e){
            System.out.println("[ExceptionHandlerFilter] TokenNotFoundException");
            //토큰의 유효기간 만료
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (JwtException | IllegalArgumentException e){
            System.out.println("[ExceptionHandlerFilter] JwtException | IllegalArgumentException ");
            //유효하지 않은 토큰
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (AccessTokenInvalidRequestException e){
            System.out.println("[ExceptionHandlerFilter] AccessTokenInvalidRequestException ");
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (Exception e){
            System.out.println("[ExceptionHandlerFilter] Exception ");
            setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    private void setErrorResponse(
            HttpServletResponse response,
            HttpStatus errorCode,
            String message
    ){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.value());
        ErrorResponse errorResponse = new ErrorResponse(errorCode.value(), message);
        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class ErrorResponse{
        private final Integer code;
        private final String message;
    }

}
