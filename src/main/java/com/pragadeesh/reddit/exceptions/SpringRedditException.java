package com.pragadeesh.reddit.exceptions;

public class SpringRedditException extends RuntimeException {
    public SpringRedditException(String message) {
        super(message);
    }

    public SpringRedditException(String message, Throwable cause) {
        super(message, cause);
    }
}
