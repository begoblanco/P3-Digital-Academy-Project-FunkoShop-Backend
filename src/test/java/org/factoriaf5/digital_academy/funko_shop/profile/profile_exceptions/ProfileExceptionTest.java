package org.factoriaf5.digital_academy.funko_shop.profile.profile_exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProfileExceptionTest {

    @Test
    public void testProfileExceptionWithMessage() {
        String errorMessage = "This is a test error message";
        ProfileException exception = assertThrows(ProfileException.class, () -> {
            throw new ProfileException(errorMessage);
        });
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testProfileExceptionWithMessageAndCause() {
        String errorMessage = "This is a test error message";
        Throwable cause = new Throwable("This is the cause");
        ProfileException exception = assertThrows(ProfileException.class, () -> {
            throw new ProfileException(errorMessage, cause);
        });
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
