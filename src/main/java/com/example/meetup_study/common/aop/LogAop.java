package com.example.meetup_study.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Aspect
@Component
public class LogAop {


    private static final String LOG_DIRECTORY = "./log/";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Pointcut("execution(* com.example.meetup_study..*(..)) && !execution(* com.example.meetup_study.auth.jwt.JwtAuthenticationProcessingFilter.*(..))")
    private void cut(){}

    @Before("cut()")
    public void before(JoinPoint joinPoint){

        String methodName = getMethodName(joinPoint);
        String className = getClassName(joinPoint);
        String executeClass = joinPoint.getTarget().getClass().getSimpleName();
        log.debug("aop [{}] {}()  / executeClass [{}]", className, methodName, executeClass);

        String logMsg = "CLASS = [ " + className + " ]  / METHOD = [ " + methodName + "() ]" + " / EXECUTECLASS = [ " + executeClass + " ]";

        writeLogToFile(logMsg, LogLevel.INFO);

//        Object[] args = joinPoint.getArgs();
//        if (args.length <= 0) log.info("no parameter");
//        for (Object arg : args) {
//            log.info("parameter : " + arg.getClass().getSimpleName());
//            log.info("parameter : " + arg);
//        }
    }

    @AfterThrowing(value = "cut()", throwing = "exception")
    public void afterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        String methodName = getMethodName(joinPoint);
        String className = getClassName(joinPoint);
        String executeClass = joinPoint.getTarget().getClass().getSimpleName();
        String logMsg = "CLASS = [ " + className + " ]  / METHOD = [ " + methodName + "() ]" + " / EXECUTECLASS = [ " + executeClass + " ]";
        writeLogToFile(logMsg, LogLevel.ERROR);
    }


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
        Class<?> declaringType = signature.getDeclaringType();
        String className;
        if(declaringType == null) className = "null";
        else className = declaringType.getSimpleName();

        return className;
    }


    private void writeLogToFile(String logMessage, LogLevel logLevel) {
        String logFolder = LOG_DIRECTORY + LocalDate.now().format(DATE_FORMATTER);
        createDirectoryIfNotExists(logFolder);

        String logFilePath = logFolder + File.separator + logLevel.name().toLowerCase() + ".log";

        try (FileWriter fileWriter = new FileWriter(logFilePath, true)) {
            String formattedLogMessage = LocalDateTime.now() + " [" + logLevel.name() + "] " + logMessage + "\n";
            fileWriter.write(formattedLogMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private enum LogLevel {
        TRACE,
        DEBUG,
        INFO,
        ERROR
    }


}




