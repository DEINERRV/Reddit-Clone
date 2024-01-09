package com.deinerrv.RedditClone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.deinerrv.RedditClone.exception.SpringRedditException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    
    @ExceptionHandler(SpringRedditException.class)
    public ResponseEntity<String> err(SpringRedditException err){
        log.info(err.getMessage());
        return new ResponseEntity<>(err.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
