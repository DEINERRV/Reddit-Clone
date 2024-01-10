package com.deinerrv.RedditClone.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deinerrv.RedditClone.dto.PostRequest;
import com.deinerrv.RedditClone.dto.PostResponse;
import com.deinerrv.RedditClone.service.PostService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class PostController {
   
    private final PostService postService;
    
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity
            .ok()
            .body(postService.getById(id));
    }

    @GetMapping("/subreddit/{subredditId}")
    public ResponseEntity<Page<PostResponse>> getAllPostsBySubreddit(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,@PathVariable Long subredditId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return ResponseEntity
            .ok()
            .body(postService.getAlLBySubreddit(pageable,subredditId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostResponse>> getAllPostsByUser(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,@PathVariable Long userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return ResponseEntity
            .ok()
            .body(postService.getAllByUser(pageable,userId));
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return ResponseEntity
            .ok()
            .body(postService.getAll(pageable));
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest posts){
        return ResponseEntity
            .ok()
            .body(postService.save(posts));
    }
    
}
