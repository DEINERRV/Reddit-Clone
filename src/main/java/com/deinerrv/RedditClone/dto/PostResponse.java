package com.deinerrv.RedditClone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    
    private Long id;
    private String postName;
    private String url;
    private String description;
    private String userName;
    private String subredditName;
    private String subredditId;
    private Integer voteCount;
    private boolean upVote;
    private boolean downVote;
    private Integer commentCount;
    private String duration;
}
