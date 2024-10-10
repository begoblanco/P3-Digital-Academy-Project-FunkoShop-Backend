package org.factoriaf5.digital_academy.funko_shop.product;

import org.factoriaf5.digital_academy.funko_shop.category.Category;
import org.factoriaf5.digital_academy.funko_shop.discount.Discount;
import org.factoriaf5.digital_academy.funko_shop.cart_item.CartItem;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductTest {

    private Product product;

    @Mock
    private Category mockCategory;

    @Mock
    private Discount mockDiscount;

    @Mock
    private OrderItem mockOrderItem1, mockOrderItem2;

    @Mock
    private CartItem mockCartItem1, mockCartItem2;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .imageHash("test-image-hash")
                .description("Test Description")
                .price(100.0f)
                .stock(10)
                .isAvailable(true)
                .build();
    }

    @Test
    void testProductCreation() {
        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("test-image-hash", product.getImageHash());
        assertEquals("Test Description", product.getDescription());
        assertEquals(100.0f, product.getPrice());
        assertEquals(10, product.getStock());
        assertTrue(product.isAvailable());
    }

    @Test
    void testGetDiscountedPriceWithNoDiscount() {
        assertEquals(100.0f, product.getDiscountedPrice());
    }

    @Test
    void testGetDiscountedPriceWithActiveDiscount() {
        when(mockDiscount.isActive()).thenReturn(true);
        when(mockDiscount.getPercentage()).thenReturn(20.0f);
        product.setDiscount(mockDiscount);

        assertEquals(80.0f, product.getDiscountedPrice());
    }

    @Test
    void testGetDiscountedPriceWithInactiveDiscount() {
        when(mockDiscount.isActive()).thenReturn(false);
        product.setDiscount(mockDiscount);

        assertEquals(100.0f, product.getDiscountedPrice());
        verify(mockDiscount, never()).getPercentage();
    }

    @Test
    void testCategoryAssociation() {
        when(mockCategory.getId()).thenReturn(1L);
        when(mockCategory.getName()).thenReturn("Test Category");

        product.setCategory(mockCategory);

        assertNotNull(product.getCategory());
        assertEquals(1L, product.getCategory().getId());
        assertEquals("Test Category", product.getCategory().getName());
    }

    @Test
    void testOrderItemsAssociation() {
        List<OrderItem> orderItems = Arrays.asList(mockOrderItem1, mockOrderItem2);
        product.setOrderItems(orderItems);

        assertNotNull(product.getOrderItems());
        assertEquals(2, product.getOrderItems().size());
    }

    @Test
    void testCartItemsAssociation() {
        List<CartItem> cartItems = Arrays.asList(mockCartItem1, mockCartItem2);
        product.setCartItems(cartItems);

        assertNotNull(product.getCartItems());
        assertEquals(2, product.getCartItems().size());
    }

    @Test
    void testBuilderWithAllFields() {
        Product fullProduct = Product.builder()
                .id(2L)
                .name("Full Product")
                .imageHash("full-image-hash")
                .description("Full Description")
                .price(200.0f)
                .stock(20)
                .isAvailable(false)
                .category(mockCategory)
                .discount(mockDiscount)
                .orderItems(Arrays.asList(mockOrderItem1, mockOrderItem2))
                .cartItems(Arrays.asList(mockCartItem1, mockCartItem2))
                .build();

        assertNotNull(fullProduct);
        assertEquals(2L, fullProduct.getId());
        assertEquals("Full Product", fullProduct.getName());
        assertEquals("full-image-hash", fullProduct.getImageHash());
        assertEquals("Full Description", fullProduct.getDescription());
        assertEquals(200.0f, fullProduct.getPrice());
        assertEquals(20, fullProduct.getStock());
        assertFalse(fullProduct.isAvailable());
        assertNotNull(fullProduct.getCategory());
        assertNotNull(fullProduct.getDiscount());
        assertNotNull(fullProduct.getOrderItems());
        assertEquals(2, fullProduct.getOrderItems().size());
        assertNotNull(fullProduct.getCartItems());
        assertEquals(2, fullProduct.getCartItems().size());
    }
}