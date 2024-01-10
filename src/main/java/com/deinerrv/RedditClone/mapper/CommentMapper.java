package com.deinerrv.RedditClone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.deinerrv.RedditClone.dto.CommentDto;
import com.deinerrv.RedditClone.entity.Comment;
import com.deinerrv.RedditClone.entity.Post;
import com.deinerrv.RedditClone.entity.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment map(CommentDto commentDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    @Mapping(target = "userId", expression = "java(comment.getUser().getId())")
    CommentDto mapToDto(Comment comment);
}