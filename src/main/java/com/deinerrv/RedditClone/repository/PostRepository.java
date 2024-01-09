package com.deinerrv.RedditClone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deinerrv.RedditClone.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>{
    
}
