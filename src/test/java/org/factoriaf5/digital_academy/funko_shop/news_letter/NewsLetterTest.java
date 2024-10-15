package org.factoriaf5.digital_academy.funko_shop.news_letter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NewsLetterTest {

    private NewsLetter newsLetter;

    @BeforeEach
    public void setUp() {
        newsLetter = NewsLetter.builder()
                .code("CODE123")
                .email("test@example.com")
                .build();
    }

    @Test
    public void testNewsLetterCreation() {
        assertNotNull(newsLetter);
        assertEquals("CODE123", newsLetter.getCode());
        assertEquals("test@example.com", newsLetter.getEmail());
        assertNull(newsLetter.getCreatedAt());
    }

    @Test
    public void testOnCreate() {
        newsLetter.onCreate();

        assertNotNull(newsLetter.getCreatedAt());
    }

    @Test
    public void testSettersAndGetters() {
        newsLetter.setEmail("new_email@example.com");
        newsLetter.setCode("NEW_CODE");
        
        assertEquals("new_email@example.com", newsLetter.getEmail());
        assertEquals("NEW_CODE", newsLetter.getCode());
    }
}
