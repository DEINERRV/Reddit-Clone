package com.deinerrv.RedditClone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deinerrv.RedditClone.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    
}
