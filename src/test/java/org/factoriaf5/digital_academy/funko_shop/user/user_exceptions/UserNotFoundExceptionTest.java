package org.factoriaf5.digital_academy.funko_shop.user.user_exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserNotFoundExceptionTest {

    @Test
    public void testUserNotFoundExceptionWithMessage() {
        String message = "User not found";
        UserNotFoundException exception = new UserNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testUserNotFoundExceptionWithMessageAndCause() {
        String message = "User not found";
        Throwable cause = new RuntimeException("Cause of the error");
        UserNotFoundException exception = new UserNotFoundException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
