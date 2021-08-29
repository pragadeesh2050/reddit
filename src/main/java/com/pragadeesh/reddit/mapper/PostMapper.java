package com.pragadeesh.reddit.mapper;


import com.pragadeesh.reddit.dto.PostRequest;
import com.pragadeesh.reddit.dto.PostResponse;
import com.pragadeesh.reddit.model.*;
import com.pragadeesh.reddit.repository.CommentRepository;
import com.pragadeesh.reddit.repository.VoteRepository;
import com.pragadeesh.reddit.service.AuthService;
import com.pragadeesh.reddit.utils.TimeAgo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostMapper {


    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private VoteRepository voteRepository;

    public PostResponse mapToDto(Post post){
        log.info("------------------------------");
        log.info("####"+ post.getPostId());
        log.info("------------------------------");
        List<Comment> comments = this.commentRepository.findByPost(post);
        return PostResponse.builder()
                .postName(post.getPostName())
                .id(post.getPostId())
                .description(post.getDescription())
                .url(post.getUrl())
                .subredditName(post.getSubreddit().getName())
                .userName(post.getUser().getUsername())
                .commentCount(comments.size())
                .duration(TimeAgo.calculate(Date.from(Instant.now()),Date.from(post.getCreatedDate())))
                .upVote(checkVote(post, VoteType.UPVOTE))
                .downVote(checkVote(post, VoteType.DOWNVOTE))
                .build();
    }

    private boolean checkVote(Post post, VoteType voteType) {
        if(authService.isLoggedIn()){
            Optional<Vote> voteForPostByUser = this.voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, this.authService.getCurrentUser());
            return voteForPostByUser.filter(x -> x.getVoteType().equals(voteType)).isPresent();
        }
        return false;
    }

    public Post mapToPost(PostRequest postRequest, Subreddit subreddit, UserTable user){
        return Post.builder()
                .createdDate(java.time.Instant.now())
                .subreddit(subreddit)
                .user(user)
                .description(postRequest.getDescription())
                .postName(postRequest.getPostName())
                .voteCount(0)
                .url(postRequest.getUrl())
                .build();

    }
}
