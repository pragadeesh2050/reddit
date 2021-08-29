package com.pragadeesh.reddit.service;


import com.pragadeesh.reddit.dto.SubRedditDto;
import com.pragadeesh.reddit.exceptions.SpringRedditException;
import com.pragadeesh.reddit.model.Subreddit;
import com.pragadeesh.reddit.repository.SubRedditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubredditService {


    @Autowired
    private SubRedditRepository subRedditRepository;


    @Transactional
    public SubRedditDto save(SubRedditDto subRedditDto) {
        Subreddit subreddit = this.mapDtotoSubreddit(subRedditDto);
        Subreddit save = subRedditRepository.save(subreddit);
        subRedditDto.setId(save.getId());
        return subRedditDto;
    }

    private Subreddit mapDtotoSubreddit(SubRedditDto subRedditDto) {
        return Subreddit.builder().name(subRedditDto.getName())
                .description(subRedditDto.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SubRedditDto> getAllSubReddit() {
        return subRedditRepository.findAll()
                .stream()
                .map(this::mapSubredditDto)
                .collect(Collectors.toList());
    }

    public SubRedditDto getSubredditById(Long subredditId) {
        Subreddit subreddit = subRedditRepository.findById(subredditId)
                .orElseThrow(() -> new SpringRedditException("No Subreddit found for the ID: " + subredditId));
        return this.mapSubredditDto(subreddit);
    }

    private SubRedditDto mapSubredditDto(Subreddit subreddit) {
        return SubRedditDto.builder().numberOfPosts(subreddit.getPosts().size())
                .id(subreddit.getId())
                .name(subreddit.getName())
                .description(subreddit.getDescription())
                .build();

    }
}
