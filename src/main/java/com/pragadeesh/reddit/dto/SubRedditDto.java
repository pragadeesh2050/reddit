package com.pragadeesh.reddit.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubRedditDto {

    private long id;

    private String name;

    private String description;

    public Integer numberOfPosts;
}
