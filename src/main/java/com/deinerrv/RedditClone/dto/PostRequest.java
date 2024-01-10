package com.deinerrv.RedditClone.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    
    private Long postId;
    private Long subredditId;

    @NotBlank(message = "Post Name cannot be empty or Null")
    private String postName;
    
    private String url;
    private String description;
}
