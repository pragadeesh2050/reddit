package com.pragadeesh.reddit.service;

import com.pragadeesh.reddit.dto.PostResponse;
import com.pragadeesh.reddit.mapper.PostMapper;
import com.pragadeesh.reddit.model.Comment;
import com.pragadeesh.reddit.model.Post;
import com.pragadeesh.reddit.model.Subreddit;
import com.pragadeesh.reddit.model.UserTable;
import com.pragadeesh.reddit.repository.*;
import io.swagger.models.auth.In;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.parameters.P;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostServiceTest {

    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private AuthService authService = Mockito.mock(AuthService.class);
    private SubRedditRepository subRedditRepository = Mockito.mock(SubRedditRepository.class);
    private UserTableRepository userTableRepository = Mockito.mock(UserTableRepository.class);
    private VoteRepository voteRepository = Mockito.mock(VoteRepository.class);
    private PostMapper postMapper = Mockito.mock(PostMapper.class);


    @Test
    @DisplayName("To test if the Post Service returns Post by Id")
    void getPostByPostId() {
        PostService postService = new PostService(authService,subRedditRepository,postRepository,
                userTableRepository,commentRepository,voteRepository,postMapper);

        UserTable user = new UserTable(1L,"","","", Instant.now(),true);
        Subreddit subreddit = new Subreddit(4L,"","",new ArrayList<>(),Instant.now(),user);

        Post post = new Post(23L,"First Post",
                "http://localhost:8040","Test", 0 ,
                user, Instant.now(),subreddit);


        PostResponse expectedResponse = new PostResponse(23L,"First Post",
                "http://localhost:8040","Test","Test user","Test Subreddit",
                0, 0, "1 Hour ago", false,false);

        List<Comment> comments = new ArrayList<Comment>();

        Mockito.when(postRepository.findById(23L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findByPost(post)).thenReturn(comments);
        Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedResponse);

        PostResponse actualResponse  = postService.getPost(23L);

        Assertions.assertThat(actualResponse.getId()).isEqualTo(expectedResponse.getId());
        Assertions.assertThat(actualResponse.getPostName()).isEqualTo(expectedResponse.getPostName());
    }
}