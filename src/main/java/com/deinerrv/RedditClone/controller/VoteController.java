package com.deinerrv.RedditClone.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deinerrv.RedditClone.dto.VoteDto;
import com.deinerrv.RedditClone.service.VoteService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {
    
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto vote) {
        voteService.vote(vote);     
        return ResponseEntity.ok().build();
    }
    
}
