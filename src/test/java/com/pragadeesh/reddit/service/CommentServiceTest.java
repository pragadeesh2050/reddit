package com.pragadeesh.reddit.service;

import com.pragadeesh.reddit.exceptions.SpringRedditException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CommentServiceTest {

    @Test
    void getAllCommentForUser() {
    }

    @Test
    @DisplayName("Test should pass when the comment is good")
    void shouldnotContainsSwearWordsInsideComments() {
        CommentService commentService = new CommentService(null, null, null, null);
//        Assertions.assertFalse(commentService.containsSwearWords("I am a good comment"));
        assertThat(commentService.containsSwearWords("I am a good comment")).isFalse();
    }

    @Test
    @DisplayName("Test Should pass when the comment is bad")
    void shouldThrowExceptionWhenThereIsBadComment() {
        CommentService commentService = new CommentService(null, null, null, null);
//        SpringRedditException exception = Assertions.assertThrows(SpringRedditException.class, () -> {
//           commentService.containsSwearWords("I am saying something with word shit");
//        });
//
//        Assertions.assertTrue(exception.getMessage().contains("Comments contain unacceptable language"));

        assertThatThrownBy(() -> {
            commentService.containsSwearWords("I am saying something with word shit");
        }).isInstanceOf(SpringRedditException.class).hasMessage("Comments contain unacceptable language");
    }
}