package com.relabs.test.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class Logger {
    @Pointcut("@annotation(com.relabs.test.logger.Print)")
    public void annotationPointCut(){}

    @Before("annotationPointCut()")
    public void log(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().toString();
        Object[] args = joinPoint.getArgs();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("\u001B[33m" + "\nCalling method\t - \t" + "\u001B[0m" +
                        className + " --> " + "\u001B[34m" + methodName + "()" + "\u001B[0m" +
                        "\u001B[33m" + " \tWith parameters\t - \t" + "\u001B[0m");
        Arrays.stream(args).forEach(param -> stringBuilder
                .append(param.getClass().getSimpleName())
                .append(" : ")
                .append(param.toString())
                .append(" | "));
        log.info(stringBuilder.toString());
    }
}
