package com.pragadeesh.reddit.dto;


import com.pragadeesh.reddit.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {

    private Long postId;
    private VoteType voteType;
}
