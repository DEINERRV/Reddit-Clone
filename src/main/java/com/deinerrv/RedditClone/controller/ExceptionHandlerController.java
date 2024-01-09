package com.deinerrv.RedditClone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.deinerrv.RedditClone.exception.SpringRedditException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    
    @ExceptionHandler(SpringRedditException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String err(SpringRedditException err){
        log.info(err.getMessage());
        return err.getMessage();
    }
}
