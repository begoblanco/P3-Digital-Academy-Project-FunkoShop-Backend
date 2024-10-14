package org.factoriaf5.digital_academy.funko_shop.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.factoriaf5.digital_academy.funko_shop.token.Token;
import org.factoriaf5.digital_academy.funko_shop.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.mockito.Mockito.*;

class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LogoutService logoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogout_WithValidToken() {
        String tokenValue = "Bearer valid-jwt-token";
        Token token = new Token();
        token.setToken("valid-jwt-token");

        when(request.getHeader("Authorization")).thenReturn(tokenValue);
        when(tokenRepository.findByToken("valid-jwt-token")).thenReturn(Optional.of(token));

        logoutService.logout(request, response, authentication);

        verify(tokenRepository).save(token);
        verify(tokenRepository).findByToken("valid-jwt-token");
        verify(tokenRepository).save(token);

        assert token.isExpired();
        assert token.isRevoked();

        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    void testLogout_WithMissingAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verify(tokenRepository, never()).findByToken(anyString());
        verify(tokenRepository, never()).save(any());
    }

    @Test
    void testLogout_WithInvalidToken() {
        String tokenValue = "Bearer invalid-jwt-token";

        when(request.getHeader("Authorization")).thenReturn(tokenValue);
        when(tokenRepository.findByToken("invalid-jwt-token")).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);

        verify(tokenRepository).findByToken("invalid-jwt-token");
        verify(tokenRepository, never()).save(any());

        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }
}
