package com.deinerrv.RedditClone.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deinerrv.RedditClone.dto.SubredditDto;
import com.deinerrv.RedditClone.entity.Subreddit;
import com.deinerrv.RedditClone.exception.SpringRedditException;
import com.deinerrv.RedditClone.mapper.SubredditMapper;
import com.deinerrv.RedditClone.repository.SubredditRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubredditService {
    
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    public SubredditDto create(SubredditDto subredditDto) {
        subredditDto.setId(null);
        Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);
        subreddit.setCreatedAt(Instant.now());
        return save(subreddit);
    }

    @Transactional
    public SubredditDto update(SubredditDto subredditDto) {
        Subreddit subreddit = subredditRepository.findById(subredditDto.getId())
            .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + subredditDto.getId()));

        subreddit.setName(subredditDto.getName());
        subreddit.setDescription(subredditDto.getDescription());
        return save(subreddit);
    }

    @Transactional
    private SubredditDto save(Subreddit subreddit) {
        Subreddit savedSubreddit = subredditRepository.save(subreddit);
        return subredditMapper.mapSubredditToDto(savedSubreddit);
    }

    @Transactional(readOnly = true)
    public Page<SubredditDto> getAll(Pageable pageable) {
        Page<Subreddit> page = subredditRepository.findAll(pageable);

        List<SubredditDto> content = page
            .getContent()
            .stream()
            .map(subredditMapper::mapSubredditToDto)
            .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, page.getTotalElements()); 
    }

    @Transactional
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
            .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
        
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
