package org.factoriaf5.digital_academy.funko_shop.product;

import org.factoriaf5.digital_academy.funko_shop.category.Category;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Funko Pop");

        product = Product.builder()
                .name("Test Product")
                .description("Test Description")
                .price(99.99f)
                .stock(100)
                .imageHash("imageHash1")
                .imageHash2("imageHash2")
                .category(category)
                .discount(10)
                .build();
    }

    @Test
    void testProductCreation() {
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(99.99f, product.getPrice());
        assertEquals(100, product.getStock());
        assertEquals("imageHash1", product.getImageHash());
        assertEquals("imageHash2", product.getImageHash2());
        assertNotNull(product.getCategory());
        assertEquals(10, product.getDiscount());

        assertNull(product.getCreatedAt());
    }

    @Test
    void testPrePersistOnCreate() {
        product.onCreate();

        assertNotNull(product.getCreatedAt());
        assertTrue(product.getCreatedAt().before(new Date()));
    }

    @Test
    void testOrderItemsRelation() {
        OrderItem orderItem1 = new OrderItem();
        OrderItem orderItem2 = new OrderItem();

        product.setOrderItems(List.of(orderItem1, orderItem2));

        assertNotNull(product.getOrderItems());
        assertEquals(2, product.getOrderItems().size());
    }

    @Test
    void testCategoryRelation() {
        assertNotNull(product.getCategory());
        assertEquals("Funko Pop", product.getCategory().getName());
    }
}
