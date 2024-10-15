package org.factoriaf5.digital_academy.funko_shop.address;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addressDTO = new AddressDTO(1L, "123 Main St", "Springfield", "SomeRegion", "12345", "Country", null);
    }

    @Test
    void getAllAddresses_ReturnsListOfAddresses() {
        List<AddressDTO> addresses = Arrays.asList(addressDTO);
        when(addressService.getAllAddresses()).thenReturn(addresses);

        List<AddressDTO> result = addressController.getAllAddresses();

        assertEquals(1, result.size());
        verify(addressService, times(1)).getAllAddresses();
    }

    @Test
    void getAddressById_WhenAddressExists_ReturnsAddress() {
        when(addressService.getAddressById(1L)).thenReturn(addressDTO);

        ResponseEntity<AddressDTO> response = addressController.getAddressById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addressDTO, response.getBody());
        verify(addressService, times(1)).getAddressById(1L);
    }

    @Test
    void getAddressById_WhenAddressDoesNotExist_ReturnsNotFound() {
        when(addressService.getAddressById(1L)).thenReturn(null);

        ResponseEntity<AddressDTO> response = addressController.getAddressById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(addressService, times(1)).getAddressById(1L);
    }

    @Test
    void createAddress_ReturnsCreatedAddress() {
        when(addressService.createAddress(addressDTO)).thenReturn(addressDTO);

        ResponseEntity<AddressDTO> response = addressController.createAddress(addressDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addressDTO, response.getBody());
        verify(addressService, times(1)).createAddress(addressDTO);
    }

    @Test
    void updateAddress_WhenAddressExists_ReturnsUpdatedAddress() {
        when(addressService.updateAddress(1L, addressDTO)).thenReturn(addressDTO);

        ResponseEntity<AddressDTO> response = addressController.updateAddress(1L, addressDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addressDTO, response.getBody());
        verify(addressService, times(1)).updateAddress(1L, addressDTO);
    }

    @Test
    void updateAddress_WhenAddressDoesNotExist_ReturnsNotFound() {
        when(addressService.updateAddress(1L, addressDTO)).thenReturn(null);

        ResponseEntity<AddressDTO> response = addressController.updateAddress(1L, addressDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(addressService, times(1)).updateAddress(1L, addressDTO);
    }

    @Test
    void deleteAddress_ReturnsNotFound() {
        ResponseEntity<Void> response = addressController.deleteAddress(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
