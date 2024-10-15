package org.factoriaf5.digital_academy.funko_shop.review.review_exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReviewNotFoundExceptionTest {

    @Test
    void constructor_ShouldSetMessage() {
        String message = "Review not found";

        ReviewNotFoundException exception = new ReviewNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructor_ShouldSetMessageAndCause() {
        String message = "Review not found with cause";
        Throwable cause = new RuntimeException("This is the cause");

        ReviewNotFoundException exception = new ReviewNotFoundException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldThrowReviewNotFoundException() {
        assertThrows(ReviewNotFoundException.class, () -> {
            throw new ReviewNotFoundException("Expected exception");
        });
    }
}
