package org.factoriaf5.digital_academy.funko_shop.review;

import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItem;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    void testReviewConstructorAndGetters() {
        Long reviewId = 1L;
        int rating = 5;

        OrderItem orderItem = new OrderItem();
        User user = new User();

        Review review = new Review(reviewId, rating, orderItem, user);

        assertEquals(reviewId, review.getId());
        assertEquals(rating, review.getRating());
        assertEquals(orderItem, review.getOrderItem());
        assertEquals(user, review.getUser());
    }

    @Test
    void testReviewSetters() {
        Review review = new Review();

        OrderItem orderItem = new OrderItem();
        User user = new User();

        review.setId(2L);
        review.setRating(4);
        review.setOrderItem(orderItem);
        review.setUser(user);

        assertEquals(2L, review.getId());
        assertEquals(4, review.getRating());
        assertEquals(orderItem, review.getOrderItem());
        assertEquals(user, review.getUser());
    }

    @Test
    void testReviewBuilder() {
        OrderItem orderItem = new OrderItem();
        User user = new User();

        Review review = Review.builder()
                              .id(3L)
                              .rating(3)
                              .orderItem(orderItem)
                              .user(user)
                              .build();

        assertEquals(3L, review.getId());
        assertEquals(3, review.getRating());
        assertEquals(orderItem, review.getOrderItem());
        assertEquals(user, review.getUser());
    }
}
