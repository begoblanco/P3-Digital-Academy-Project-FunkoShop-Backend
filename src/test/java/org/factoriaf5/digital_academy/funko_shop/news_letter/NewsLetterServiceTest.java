package org.factoriaf5.digital_academy.funko_shop.news_letter;

import org.factoriaf5.digital_academy.funko_shop.email.EmailService;
import org.factoriaf5.digital_academy.funko_shop.product.product_exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsLetterServiceTest {

    @Mock
    private NewsLetterRepository newsLetterRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NewsLetterService newsLetterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNewsLetter_Success() {
        NewsLetterDTO newsLetterDTO = new NewsLetterDTO(null, null, "test@example.com", null);
        NewsLetter savedNewsLetter = new NewsLetter();
        savedNewsLetter.setId(1L);
        savedNewsLetter.setEmail("test@example.com");
        savedNewsLetter.setCode("ABC12345");

        when(newsLetterRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(newsLetterRepository.save(any(NewsLetter.class))).thenReturn(savedNewsLetter);

        NewsLetterDTO result = newsLetterService.createNewsLetter(newsLetterDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertNotNull(result.getCode());

        verify(newsLetterRepository, times(1)).existsByEmail("test@example.com");
        verify(newsLetterRepository, times(1)).save(any(NewsLetter.class));
        verify(emailService, times(1)).sendSubscriptionEmail(eq("test@example.com"), anyString());
    }

    @Test
    void testCreateNewsLetter_EmailAlreadyExists() {
        NewsLetterDTO newsLetterDTO = new NewsLetterDTO(null, null, "test@example.com", null);

        when(newsLetterRepository.existsByEmail("test@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            newsLetterService.createNewsLetter(newsLetterDTO);
        });

        assertEquals("Email already in use", exception.getMessage());

        verify(newsLetterRepository, times(1)).existsByEmail("test@example.com");
        verify(newsLetterRepository, never()).save(any(NewsLetter.class));
        verify(emailService, never()).sendSubscriptionEmail(anyString(), anyString());
    }

    @Test
    void testDeleteProduct_Success() {
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setId(1L);
        newsLetter.setCode("ABC12345");

        when(newsLetterRepository.findByCode("ABC12345")).thenReturn(newsLetter);

        newsLetterService.deleteProduct("ABC12345");

        verify(newsLetterRepository, times(1)).findByCode("ABC12345");
        verify(newsLetterRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(newsLetterRepository.findByCode("ABC12345")).thenReturn(null);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            newsLetterService.deleteProduct("ABC12345");
        });

        assertEquals("Product not found with code: ABC12345", exception.getMessage());

        verify(newsLetterRepository, times(1)).findByCode("ABC12345");
        verify(newsLetterRepository, never()).deleteById(anyLong());
    }
}
