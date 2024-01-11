package com.deinerrv.RedditClone.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deinerrv.RedditClone.dto.VoteDto;
import com.deinerrv.RedditClone.entity.Post;
import com.deinerrv.RedditClone.entity.Vote;
import com.deinerrv.RedditClone.entity.VoteType;
import com.deinerrv.RedditClone.exception.SpringRedditException;
import com.deinerrv.RedditClone.repository.PostRepository;
import com.deinerrv.RedditClone.repository.VoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new SpringRedditException("Post Not Found with ID - " + voteDto.getPostId()));

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
            voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())
        ) {
            throw new SpringRedditException("You have already "+ voteDto.getVoteType() + "'d for this post");
        }

        //If the person voted, the new vote must be counted by 2, 
        //one to go back to the vote count before they voted 
        //and the other for the new vote.
        int multiplier = 1;
        if(voteByPostAndUser.isPresent()) multiplier = 2;

        if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + (1 * multiplier));
        } else {
            post.setVoteCount(post.getVoteCount() - (1 * multiplier));
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
