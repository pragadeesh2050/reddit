package com.pragadeesh.reddit.repository;

import com.pragadeesh.reddit.model.Post;
import com.pragadeesh.reddit.model.UserTable;
import com.pragadeesh.reddit.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, UserTable currentUser);
}
