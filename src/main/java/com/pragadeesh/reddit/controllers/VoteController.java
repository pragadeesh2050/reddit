package com.pragadeesh.reddit.controllers;


import com.pragadeesh.reddit.dto.VoteDto;
import com.pragadeesh.reddit.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
public class VoteController {


    @Autowired
    private VoteService voteService;


    @PostMapping
    public ResponseEntity<Void> performVoting(@RequestBody VoteDto voteDto){
        this.voteService.performVoting(voteDto);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
