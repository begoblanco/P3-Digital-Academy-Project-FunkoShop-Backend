package org.factoriaf5.digital_academy.funko_shop.order_item;

import org.factoriaf5.digital_academy.funko_shop.order.Order;
import org.factoriaf5.digital_academy.funko_shop.product.Product;
import org.factoriaf5.digital_academy.funko_shop.review.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    private OrderItem orderItem;
    private Order order;
    private Product product;
    private Review review;

    @BeforeEach
    void setUp() {
        order = new Order();
        product = new Product();
        review = new Review();
    
        orderItem = OrderItem.builder()
                .id(1L)
                .quantity(2)
                .order(order)
                .product(product)
                .review(review)
                .build();
    
        review.setOrderItem(orderItem); 
    }
    

    @Test
    void testOrderItemConstructorAndGetters() {
        assertEquals(1L, orderItem.getId());
        assertEquals(2, orderItem.getQuantity());
        assertEquals(order, orderItem.getOrder());
        assertEquals(product, orderItem.getProduct());
        assertEquals(review, orderItem.getReview());
    }

    @Test
    void testOrderItemSetters() {
        Order newOrder = new Order();
        Product newProduct = new Product();
        Review newReview = new Review();
        
        orderItem.setQuantity(5);
        orderItem.setOrder(newOrder);
        orderItem.setProduct(newProduct);
        orderItem.setReview(newReview);

        assertEquals(5, orderItem.getQuantity());
        assertEquals(newOrder, orderItem.getOrder());
        assertEquals(newProduct, orderItem.getProduct());
        assertEquals(newReview, orderItem.getReview());
    }

    @Test
void testOrderItemRelationships() {
    assertEquals(order, orderItem.getOrder());
    assertEquals(product, orderItem.getProduct());
    assertEquals(review, orderItem.getReview());
    
    assertEquals(orderItem, review.getOrderItem()); 
}


    @Test
    void testOrderItemDefaultConstructor() {
        OrderItem emptyOrderItem = new OrderItem();
        
        assertNull(emptyOrderItem.getId());
        assertEquals(0, emptyOrderItem.getQuantity());
        assertNull(emptyOrderItem.getOrder());
        assertNull(emptyOrderItem.getProduct());
        assertNull(emptyOrderItem.getReview());
    }
}
