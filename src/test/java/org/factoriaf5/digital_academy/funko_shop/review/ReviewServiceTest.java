package org.factoriaf5.digital_academy.funko_shop.review;

import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItem;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItemRepository;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.factoriaf5.digital_academy.funko_shop.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
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
    void addReview_ShouldSaveReview() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        User user = new User();
        user.setId(1L);

        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, 1L);

        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        reviewService.addReview(reviewDTO, user);

        verify(reviewRepository).save(any(Review.class));

        Review reviewToSave = new Review();
        reviewToSave.setRating(reviewDTO.getRating());
        reviewToSave.setOrderItem(orderItem);
        reviewToSave.setUser(user);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());

        Review savedReview = reviewCaptor.getValue();
        assertEquals(reviewDTO.getRating(), savedReview.getRating());
        assertEquals(orderItem, savedReview.getOrderItem());
        assertEquals(user, savedReview.getUser());
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

        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, 1L);

        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(existingReview));
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class))).thenReturn(updatedReview);

        ReviewDTO result = reviewService.updateReview(reviewDTO, user);

        assertEquals(reviewDTO.getId(), result.getId());
        assertEquals(reviewDTO.getRating(), result.getRating());
        assertEquals(reviewDTO.getOrderItem(), result.getOrderItem());
    }

    @Test
    void addReview_ShouldThrowIllegalArgumentException_WhenOrderItemNotFound() {
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, 1L);
        User user = new User();
        user.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.addReview(reviewDTO, user);
        });
    }

    @Test
    void addReview_ShouldThrowIllegalArgumentException_WhenReviewAlreadyExists() {
        OrderItem orderItem = new OrderItem();
        when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));

        Review existingReview = new Review();
        when(reviewRepository.findByOrderItemAndUser(any(OrderItem.class), any(User.class)))
                .thenReturn(existingReview);

        ReviewDTO reviewDTO = new ReviewDTO(1L, 5, 1L);
        User user = new User();
        user.setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.addReview(reviewDTO, user);
        });
    }

    @Test
void updateReview_ShouldThrowIllegalArgumentException_WhenOrderItemNotFound() {
    when(orderItemRepository.findById(anyLong())).thenReturn(Optional.empty());

    ReviewDTO reviewDTO = new ReviewDTO(1L, 5, 1L);
    User user = new User();
    user.setId(1L);

    assertThrows(NoSuchElementException.class, () -> {
        reviewService.updateReview(reviewDTO, user);
    });
}

@Test
void getReviewByOrderItemIdAndUser_ShouldReturnNull_WhenReviewNotFound() {
    OrderItem orderItem = new OrderItem();
    when(orderItemRepository.findById(anyLong())).thenReturn(Optional.of(orderItem));
    when(reviewRepository.findByOrderItemAndUser(any(OrderItem.class), any(User.class)))
            .thenReturn(null);

    User user = new User();
    user.setId(1L);

    ReviewDTO result = reviewService.getReviewByOrderItemIdAndUser(1L, user);
    assertNull(result);
}

}
