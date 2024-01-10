package com.deinerrv.RedditClone.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deinerrv.RedditClone.dto.SubredditDto;
import com.deinerrv.RedditClone.service.SubredditService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/subreddit")
@RequiredArgsConstructor
public class SubredditController {
    
    private final SubredditService subredditService;

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubredditById(@PathVariable Long id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(subredditService.getSubreddit(id));
    }
    
    @GetMapping
    public ResponseEntity<Page<SubredditDto>> getAllSubreddits(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pagination = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(subredditService.getAll(pagination));
    }


    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subreddit) {
        
        return ResponseEntity
            .ok()
            .body(subredditService.create(subreddit));
    }
    
    
}
