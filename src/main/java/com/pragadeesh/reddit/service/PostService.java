package com.pragadeesh.reddit.service;


import com.pragadeesh.reddit.dto.PostRequest;
import com.pragadeesh.reddit.dto.PostResponse;
import com.pragadeesh.reddit.exceptions.PostNotFoundException;
import com.pragadeesh.reddit.exceptions.SpringRedditException;
import com.pragadeesh.reddit.exceptions.SubRedditNotFoundException;
import com.pragadeesh.reddit.mapper.PostMapper;
import com.pragadeesh.reddit.model.*;
import com.pragadeesh.reddit.repository.*;
import com.pragadeesh.reddit.utils.TimeAgo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class PostService {

    private final AuthService authService;
    private final SubRedditRepository subRedditRepository;
    private final PostRepository postRepository;
    private final UserTableRepository userTableRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final PostMapper postMapper;

    public List<PostResponse> getAllPosts() {
        List<Post> posts = this.postRepository.findAll();
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with " + postId + "Not Found"));
        return postMapper.mapToDto(post);
    }

    public Post save(PostRequest postRequest) {
        Subreddit subreddit = subRedditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubRedditNotFoundException(postRequest.getSubredditName()));

        return postRepository.save(postMapper.mapToPost(postRequest, subreddit, this.authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subRedditRepository.findById(subredditId)
                .orElseThrow(() -> new SubRedditNotFoundException(subredditId.toString()));
        List<Post> posts = this.postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }


    public List<PostResponse> getPostsByUsername(String userName) {
        UserTable user = this.userTableRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("USer with username " + userName + "not found"));
        List<Post> posts = this.postRepository.findByUser(user);
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }





}
