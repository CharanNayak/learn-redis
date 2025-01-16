package com.example.learn_redis.basic;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RedisControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String translateToException(Exception e) {
        e.printStackTrace();
        return e.getMessage();
    }
}
