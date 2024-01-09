package com.deinerrv.RedditClone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deinerrv.RedditClone.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long>{

}
