package org.factoriaf5.digital_academy.funko_shop.address;

import org.factoriaf5.digital_academy.funko_shop.profile.ProfileDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressDTOTest {

    @Test
    void testAddressDTOConstructorAndGetters() {
        long id = 1L;
        String street = "123 Main St";
        String city = "Springfield";
        String region = "Midwest";
        String postalCode = "12345";
        String country = "USA";
        ProfileDTO profile = new ProfileDTO(); 

        AddressDTO addressDTO = new AddressDTO(id, street, city, region, postalCode, country, profile);

        assertEquals(id, addressDTO.getId());
        assertEquals(street, addressDTO.getStreet());
        assertEquals(city, addressDTO.getCity());
        assertEquals(region, addressDTO.getRegion());
        assertEquals(postalCode, addressDTO.getPostalCode());
        assertEquals(country, addressDTO.getCountry());
        assertEquals(profile, addressDTO.getProfile());
    }

    @Test
    void testAddressDTOSetters() {
        AddressDTO addressDTO = new AddressDTO();
        ProfileDTO profile = new ProfileDTO();

        addressDTO.setId(2L);
        addressDTO.setStreet("456 Elm St");
        addressDTO.setCity("Shelbyville");
        addressDTO.setRegion("South");
        addressDTO.setPostalCode("54321");
        addressDTO.setCountry("USA");
        addressDTO.setProfile(profile);

        assertEquals(2L, addressDTO.getId());
        assertEquals("456 Elm St", addressDTO.getStreet());
        assertEquals("Shelbyville", addressDTO.getCity());
        assertEquals("South", addressDTO.getRegion());
        assertEquals("54321", addressDTO.getPostalCode());
        assertEquals("USA", addressDTO.getCountry());
        assertEquals(profile, addressDTO.getProfile());
    }

    @Test
    void testEmptyAddressDTO() {
        AddressDTO addressDTO = new AddressDTO();

        assertEquals(0L, addressDTO.getId()); 
        assertNull(addressDTO.getStreet());
        assertNull(addressDTO.getCity());
        assertNull(addressDTO.getRegion());
        assertNull(addressDTO.getPostalCode());
        assertNull(addressDTO.getCountry());
        assertNull(addressDTO.getProfile());
    }
}
