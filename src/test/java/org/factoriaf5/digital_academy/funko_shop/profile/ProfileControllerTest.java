package org.factoriaf5.digital_academy.funko_shop.profile;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.factoriaf5.digital_academy.funko_shop.profile.profile_exceptions.ProfileNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

public class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private User user;
    private ProfileDTO profileDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        profileDTO = new ProfileDTO();
        profileDTO.setUser(user.getId());
        profileDTO.setFirstName("John");
        profileDTO.setLastName("Doe");
    }

    @Test
    void testGetAllProfiles() {
        List<ProfileDTO> profiles = Collections.singletonList(profileDTO);
        when(profileService.getAllProfiles()).thenReturn(profiles);

        ResponseEntity<List<ProfileDTO>> response = profileController.getAllProfiles();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());  
        assertEquals(1, response.getBody().size());
        verify(profileService).getAllProfiles();
    }

    @Test
    void testGetProfileByUser() {
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication((UsernamePasswordAuthenticationToken) principal);

        when(profileService.getProfileByUser(user)).thenReturn(profileDTO);

        ResponseEntity<ProfileDTO> response = profileController.getProfileByUser(principal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value()); 
        assertEquals(profileDTO, response.getBody());
        verify(profileService).getProfileByUser(user);
    }

    @Test
    void testGetProfileByUser_NotFound() {
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication((Authentication) principal);

        when(profileService.getProfileByUser(user)).thenThrow(new ProfileNotFoundException("Profile not found"));

        ResponseEntity<ProfileDTO> response = profileController.getProfileByUser(principal);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());  
        verify(profileService).getProfileByUser(user);
    }

    @Test
    void testUpdateProfile() {
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication((Authentication) principal);

        when(profileService.updateProfile(user, profileDTO)).thenReturn(profileDTO);

        ResponseEntity<ProfileDTO> response = profileController.updateProfile(principal, profileDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());  
        assertEquals(profileDTO, response.getBody());
        verify(profileService).updateProfile(user, profileDTO);
    }

    @Test
    void testUpdateProfile_NotFound() {
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication((Authentication) principal);

        when(profileService.updateProfile(user, profileDTO)).thenThrow(new ProfileNotFoundException("Profile not found"));

        ResponseEntity<ProfileDTO> response = profileController.updateProfile(principal, profileDTO);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());  
        verify(profileService).updateProfile(user, profileDTO);
    }

    @Test
    void testDeleteProfile() {
        doNothing().when(profileService).deleteProfile(anyLong());

        ResponseEntity<Void> response = profileController.deleteProfile(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());  
        verify(profileService).deleteProfile(1L);
    }

    @Test
    void testDeleteProfile_NotFound() {
        doThrow(new ProfileNotFoundException("Profile not found")).when(profileService).deleteProfile(anyLong());

        ResponseEntity<Void> response = profileController.deleteProfile(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());  
        verify(profileService).deleteProfile(1L);
    }
}
