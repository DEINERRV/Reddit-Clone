package com.deinerrv.RedditClone.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deinerrv.RedditClone.dto.RegisterRequest;
import com.deinerrv.RedditClone.entity.NotificationEmail;
import com.deinerrv.RedditClone.entity.User;
import com.deinerrv.RedditClone.entity.VerificationToken;
import com.deinerrv.RedditClone.exception.SpringRedditException;
import com.deinerrv.RedditClone.repository.UserRepository;
import com.deinerrv.RedditClone.repository.VerificationTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor //This take care about constructor injection 
public class AuthService {
    

    final PasswordEncoder passwordEncoder;
    final UserRepository userRepository;
    final VerificationTokenRepository verificationTokenRepository;
    final MailService mailService;

    @Transactional
    public void signup(RegisterRequest registerRequest) throws SpringRedditException{
        //Setup the new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedAt(Instant.now());
        user.setEnabled(false);

        if(userRepository.existsByEmail(user.getEmail())){
            throw new SpringRedditException("The email is already registered");
        }

        userRepository.save(user);
        String token = generateVerificationToken(user);

        NotificationEmail email = new NotificationEmail();
        email.setSubject("Please Activate your Account");
        email.setBody(token);
        email.setRecipient(user.getEmail());
        email.setBody("Thank you for signing up to Spring Reddit, " +
        "please click on the below url to activate your account : " +
        "http://localhost:8080/api/auth/accountVerification/" + token);

        mailService.sendMail(email);
    }

    String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        
        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
