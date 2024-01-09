package com.deinerrv.RedditClone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deinerrv.RedditClone.entity.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long>{
    Optional<VerificationToken> findByToken(String token);
}
