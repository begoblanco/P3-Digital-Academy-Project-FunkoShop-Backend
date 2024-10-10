package org.factoriaf5.digital_academy.funko_shop.profile;

import org.factoriaf5.digital_academy.funko_shop.address.AddressDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileDTOTest {

    @Test
    void testAllArgsConstructor() {
        AddressDTO addressDTO = new AddressDTO(0, "123 Street", "City", "Region", "12345", "Country", null);
        ProfileDTO profileDTO = new ProfileDTO(
                1L,
                "John",
                "Doe",
                "1234567890",
                "123 Street",
                "City",
                "Region",
                "12345",
                "Country",
                true,
                true,
                1L,
                addressDTO);

        assertEquals(1L, profileDTO.getId());
        assertEquals("John", profileDTO.getFirstName());
        assertEquals("Doe", profileDTO.getLastName());
        assertEquals("1234567890", profileDTO.getPhoneNumber());
        assertEquals("123 Street", profileDTO.getStreet());
        assertEquals("City", profileDTO.getCity());
        assertEquals("Region", profileDTO.getRegion());
        assertEquals("12345", profileDTO.getPostalCode());
        assertEquals("Country", profileDTO.getCountry());
        assertTrue(profileDTO.isShipping());
        assertTrue(profileDTO.isSubscribed());
        assertEquals(1L, profileDTO.getUser());
        assertEquals(addressDTO, profileDTO.getAddress());
    }

    @Test
    void testNoArgsConstructor() {
        ProfileDTO profileDTO = new ProfileDTO();

        assertNull(profileDTO.getId());
        assertNull(profileDTO.getFirstName());
        assertNull(profileDTO.getLastName());
        assertNull(profileDTO.getPhoneNumber());
        assertNull(profileDTO.getStreet());
        assertNull(profileDTO.getCity());
        assertNull(profileDTO.getRegion());
        assertNull(profileDTO.getPostalCode());
        assertNull(profileDTO.getCountry());
        assertFalse(profileDTO.isShipping());
        assertFalse(profileDTO.isSubscribed());
        assertNull(profileDTO.getUser());
        assertNull(profileDTO.getAddress());
    }

    @Test
    void testSettersAndGetters() {
        ProfileDTO profileDTO = new ProfileDTO();
        AddressDTO addressDTO = new AddressDTO(0, "456 Avenue", "New City", "New Region", "67890", "New Country",
                profileDTO);

        profileDTO.setId(2L);
        profileDTO.setFirstName("Jane");
        profileDTO.setLastName("Smith");
        profileDTO.setPhoneNumber("0987654321");
        profileDTO.setStreet("456 Avenue");
        profileDTO.setCity("New City");
        profileDTO.setRegion("New Region");
        profileDTO.setPostalCode("67890");
        profileDTO.setCountry("New Country");
        profileDTO.setShipping(false);
        profileDTO.setSubscribed(false);
        profileDTO.setUser(2L);
        profileDTO.setAddress(addressDTO);

        assertEquals(2L, profileDTO.getId());
        assertEquals("Jane", profileDTO.getFirstName());
        assertEquals("Smith", profileDTO.getLastName());
        assertEquals("0987654321", profileDTO.getPhoneNumber());
        assertEquals("456 Avenue", profileDTO.getStreet());
        assertEquals("New City", profileDTO.getCity());
        assertEquals("New Region", profileDTO.getRegion());
        assertEquals("67890", profileDTO.getPostalCode());
        assertEquals("New Country", profileDTO.getCountry());
        assertFalse(profileDTO.isShipping());
        assertFalse(profileDTO.isSubscribed());
        assertEquals(2L, profileDTO.getUser());
        assertEquals(addressDTO, profileDTO.getAddress());
    }
}
