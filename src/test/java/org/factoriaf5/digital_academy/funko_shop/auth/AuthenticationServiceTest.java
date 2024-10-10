package org.factoriaf5.digital_academy.funko_shop.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import org.factoriaf5.digital_academy.funko_shop.config.JwtService;
import org.factoriaf5.digital_academy.funko_shop.token.Token;
import org.factoriaf5.digital_academy.funko_shop.token.TokenRepository;
import org.factoriaf5.digital_academy.funko_shop.user.Role;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.factoriaf5.digital_academy.funko_shop.user.UserRepository;

public class AuthenticationServiceTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_EmailAlreadyInUse() {
        RegisterRequest request = new RegisterRequest("user@example.com", "password");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.register(request);
        });
        assertEquals("Email already in use", exception.getMessage());
    }

    @Test
    public void testAuthenticate_Success() {
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password");
        User user = User.builder()
                .id(1L)
                .email(request.getEmail())
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(java.util.Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals(user.getId(), response.getId());
        assertEquals(user.getRole().name(), response.getRole());
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    public void testRevokeAllUserTokens_NoTokens() {
        User user = User.builder().id(1L).build();

        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(Collections.emptyList());

        verify(tokenRepository, never()).saveAll(any());
    }

    @Test
    public void testRevokeAllUserTokens_WithTokens() {
        User user = User.builder().id(1L).build();
        Token token = Token.builder().user(user).expired(false).revoked(false).build();

        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(Collections.singletonList(token));

        assertFalse(token.isExpired());
        assertFalse(token.isRevoked());
    }

    @Test
    public void testRefreshToken_InvalidToken() throws IOException {
        String authHeader = "Bearer invalidToken";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authHeader);

        authenticationService.refreshToken(request, response);

        verify(tokenRepository, never()).save(any());
    }

}
