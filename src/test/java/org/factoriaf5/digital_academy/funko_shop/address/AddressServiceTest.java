package org.factoriaf5.digital_academy.funko_shop.address;

import org.factoriaf5.digital_academy.funko_shop.address.address_exceptions.AddressNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address address;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        address = new Address(1L, "123 Main St", "Springfield", "SomeRegion", "12345", "Country", null);
        addressDTO = new AddressDTO(1L, "123 Main St", "Springfield", "SomeRegion", "12345", "Country", null);
    }

    @Test
    void getAllAddresses_ReturnsListOfAddressDTOs() {
        when(addressRepository.findAll()).thenReturn(Arrays.asList(address));

        List<AddressDTO> result = addressService.getAllAddresses();

        assertEquals(1, result.size());
        assertEquals(addressDTO.getStreet(), result.get(0).getStreet());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void getAddressById_WhenAddressExists_ReturnsAddressDTO() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

        AddressDTO result = addressService.getAddressById(1L);

        assertNotNull(result);
        assertEquals(addressDTO.getStreet(), result.getStreet());
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void getAddressById_WhenAddressDoesNotExist_ThrowsException() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> addressService.getAddressById(1L));
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void createAddress_SavesAndReturnsAddressDTO() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDTO result = addressService.createAddress(addressDTO);

        assertNotNull(result);
        assertEquals(addressDTO.getStreet(), result.getStreet());
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void updateAddress_WhenAddressExists_UpdatesAndReturnsAddressDTO() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDTO updatedDTO = new AddressDTO(1L, "456 Updated St", "NewCity", "NewRegion", "67890", "Country", null);
        AddressDTO result = addressService.updateAddress(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("456 Updated St", result.getStreet());
        assertEquals("NewCity", result.getCity());
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void updateAddress_WhenAddressDoesNotExist_ThrowsException() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        AddressDTO updatedDTO = new AddressDTO(1L, "456 Updated St", "NewCity", "NewRegion", "67890", "Country", null);

        assertThrows(AddressNotFoundException.class, () -> addressService.updateAddress(1L, updatedDTO));
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void deleteAddress_WhenAddressExists_DeletesAddress() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

        addressService.deleteAddress(1L);

        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).delete(address);
    }

    @Test
    void deleteAddress_WhenAddressDoesNotExist_ThrowsException() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> addressService.deleteAddress(1L));
        verify(addressRepository, times(1)).findById(1L);
    }
}

