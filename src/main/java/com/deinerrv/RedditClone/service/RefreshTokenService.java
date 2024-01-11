package com.deinerrv.RedditClone.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deinerrv.RedditClone.entity.RefreshToken;
import com.deinerrv.RedditClone.exception.SpringRedditException;
import com.deinerrv.RedditClone.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new SpringRedditException("Invalid refresh Token"));
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
