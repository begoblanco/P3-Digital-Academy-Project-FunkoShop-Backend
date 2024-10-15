package org.factoriaf5.digital_academy.funko_shop.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @SuppressWarnings("unused")
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    public void testRegister_Success() {
        RegisterRequest registerRequest = new RegisterRequest("user@example.com", "password");
        AuthenticationResponse authResponse = new AuthenticationResponse(1L, "USER", "accessToken", "refreshToken");

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        ResponseEntity<?> response = authenticationController.register(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    public void testRegister_EmailAlreadyInUse() {
        RegisterRequest registerRequest = new RegisterRequest("user@example.com", "password");

        when(authenticationService.register(any(RegisterRequest.class)))
                .thenThrow(new IllegalArgumentException("Email already in use"));

        ResponseEntity<?> response = authenticationController.register(registerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already in use", response.getBody());
    }

    @Test
    public void testAuthenticate_Success() {
        AuthenticationRequest authRequest = new AuthenticationRequest("user@example.com", "password");
        AuthenticationResponse authResponse = new AuthenticationResponse(1L, "USER", "accessToken", "refreshToken");

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.authenticate(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    public void testRefreshToken() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        doNothing().when(authenticationService).refreshToken(request, response);

        authenticationController.refreshToken(request, response);

        verify(authenticationService, times(1)).refreshToken(request, response);
    }
}
