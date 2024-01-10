package com.deinerrv.RedditClone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deinerrv.RedditClone.dto.PostRequest;
import com.deinerrv.RedditClone.dto.PostResponse;
import com.deinerrv.RedditClone.entity.Post;
import com.deinerrv.RedditClone.entity.Subreddit;
import com.deinerrv.RedditClone.entity.User;
import com.deinerrv.RedditClone.exception.SpringRedditException;
import com.deinerrv.RedditClone.mapper.PostMapper;
import com.deinerrv.RedditClone.repository.PostRepository;
import com.deinerrv.RedditClone.repository.SubredditRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    @Transactional
    public PostResponse save(PostRequest postRequest){
        Subreddit subreddit = subredditRepository.findById(postRequest.getSubredditId())
            .orElseThrow(() -> new SpringRedditException("Subreddit Not Found"));
        User currentUser = authService.getCurrentUser();
        Post savedPost = postRepository.save(postMapper.map(postRequest, subreddit, currentUser));
        return postMapper.mapToDto(savedPost);
    }

    @Transactional(readOnly = true)
    public PostResponse getById(Long id){
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new SpringRedditException("Post Not Found"));
        
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAll(Pageable pageable){
        Page<Post> posts = postRepository.findAll(pageable);
        return castToDto(posts);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAlLBySubreddit(Pageable pageable, Long subredditId){
        Page<Post> posts = postRepository.findAllBySubredditId(pageable, subredditId);
        return castToDto(posts);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllByUser(Pageable pageable, Long userId){
        Page<Post> posts = postRepository.findAllByUserId(pageable, userId);
        return castToDto(posts);
    }

    private Page<PostResponse> castToDto(Page<Post> posts){
        List<PostResponse> postsDto = posts
            .stream()
            .map(postMapper::mapToDto)
            .collect(Collectors.toList());
        
        return new PageImpl<>(postsDto, posts.getPageable(), posts.getTotalElements());
    }
}
