package org.factoriaf5.digital_academy.funko_shop.review;

import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItem;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItemRepository;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public void addReview(ReviewDTO reviewDTO, User user) {

        OrderItem orderItem = orderItemRepository.findById(reviewDTO.getOrderItem())
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));

        Review isReview = reviewRepository.findByOrderItemAndUser(orderItem, user);

        if (isReview == null) {
            Review review = new Review();
            review.setRating(reviewDTO.getRating());
            review.setOrderItem(orderItem);
            review.setUser(user);

            reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("Review already exists");
        }
    }

    public ReviewDTO updateReview(ReviewDTO reviewDTO, User user) {

        OrderItem orderItem = orderItemRepository.findById(reviewDTO.getOrderItem()).get();
        Review review = mapToEntity(reviewDTO, orderItem, user);
        Review updatedReview = reviewRepository.save(review);
        return mapToDTO(updatedReview);
    }

    public ReviewDTO getReviewByOrderItemIdAndUser(Long orderItemId, User user) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));

        Review review = reviewRepository.findByOrderItemAndUser(orderItem, user);

        if (review == null) {
            return null;
        }

        return mapToDTO(review);
    }

    private ReviewDTO mapToDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getRating(),
                review.getOrderItem().getId());
    }

    private Review mapToEntity(ReviewDTO reviewDTO, OrderItem orderItem, User user) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setRating(reviewDTO.getRating());
        review.setOrderItem(orderItem);
        review.setUser(user);
        return review;
    }

}
