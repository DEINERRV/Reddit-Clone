package com.deinerrv.RedditClone.mapper;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.deinerrv.RedditClone.dto.PostRequest;
import com.deinerrv.RedditClone.dto.PostResponse;
import com.deinerrv.RedditClone.entity.Post;
import com.deinerrv.RedditClone.entity.Subreddit;
import com.deinerrv.RedditClone.entity.User;
import com.deinerrv.RedditClone.entity.Vote;
import com.deinerrv.RedditClone.entity.VoteType;
import com.deinerrv.RedditClone.repository.CommentRepository;
import com.deinerrv.RedditClone.repository.VoteRepository;
import com.deinerrv.RedditClone.service.AuthService;

@Mapper( componentModel = "spring")
public abstract class PostMapper {

    @Autowired private CommentRepository commentRepository;
    @Autowired private VoteRepository voteRepository;
    @Autowired private AuthService authService;

    @Mapping(target = "id", source = "postRequest.postId")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "subredditId", source = "subreddit.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post.getId()))")
    @Mapping(target = "duration", expression = "java(getDuration(post.getCreatedAt()))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    //
    Integer commentCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, VoteType.UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, VoteType.DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser = voteRepository.findTopByPostAndUserOrderByIdDesc(post,authService.getCurrentUser());
            if(voteForPostByUser.isPresent())
                return voteForPostByUser.get().getVoteType() == voteType;
        }

        return false;
    }

    String getDuration(Instant createdAt){
        Instant now = Instant.now();
        Duration duration = Duration.between(createdAt, now);

        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();

        String timeAgo;

        if (days > 0) {
            timeAgo = days + " days ago";
        } else if (hours > 0) {
            timeAgo = hours + " hours ago";
        } else if (minutes > 0) {
            timeAgo = minutes + " minutes ago";
        } else {
            timeAgo = "just now";
        }

        return timeAgo;
    }
}