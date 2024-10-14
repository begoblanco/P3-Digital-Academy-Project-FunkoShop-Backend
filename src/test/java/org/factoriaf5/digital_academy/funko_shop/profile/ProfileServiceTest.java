package org.factoriaf5.digital_academy.funko_shop.profile;

import org.factoriaf5.digital_academy.funko_shop.profile.profile_exceptions.ProfileNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.factoriaf5.digital_academy.funko_shop.user.UserRepository;
import org.factoriaf5.digital_academy.funko_shop.user.user_exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileService profileService;

    private User user;
    private Profile profile;
    private ProfileDTO profileDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        profile = new Profile();
        profile.setId(1L);
        profile.setUser(user);
        profile.setFirstName("Mauricio");
        profile.setLastName("Colmenero");
        profile.setPhoneNumber("1234567890");
        profile.setStreet("123 Street");
        profile.setCity("City");
        profile.setRegion("Region");
        profile.setPostalCode("12345");
        profile.setCountry("Country");
        profile.setShipping(true);
        profile.setSubscribed(true);

        profileDTO = new ProfileDTO(1L, "Mauricio", "Colmenero", "1234567890", "123 Street", "City", "Region", "12345",
                "Country", true, true, 1L, null);
    }

    @Test
    void createProfile_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        profileService.createProfile(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void createProfile_UserNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> profileService.createProfile(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    void getAllProfiles_ReturnsListOfProfiles() {
        when(profileRepository.findAll()).thenReturn(Arrays.asList(profile, profile));

        List<ProfileDTO> result = profileService.getAllProfiles();

        assertEquals(2, result.size());
        verify(profileRepository, times(1)).findAll();
    }

    @Test
    void getProfileByUser_Success() {
        when(profileRepository.findByUser(any(User.class))).thenReturn(Optional.of(profile));

        ProfileDTO result = profileService.getProfileByUser(user);

        assertNotNull(result);
        assertEquals("Mauricio", result.getFirstName());
        verify(profileRepository, times(1)).findByUser(user);
    }

    @Test
    void getProfileByUser_ProfileNotFound_ThrowsException() {
        when(profileRepository.findByUser(any(User.class))).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> profileService.getProfileByUser(user));
        verify(profileRepository, times(1)).findByUser(user);
    }

    @Test
    void updateProfile_Success() {
        when(profileRepository.findByUser(any(User.class))).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        ProfileDTO result = profileService.updateProfile(user, profileDTO);

        assertNotNull(result);
        assertEquals("Mauricio", result.getFirstName());
        verify(profileRepository, times(1)).findByUser(user);
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void updateProfile_ProfileNotFound_ThrowsException() {
        when(profileRepository.findByUser(any(User.class))).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> profileService.updateProfile(user, profileDTO));
        verify(profileRepository, times(1)).findByUser(user);
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    void deleteProfile_Success() {
        when(profileRepository.existsById(1L)).thenReturn(true);

        profileService.deleteProfile(1L);

        verify(profileRepository, times(1)).existsById(1L);
        verify(profileRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProfile_NotFound_ThrowsException() {
        when(profileRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProfileNotFoundException.class, () -> profileService.deleteProfile(1L));
        verify(profileRepository, times(1)).existsById(1L);
        verify(profileRepository, never()).deleteById(1L);
    }

    @Test
    void mapToDTO_MapsProfileToDTO() {
        ProfileDTO result = profileService.mapToDTO(profile);

        assertNotNull(result);
        assertEquals("Mauricio", result.getFirstName());
        assertEquals("Colmenero", result.getLastName());
        assertEquals("1234567890", result.getPhoneNumber());
        verify(profileRepository, never()).save(any(Profile.class));
    }
}
