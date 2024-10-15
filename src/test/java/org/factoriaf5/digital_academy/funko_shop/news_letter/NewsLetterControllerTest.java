package org.factoriaf5.digital_academy.funko_shop.news_letter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;

public class NewsLetterControllerTest {

    @InjectMocks
    private NewsLetterController newsLetterController;

    @Mock
    private NewsLetterService newsLetterService;

    private NewsLetterDTO newsLetterDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        newsLetterDTO = new NewsLetterDTO(1L, "CODE123", "test1@example.com", new Date());
    }

    @Test
    public void testCreateNewsLetter_Success() {
        when(newsLetterService.createNewsLetter(any(NewsLetterDTO.class))).thenReturn(newsLetterDTO);

        ResponseEntity<NewsLetterDTO> response = newsLetterController.createnNewsLetter(newsLetterDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newsLetterDTO, response.getBody());
        verify(newsLetterService, times(1)).createNewsLetter(any(NewsLetterDTO.class));
    }

    @Test
    public void testDeleteProduct_Success() {
        String code = "CODE123";
        ResponseEntity<Void> response = newsLetterController.deleteProduct(code);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(newsLetterService, times(1)).deleteProduct(code);
    }

    @Test
    public void testUnsubscribe_Success() {
        String code = "CODE123";
        ResponseEntity<Void> response = newsLetterController.unsubscribe(code);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(newsLetterService, times(1)).deleteProduct(code);
    }
}
