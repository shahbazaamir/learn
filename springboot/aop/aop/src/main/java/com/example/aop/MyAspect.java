package com.example.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAspect.class);

    @Before("execution(* com.example.aop.DemoController.test() )")
    public void check(JoinPoint joinPoint){
        System.out.println("hello"+joinPoint);
        LOGGER.info("hello"+joinPoint);
    }


    @Before("@annotation(CustomAn)")
    public void annotaion(JoinPoint joinPoint){
        System.out.println("hello custom1"+joinPoint);

        LOGGER.info("hello custom1"+joinPoint);

    }

}
