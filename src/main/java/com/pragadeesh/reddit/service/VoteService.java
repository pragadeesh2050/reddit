package com.pragadeesh.reddit.service;


import com.pragadeesh.reddit.dto.VoteDto;
import com.pragadeesh.reddit.exceptions.PostNotFoundException;
import com.pragadeesh.reddit.exceptions.SpringRedditException;
import com.pragadeesh.reddit.model.Post;
import com.pragadeesh.reddit.model.Vote;
import com.pragadeesh.reddit.model.VoteType;
import com.pragadeesh.reddit.repository.PostRepository;
import com.pragadeesh.reddit.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AuthService authService;

    public void performVoting(VoteDto voteDto) {
        Post post = this.postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post with Id" + voteDto.getPostId() + "not found"));

        Optional<Vote> voteUser = this.voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (voteUser.isPresent() && voteUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + "this post");
        }

        if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        }
        if (VoteType.DOWNVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
