package com.deinerrv.RedditClone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.deinerrv.RedditClone.dto.PostRequest;
import com.deinerrv.RedditClone.dto.PostResponse;
import com.deinerrv.RedditClone.entity.Post;
import com.deinerrv.RedditClone.entity.Subreddit;
import com.deinerrv.RedditClone.entity.User;

@Mapper( componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", source = "postRequest.postId")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "subredditId", source = "subreddit.id")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);

}