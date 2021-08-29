package com.pragadeesh.reddit.repository;

import com.pragadeesh.reddit.dto.CommentsDto;
import com.pragadeesh.reddit.model.Comment;
import com.pragadeesh.reddit.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findByUser(String userName);
}
