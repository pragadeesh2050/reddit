package com.pragadeesh.reddit.repository;

import com.pragadeesh.reddit.model.Post;
import com.pragadeesh.reddit.model.UserTable;
import com.pragadeesh.reddit.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(UserTable user);
}
