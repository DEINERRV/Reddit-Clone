package com.deinerrv.RedditClone.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deinerrv.RedditClone.dto.AuthenticationResponse;
import com.deinerrv.RedditClone.dto.LoginRequest;
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
    

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;


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

    public void verifyAccount(String token){
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));

        fetchUserAndEnable(verificationToken.get());
    }


    @Transactional
    public void fetchUserAndEnable(VerificationToken token){
        Long userId = token.getUser().getId();

        User user = userRepository.findById(userId).orElseThrow(() -> new SpringRedditException("No user found for validation"));
        user.setEnabled(true);
        userRepository.save(user);

        verificationTokenRepository.deleteById(token.getId());

        NotificationEmail email = new NotificationEmail();
        email.setSubject("Account Verified");
        email.setRecipient(user.getEmail());
        email.setBody("Your Account has been successfully verified");

        mailService.sendMail(email);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword()));
                
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtProvider.generateToken(authenticate);

        return AuthenticationResponse.builder()
            .authenticationToken(token)
            //.refreshToken(refreshTokenService.generateRefreshToken().getToken())
            .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
            .username(loginRequest.getEmail())
            .build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder
            .getContext().getAuthentication().getPrincipal();

        return userRepository.findByEmail(principal.getSubject())
                .orElseThrow(() -> new SpringRedditException("Account not found with this email - " + principal.getSubject()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
