package com.deinerrv.RedditClone.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.deinerrv.RedditClone.dto.SubredditDto;
import com.deinerrv.RedditClone.entity.Post;
import com.deinerrv.RedditClone.entity.Subreddit;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
    
    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);


    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);

    default Integer mapPosts(List<Post> posts) {
        return posts != null ? posts.size() : 0;
    }
}
