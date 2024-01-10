package com.deinerrv.RedditClone.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deinerrv.RedditClone.dto.CommentDto;
import com.deinerrv.RedditClone.service.CommentService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;

    @PostMapping
    public CommentDto createComment(@RequestBody CommentDto comment) {
        return commentService.save(comment);
    }

    @GetMapping("/user/{userId}")
    public Page<CommentDto> getAllByUser(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "5") int size,
        @PathVariable Long userId
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return commentService.getAllByUser(pageable, userId);
    }

    @GetMapping("/post/{postId}")
    public Page<CommentDto> getAllByPost(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "5") int size,
        @PathVariable Long postId
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return commentService.getAllByPost(pageable, postId);
    }
    
    
}
