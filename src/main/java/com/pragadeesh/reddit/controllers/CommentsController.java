package com.pragadeesh.reddit.controllers;


import com.pragadeesh.reddit.dto.CommentsDto;
import com.pragadeesh.reddit.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<String> createComments(@RequestBody CommentsDto commentsDto) {
        commentService.save(commentsDto);
        return new ResponseEntity<String>("Comments successfully created", HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return new ResponseEntity<List<CommentsDto>>(this.commentService.getAllCommentsForPost(postId), HttpStatus.OK);
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String username) {
        return new ResponseEntity<List<CommentsDto>>(this.commentService.getAllCommentForUser(username), HttpStatus.OK);
    }
}
