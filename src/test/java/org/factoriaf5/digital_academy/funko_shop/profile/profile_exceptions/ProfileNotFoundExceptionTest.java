package org.factoriaf5.digital_academy.funko_shop.profile.profile_exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProfileNotFoundExceptionTest {

    @Test
    public void testProfileNotFoundException() {
        assertThrows(ProfileNotFoundException.class, () -> {
            throw new ProfileNotFoundException("Profile not found");
        });
    }

    @Test
    public void testProfileNotFoundExceptionWithMessage() {
        ProfileNotFoundException exception = assertThrows(ProfileNotFoundException.class, () -> {
            throw new ProfileNotFoundException("Profile not found");
        });
        assertEquals("Profile not found", exception.getMessage());
    }

    @Test
    public void testProfileNotFoundExceptionWithMessageAndCause() {
        Throwable cause = new RuntimeException("Cause of the exception");
        ProfileNotFoundException exception = assertThrows(ProfileNotFoundException.class, () -> {
            throw new ProfileNotFoundException("Profile not found", cause);
        });
        assertEquals("Profile not found", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testProfileNotFoundExceptionWithNullMessage() {
        ProfileNotFoundException exception = assertThrows(ProfileNotFoundException.class, () -> {
            throw new ProfileNotFoundException(null);
        });
        assertNull(exception.getMessage());
    }

    @Test
    public void testProfileNotFoundExceptionWithNullMessageAndCause() {
        Throwable cause = new RuntimeException("Cause of the exception");
        ProfileNotFoundException exception = assertThrows(ProfileNotFoundException.class, () -> {
            throw new ProfileNotFoundException(null, cause);
        });
        assertNull(exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
