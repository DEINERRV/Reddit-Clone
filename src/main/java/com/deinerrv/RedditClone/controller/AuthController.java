package com.deinerrv.RedditClone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deinerrv.RedditClone.dto.AuthenticationResponse;
import com.deinerrv.RedditClone.dto.LoginRequest;
import com.deinerrv.RedditClone.dto.RefreshTokenRequest;
import com.deinerrv.RedditClone.dto.RegisterRequest;
import com.deinerrv.RedditClone.exception.SpringRedditException;
import com.deinerrv.RedditClone.service.AuthService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


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

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> getMethodName(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully",HttpStatus.OK);
    }
    
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshToken){
        authService.logout(refreshToken);
        return ResponseEntity
            .ok()
            .body("Logout Successfully");
    }
    
    
}
