package com.deinerrv.RedditClone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deinerrv.RedditClone.dto.CommentDto;
import com.deinerrv.RedditClone.entity.Comment;
import com.deinerrv.RedditClone.entity.NotificationEmail;
import com.deinerrv.RedditClone.entity.Post;
import com.deinerrv.RedditClone.entity.User;
import com.deinerrv.RedditClone.exception.SpringRedditException;
import com.deinerrv.RedditClone.mapper.CommentMapper;
import com.deinerrv.RedditClone.repository.CommentRepository;
import com.deinerrv.RedditClone.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final MailService mailService;

    @Transactional
    public CommentDto save(CommentDto comment){
        Post post = postRepository.findById(comment.getPostId())
            .orElseThrow(() -> new SpringRedditException("No Post with id - "+comment.getPostId()));
        User currentUser = authService.getCurrentUser();
        Comment savedComment = commentRepository.save(commentMapper.map(comment, post, currentUser));
        
        String emailMessage = currentUser.getUsername() + " posted a comment on your post.\n" ;        sendCommentNotification(null, post.getUser());
        sendCommentNotification(emailMessage, post.getUser());
        
        return commentMapper.mapToDto(savedComment);
    }

    @Transactional(readOnly = true)
    public Page<CommentDto> getAllByPost(Pageable pageable, Long postId){
        Page<Comment> comments = commentRepository.findAllByPostId(pageable, postId);
        return castToDto(comments);
    }

    @Transactional(readOnly = true)
    public Page<CommentDto> getAllByUser(Pageable pageable, Long userId){
        Page<Comment> comments = commentRepository.findAllByUserId(pageable, userId);
        return castToDto(comments);
    }

    private Page<CommentDto> castToDto(Page<Comment> comments){
        List<CommentDto> commentsDto = comments.getContent()
            .stream()
            .map(commentMapper::mapToDto)
            .collect(Collectors.toList());
        
        return new PageImpl<>(commentsDto, comments.getPageable(), comments.getTotalElements());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail("New Comment on your post", user.getEmail(), message));
    }
}
