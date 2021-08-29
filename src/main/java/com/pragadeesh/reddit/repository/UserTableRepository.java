package com.pragadeesh.reddit.repository;

import com.pragadeesh.reddit.model.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTableRepository extends JpaRepository<UserTable, Long> {

    Optional<UserTable> findByUsername(String username);
}
