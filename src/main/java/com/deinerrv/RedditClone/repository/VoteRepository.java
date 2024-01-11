package com.deinerrv.RedditClone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deinerrv.RedditClone.entity.Post;
import com.deinerrv.RedditClone.entity.User;
import com.deinerrv.RedditClone.entity.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long>{
    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User user);
}
