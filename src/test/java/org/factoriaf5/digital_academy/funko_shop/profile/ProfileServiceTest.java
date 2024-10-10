package org.factoriaf5.digital_academy.funko_shop.profile;

import org.factoriaf5.digital_academy.funko_shop.profile.profile_exceptions.ProfileNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.user.*;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProfile_Success() {
        //UserDTO userDTO = new UserDTO(1L, "test@example.com", "password", null, null, null, null, null, null);
        ProfileDTO profileDTO = new ProfileDTO(null, "John", "Doe", "1234567890", "123 Street", "City", "Region",
                "12345", "Country", true, true, 1L, null);
        User user = new User();
        user.setId(1L);
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        ProfileDTO result = profileService.createProfile(profileDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).findById(1L);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void createProfile_UserNotFound() {
        ProfileDTO profileDTO = new ProfileDTO(null, "John", "Doe", "1234567890", "123 Street", "City", "Region",
                "12345", "Country", true, true, 1L, null);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> profileService.createProfile(profileDTO));
        verify(userRepository).findById(1L);
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    void getAllProfiles_Success() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(1L);
        Profile profile1 = new Profile();
        profile1.setId(1L);
        profile1.setUser(user1);
        Profile profile2 = new Profile();
        profile2.setId(2L);
        profile2.setUser(user2);
        List<Profile> profiles = Arrays.asList(profile1, profile2);

        when(profileRepository.findAll()).thenReturn(profiles);

        List<ProfileDTO> result = profileService.getAllProfiles();

        assertEquals(2, result.size());
        verify(profileRepository).findAll();
    }

    @Test
    void getProfileById_Success() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setPhoneNumber("1234567890");
        profile.setStreet("123 Street");
        profile.setCity("City");
        profile.setRegion("Region");
        profile.setPostalCode("12345");
        profile.setCountry("Country");
        profile.setShipping(true);
        profile.setSubscribed(true);
        profile.setUser(new User());

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        ProfileDTO result = profileService.getProfileById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals("123 Street", result.getStreet());
        assertEquals("City", result.getCity());
        assertEquals("Region", result.getRegion());
        assertEquals("12345", result.getPostalCode());
        assertEquals("Country", result.getCountry());
        assertTrue(result.isShipping());
        assertTrue(result.isSubscribed());

        verify(profileRepository, times(1)).findById(1L);
    }

    @Test
    void getProfileById_NotFound() {
        when(profileRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> profileService.getProfileById(1L));
        verify(profileRepository).findById(1L);
    }

    @Test
    void updateProfile_Success() {
        Profile existingProfile = new Profile();
        existingProfile.setId(1L);
        existingProfile.setUser(new User());
        ProfileDTO updatedProfileDTO = new ProfileDTO(1L, "John", "Doe", "1234567890", "123 Street", "City", "Region",
                "12345", "Country", true, true, 1L, null);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(existingProfile);

        ProfileDTO result = profileService.updateProfile(1L, updatedProfileDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        verify(profileRepository).findById(1L);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void updateProfile_NotFound() {
        ProfileDTO updatedProfileDTO = new ProfileDTO(1L, "John", "Doe", "1234567890", "123 Street", "City", "Region",
                "12345", "Country", true, true, null, null);

        when(profileRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> profileService.updateProfile(1L, updatedProfileDTO));
        verify(profileRepository).findById(1L);
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    void deleteProfile_Success() {
        when(profileRepository.existsById(1L)).thenReturn(true);

        profileService.deleteProfile(1L);

        verify(profileRepository).existsById(1L);
        verify(profileRepository).deleteById(1L);
    }

    @Test
    void deleteProfile_NotFound() {
        when(profileRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProfileNotFoundException.class, () -> profileService.deleteProfile(1L));
        verify(profileRepository).existsById(1L);
        verify(profileRepository, never()).deleteById(1L);
    }

    @Test
    void mapToDTO_NullUser() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setPhoneNumber("1234567890");
        profile.setStreet("123 Street");
        profile.setCity("City");
        profile.setRegion("Region");
        profile.setPostalCode("12345");
        profile.setCountry("Country");
        profile.setShipping(true);
        profile.setSubscribed(true);
        profile.setUser(new User());

        ProfileDTO result = profileService.mapToDTO(profile);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals("123 Street", result.getStreet());
        assertEquals("City", result.getCity());
        assertEquals("Region", result.getRegion());
        assertEquals("12345", result.getPostalCode());
        assertEquals("Country", result.getCountry());
        assertTrue(result.isShipping());
        assertTrue(result.isSubscribed());
        assertNull(result.getUser());
    }

    @Test
    void mapToDTO_WithUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");

        Profile profile = new Profile();
        profile.setId(1L);
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setPhoneNumber("1234567890");
        profile.setStreet("123 Street");
        profile.setCity("City");
        profile.setRegion("Region");
        profile.setPostalCode("12345");
        profile.setCountry("Country");
        profile.setShipping(true);
        profile.setSubscribed(true);
        profile.setUser(user);

        ProfileDTO result = profileService.mapToDTO(profile);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals("123 Street", result.getStreet());
        assertEquals("City", result.getCity());
        assertEquals("Region", result.getRegion());
        assertEquals("12345", result.getPostalCode());
        assertEquals("Country", result.getCountry());
        assertTrue(result.isShipping());
        assertTrue(result.isSubscribed());
        assertNotNull(result.getUser());
        assertEquals(1L, result.getUser());
    }
}