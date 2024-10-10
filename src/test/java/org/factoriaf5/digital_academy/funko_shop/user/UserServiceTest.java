package org.factoriaf5.digital_academy.funko_shop.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    private User user;
    private ChangePasswordRequest changePasswordRequest;
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock user
        user = new User();
        user.setPassword("encodedOldPassword");

        // Setup changePasswordRequest
        changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("oldPassword");
        changePasswordRequest.setNewPassword("newPassword");
        changePasswordRequest.setConfirmationPassword("newPassword");

        // Setup principal
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        principal = (Principal) authentication;
    }

    @Test
    void changePassword_ShouldThrowExceptionIfCurrentPasswordIsWrong() {
        // Simulate password mismatch
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Assert exception is thrown
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                userService.changePassword(changePasswordRequest, principal)
        );

        assertEquals("Wrong password", exception.getMessage());
        verify(repository, never()).save(any(User.class)); // Should not save anything
    }

    @Test
    void changePassword_ShouldThrowExceptionIfNewPasswordsDoNotMatch() {
        // Simulate correct current password
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Simulate password confirmation mismatch
        changePasswordRequest.setConfirmationPassword("differentNewPassword");

        // Assert exception is thrown
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                userService.changePassword(changePasswordRequest, principal)
        );

        assertEquals("Password are not the same", exception.getMessage());
        verify(repository, never()).save(any(User.class)); // Should not save anything
    }

    @Test
    void changePassword_ShouldSaveUserWithNewPasswordIfAllConditionsMet() {
        // Simulate correct current password
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        // Simulate password encoding
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");

        // Call the method
        userService.changePassword(changePasswordRequest, principal);

        // Verify password was updated and saved
        assertEquals("encodedNewPassword", user.getPassword());
        verify(repository, times(1)).save(user);
    }
}