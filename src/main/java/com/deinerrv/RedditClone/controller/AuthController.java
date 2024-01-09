package com.deinerrv.RedditClone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deinerrv.RedditClone.dto.RegisterRequest;
import com.deinerrv.RedditClone.exception.SpringRedditException;
import com.deinerrv.RedditClone.service.AuthService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest ) throws SpringRedditException{
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Successfully Registered",HttpStatus.OK);
    }
}
