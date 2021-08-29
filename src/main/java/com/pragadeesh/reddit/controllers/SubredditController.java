package com.pragadeesh.reddit.controllers;


import com.pragadeesh.reddit.dto.SubRedditDto;
import com.pragadeesh.reddit.service.SubredditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@Slf4j
public class SubredditController {

    @Autowired
    private SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubRedditDto> createSubReddit(@RequestBody SubRedditDto subRedditDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subRedditDto));
    }

    @GetMapping()
    public ResponseEntity<List<SubRedditDto>> getAllSubReddit(){
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAllSubReddit());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubRedditDto> getSubReddit(@PathVariable("id") Long subredditId){
       return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubredditById(subredditId));
    }
}
