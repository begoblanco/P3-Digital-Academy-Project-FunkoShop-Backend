package org.factoriaf5.digital_academy.funko_shop.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class UserTest {

    private User user;

    @Mock
    private Role role;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .email("test@example.com")
                .password("password123")
                .role(role)
                .build();
    }

    @Test
    public void testGetAuthorities() {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(role.getAuthorities()).thenReturn(authorities);
        assertEquals(authorities, user.getAuthorities());
        verify(role, times(1)).getAuthorities();
    }

    @Test
    public void testGetPassword() {
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testGetUsername() {
        assertEquals("test@example.com", user.getUsername());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(user.isEnabled());
    }
}