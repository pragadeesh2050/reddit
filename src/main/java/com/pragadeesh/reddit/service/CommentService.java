package com.pragadeesh.reddit.service;

import com.pragadeesh.reddit.dto.CommentsDto;
import com.pragadeesh.reddit.exceptions.PostNotFoundException;
import com.pragadeesh.reddit.exceptions.SpringRedditException;
import com.pragadeesh.reddit.model.Comment;
import com.pragadeesh.reddit.model.Post;
import com.pragadeesh.reddit.model.UserTable;
import com.pragadeesh.reddit.repository.CommentRepository;
import com.pragadeesh.reddit.repository.PostRepository;
import com.pragadeesh.reddit.repository.UserTableRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserTableRepository userTableRepository;

    @Transactional
    public void save(CommentsDto commentsDto) {
        Post post = this.postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post with Id" + commentsDto.getPostId() + "not found"));
        UserTable userTable = authService.getCurrentUser();
        Comment comment = this.mapToComment(commentsDto, post, userTable);
        this.commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with Id" + postId + "not found"));
        return this.commentRepository.findByPost(post).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentsDto> getAllCommentForUser(String userName) {
        UserTable user = this.userTableRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("USer with username " + userName + "not found"));

        return this.commentRepository.findByUser(user.getUsername()).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public boolean containsSwearWords(String comment){
        if(comment.contains("shit")){
            throw new SpringRedditException("Comments contain unacceptable language");
        }
        return false;
    }

    private CommentsDto mapToDto(Comment comment) {
        return CommentsDto.builder()
                .text(comment.getText())
                .createdDate(Instant.now())
                .postId(comment.getPost().getPostId())
                .userName(comment.getUser().getUsername())
                .id(comment.getId())
                .build();

    }

    private Comment mapToComment(CommentsDto commentsDto, Post post, UserTable user) {
        return Comment.builder()
                .text(commentsDto.getText())
                .post(post)
                .user(user)
                .createdDate(Instant.now()).build();
    }


}
