package org.factoriaf5.digital_academy.funko_shop.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addReview_ShouldReturnCreatedReview() {
        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, null, null);
        when(reviewService.addReview(any(ReviewDTO.class))).thenReturn(reviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.addReview(reviewDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(reviewDTO, response.getBody());
    }

    @Test
    void getReviewById_ShouldReturnReview() {
        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, null, null);
        when(reviewService.getReviewById(anyLong())).thenReturn(reviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.getReviewById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTO, response.getBody());
    }

    @Test
    void getAllReviews_ShouldReturnReviews() {
        List<ReviewDTO> reviewDTOs = Arrays.asList(
                new ReviewDTO(1L, 5, null, null),
                new ReviewDTO(2L, 4, null, null)
        );
        when(reviewService.getAllReviews()).thenReturn(reviewDTOs);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getAllReviews();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTOs, response.getBody());
    }

    @Test
    void updateReview_ShouldReturnUpdatedReview() {
        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, null, null);
        when(reviewService.updateReview(anyLong(), any(ReviewDTO.class))).thenReturn(reviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.updateReview(1L, reviewDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTO, response.getBody());
    }

    @Test
    void deleteReview_ShouldReturnNoContent() {

        ResponseEntity<Void> response = reviewController.deleteReview(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
