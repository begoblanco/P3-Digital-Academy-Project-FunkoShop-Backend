package org.factoriaf5.digital_academy.funko_shop.review;

import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItem;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItemDTO;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItemRepository;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.factoriaf5.digital_academy.funko_shop.user.UserDTO;
import org.factoriaf5.digital_academy.funko_shop.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addReview_ShouldReturnReviewDTO() {

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        User user = new User();
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setOrderItem(orderItem);
        review.setUser(user);

        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, new OrderItemDTO(1L, 1, null, null, null), new UserDTO(1L, null, null, null, null, null, null, null, null));
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

      
        ReviewDTO result = reviewService.addReview(reviewDTO);

       
        assertEquals(reviewDTO.getId(), result.getId());
        assertEquals(reviewDTO.getRating(), result.getRating());
    }

    @Test
    void getReviewById_ShouldReturnReviewDTO() {
        
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        User user = new User();
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setOrderItem(orderItem);
        review.setUser(user);

        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, new OrderItemDTO(1L, 1, null, null, null), new UserDTO(1L, null, null, null, null, null, null, null, null));
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

      
        ReviewDTO result = reviewService.getReviewById(1L);

        
        assertEquals(reviewDTO.getId(), result.getId());
        assertEquals(reviewDTO.getRating(), result.getRating());
    }

    @Test
    void getAllReviews_ShouldReturnListOfReviewDTOs() {
        
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        User user = new User();
        user.setId(1L);

        List<Review> reviews = Arrays.asList(
            new Review(1L, 5, orderItem, user),
            new Review(2L, 4, orderItem, user)
        );

        List<ReviewDTO> reviewDTOs = Arrays.asList(
            new ReviewDTO(1L, 5, new OrderItemDTO(1L, 1, null, null, null), new UserDTO(1L, null, null, null, null, null, null, null, null)),
            new ReviewDTO(2L, 4, new OrderItemDTO(1L, 1, null, null, null), new UserDTO(1L, null, null, null, null, null, null, null, null))
        );

        when(reviewRepository.findAll()).thenReturn(reviews);

        
        List<ReviewDTO> result = reviewService.getAllReviews();

       
        assertEquals(reviewDTOs.size(), result.size());
        assertEquals(reviewDTOs.get(0).getId(), result.get(0).getId());
    }

    @Test
    void updateReview_ShouldReturnUpdatedReviewDTO() {
       
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        User user = new User();
        user.setId(1L);

        Review existingReview = new Review();
        existingReview.setId(1L);
        existingReview.setRating(4);
        existingReview.setOrderItem(orderItem);
        existingReview.setUser(user);

        Review updatedReview = new Review();
        updatedReview.setId(1L);
        updatedReview.setRating(5);
        updatedReview.setOrderItem(orderItem);
        updatedReview.setUser(user);

        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, new OrderItemDTO(1L, 1, null, null, null), new UserDTO(1L, null, null, null, null, null, null, null, null));
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(existingReview));
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class))).thenReturn(updatedReview);

        
        ReviewDTO result = reviewService.updateReview(1L, reviewDTO);

        
        assertEquals(reviewDTO.getId(), result.getId());
        assertEquals(reviewDTO.getRating(), result.getRating());
    }

    @Test
    void deleteReview_ShouldCallDelete() {
       
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        User user = new User();
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setOrderItem(orderItem);
        review.setUser(user);

        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

       
        reviewService.deleteReview(1L);

    }
}
