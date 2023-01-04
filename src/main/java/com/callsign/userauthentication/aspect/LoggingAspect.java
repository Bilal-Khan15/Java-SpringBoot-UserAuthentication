package com.callsign.userauthentication.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.callsign.userauthentication.service.PersonDetailsService.loadUserByUsername(..))")
    public void beforeLogger(JoinPoint jp) {
        String username = jp.getArgs()[0].toString();
        System.out.println("Request to loadUserByUsername initiated for user: " + username);
    }

    @After("execution(* com.callsign.userauthentication.service.PersonDetailsService.loadUserByUsername(..))")
    public void afterLogger(JoinPoint jp) {
        String username = jp.getArgs()[0].toString();
        System.out.println("Request to loadUserByUsername completed for user: " + username);
    }

    @Pointcut("execution(* com.callsign.userauthentication.service.PersonDetailsService.loadUserByUsername(..))")
    public void afterReturningPointCut() {}

    @AfterReturning(pointcut = "afterReturningPointCut()", returning = "retVal")
    public void afterReturning(UserDetails retVal) {
        UserDetails userDetails = retVal;
        System.out.println("Successful response from loadUserByUsername for user: " + userDetails.getUsername());
    }

    @AfterThrowing("execution(* com.callsign.userauthentication.service.PersonDetailsService.loadUserByUsername(..))")
    public void afterException(JoinPoint jp) {
        String username = jp.getArgs()[0].toString();
        System.out.println("Error response from loadUserByUsername for user: " + username);
    }
}
