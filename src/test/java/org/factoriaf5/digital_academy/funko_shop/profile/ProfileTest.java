// package org.factoriaf5.digital_academy.funko_shop.profile;

// import org.factoriaf5.digital_academy.funko_shop.address.Address;
// import org.factoriaf5.digital_academy.funko_shop.user.User;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

// public class ProfileTest {

//     private Profile profile;
//     private User user;
//     private Address address;

//     @BeforeEach
//     public void setUp() {
//         user = new User();
//         user.setId(1L);
//         user.setEmail("testuser@email.com");

//         address = new Address();
//         address.setId(1L);
//         address.setStreet("123 Main St");
//         address.setCity("Test City");
//         address.setRegion("Test Region");
//         address.setPostalCode("12345");
//         address.setCountry("Test Country");

//         profile = Profile.builder()
//                 .id(1L)
//                 .firstName("John")
//                 .lastName("Doe")
//                 .phoneNumber("1234567890")
//                 .street("123 Main St")
//                 .city("Test City")
//                 .region("Test Region")
//                 .postalCode("12345")
//                 .country("Test Country")
//                 .isShipping(true)
//                 .isSuscribed(true)
//                 .user(user)
//                 .address(address)
//                 .build();
//     }

//     @Test
//     public void testProfileCreation() {
//         assertNotNull(profile);
//         assertEquals(1L, profile.getId());
//         assertEquals("John", profile.getFirstName());
//         assertEquals("Doe", profile.getLastName());
//         assertEquals("1234567890", profile.getPhoneNumber());
//         assertEquals("123 Main St", profile.getStreet());
//         assertEquals("Test City", profile.getCity());
//         assertEquals("Test Region", profile.getRegion());
//         assertEquals("12345", profile.getPostalCode());
//         assertEquals("Test Country", profile.getCountry());
//         assertTrue(profile.isShipping());
//         assertTrue(profile.isSuscribed());
//         assertEquals(user, profile.getUser());
//         assertEquals(address, profile.getAddress());
//     }

//     @Test
//     public void testProfileUpdate() {
//         profile.setFirstName("Jane");
//         profile.setLastName("Smith");
//         profile.setPhoneNumber("0987654321");
//         profile.setStreet("456 Another St");
//         profile.setCity("Another City");
//         profile.setRegion("Another Region");
//         profile.setPostalCode("54321");
//         profile.setCountry("Another Country");
//         profile.setShipping(false);
//         profile.setSuscribed(false);

//         assertEquals("Jane", profile.getFirstName());
//         assertEquals("Smith", profile.getLastName());
//         assertEquals("0987654321", profile.getPhoneNumber());
//         assertEquals("456 Another St", profile.getStreet());
//         assertEquals("Another City", profile.getCity());
//         assertEquals("Another Region", profile.getRegion());
//         assertEquals("54321", profile.getPostalCode());
//         assertEquals("Another Country", profile.getCountry());
//         assertFalse(profile.isShipping());
//         assertFalse(profile.isSuscribed());
//     }

//     @Test
//     public void testProfileUserAssociation() {
//         User newUser = new User();
//         newUser.setId(2L);
//         newUser.setEmail("newuser@email.com");

//         profile.setUser(newUser);

//         assertEquals(newUser, profile.getUser());
//     }

//     @Test
//     public void testProfileAddressAssociation() {
//         Address newAddress = new Address();
//         newAddress.setId(2L);
//         newAddress.setStreet("789 New St");
//         newAddress.setCity("New City");
//         newAddress.setRegion("New Region");
//         newAddress.setPostalCode("67890");
//         newAddress.setCountry("New Country");

//         profile.setAddress(newAddress);

//         assertEquals(newAddress, profile.getAddress());
//     }
// }
