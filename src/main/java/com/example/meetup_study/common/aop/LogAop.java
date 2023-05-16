package com.example.meetup_study.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LogAop {

    //com.example.meetup_study.auth 제외
//    @Pointcut("execution(* com.example.meetup_study..*(..)) && !execution(* com.example.meetup_study.auth.jwt..*(..))")
    @Pointcut("execution(* com.example.meetup_study..*(..)) && !execution(* com.example.meetup_study.auth.jwt.JwtAuthenticationProcessingFilter.*(..))")
    private void cut(){}

    @Before("cut()")
    public void before(JoinPoint joinPoint){

        String methodName = getMethodName(joinPoint);
        String className = getClassName(joinPoint);

        log.debug("aop [{}] {}()", className, methodName);


//        Object[] args = joinPoint.getArgs();
//        if (args.length <= 0) log.info("no parameter");
//        for (Object arg : args) {
//            log.info("parameter : " + arg.getClass().getSimpleName());
//            log.info("parameter : " + arg);
//        }
    }

//    @AfterReturning(value = "cut()", returning = "returnObj")
//    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
//
//        String methodName = getMethodName(joinPoint);
//        String className = getClassName(joinPoint);
//
//        log.debug("aop [{}] {}()", className, methodName);
//
//        log.debug("aop return type = {}", returnObj.getClass().getSimpleName());
//        log.debug("aop return value = {}", returnObj);
//    }

//    @AfterThrowing(value = "cut()", throwing = "exception")
//    public void afterThrowingLog(JoinPoint joinPoint, Exception exception) {
//
//        String methodName = getMethodName(joinPoint);
//        String className = getClassName(joinPoint);
//
//        log.debug("[{}] {}()", className, methodName);
//
//        log.error("exception = {}", exception);
//    }


    private String getMethodName(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName;
        if(method == null) methodName = "null";
        else methodName = method.getName();
        return methodName;
    }

    private String getClassName(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //signature.getDeclaringType()를 변수에 담아
        Class<?> declaringType = signature.getDeclaringType();
        String className;
        if(declaringType == null) className = "null";
        else className = declaringType.getSimpleName();

        return className;
    }
}
