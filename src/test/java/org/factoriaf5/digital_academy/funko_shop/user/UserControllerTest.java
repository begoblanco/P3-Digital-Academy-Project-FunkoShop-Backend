package org.factoriaf5.digital_academy.funko_shop.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService service;

    @Mock
    private java.security.Principal connectedUser;

    @InjectMocks
    private UserController userController;

    @Test
    public void testSuccessfulPasswordChange() {
        ChangePasswordRequest request = new ChangePasswordRequest();

        ResponseEntity<?> response = userController.changePassword(request, connectedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service).changePassword(request, connectedUser);
    }

    @Test
    public void testExceptionThrownByService() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        doThrow(new RuntimeException()).when(service).changePassword(request, connectedUser);

        assertThrows(RuntimeException.class, () -> userController.changePassword(request, connectedUser));
    }
}
