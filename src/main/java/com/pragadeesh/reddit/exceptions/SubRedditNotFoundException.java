package com.pragadeesh.reddit.exceptions;

public class SubRedditNotFoundException extends RuntimeException {
    public SubRedditNotFoundException(String message) {
        super(message);
    }
}
