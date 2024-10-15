package org.factoriaf5.digital_academy.funko_shop.review;

import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private Principal mockPrincipal;

    @Mock
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockPrincipal.getName()).thenReturn("user@example.com");
        when(mockUser.getEmail()).thenReturn("user@example.com");
    }

    @Test
    void addReview_ShouldReturnCreatedStatus() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(mockUser, null);

        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, 1L);
        doNothing().when(reviewService).addReview(any(ReviewDTO.class), any(User.class));

        ResponseEntity<?> response = reviewController.addReview(authenticationToken, reviewDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(reviewService, times(1)).addReview(any(ReviewDTO.class), any(User.class));
    }

    @Test
    void updateReview_ShouldReturnUpdatedReview() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(mockUser, null);

        ReviewDTO reviewDTO = new ReviewDTO(1L, 4, 1L);
        when(reviewService.updateReview(any(ReviewDTO.class), any(User.class))).thenReturn(reviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.updateReview(authenticationToken, reviewDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTO, response.getBody());
    }

    @Test
    void getReviewByIdAndUser_ShouldReturnReview() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(mockUser, null);

        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, 1L);
        when(reviewService.getReviewByOrderItemIdAndUser(anyLong(), any(User.class))).thenReturn(reviewDTO);

        ResponseEntity<ReviewDTO> response = reviewController.getReviewByIdAndUser(authenticationToken, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviewDTO, response.getBody());
    }
}
