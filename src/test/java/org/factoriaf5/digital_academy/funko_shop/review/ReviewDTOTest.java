package org.factoriaf5.digital_academy.funko_shop.review;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReviewDTOTest {

    @Test
    void testReviewDTOConstructorAndGetters() {
        Long id = 1L;
        int rating = 5;
        Long orderItem = 2L;

        ReviewDTO reviewDTO = new ReviewDTO(id, rating, orderItem);

        assertEquals(id, reviewDTO.getId());
        assertEquals(rating, reviewDTO.getRating());
        assertEquals(orderItem, reviewDTO.getOrderItem());
    }

    @Test
    void testReviewDTOSetters() {
        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setId(1L);
        reviewDTO.setRating(4);
        reviewDTO.setOrderItem(2L);

        
        assertEquals(1L, reviewDTO.getId());
        assertEquals(4, reviewDTO.getRating());
        assertEquals(2L, reviewDTO.getOrderItem());
    }
}
