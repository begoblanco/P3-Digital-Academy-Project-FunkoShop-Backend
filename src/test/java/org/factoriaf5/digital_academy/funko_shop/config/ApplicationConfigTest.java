package org.factoriaf5.digital_academy.funko_shop.config;

import org.factoriaf5.digital_academy.funko_shop.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationConfigTest {

    @Mock
    private UserRepository userRepository;

    private ApplicationConfig applicationConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationConfig = new ApplicationConfig(userRepository);
    }

    @Test
    public void testUserDetailsService() {
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        assertThat(userDetailsService).isNotNull();
    }

    @Test
    public void testAuditorAware() {
        var auditorAware = applicationConfig.auditorAware();
        assertThat(auditorAware).isNotNull();
    }


    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();
        assertThat(passwordEncoder).isNotNull();
    }
}

