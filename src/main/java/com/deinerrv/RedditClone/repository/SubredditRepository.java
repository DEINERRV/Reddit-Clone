package com.deinerrv.RedditClone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deinerrv.RedditClone.entity.Subreddit;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit,Long>{
    
}
