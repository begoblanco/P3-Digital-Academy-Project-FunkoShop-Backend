package org.factoriaf5.digital_academy.funko_shop.favorite;

import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItem;
import org.factoriaf5.digital_academy.funko_shop.product.Product;
import org.factoriaf5.digital_academy.funko_shop.product.ProductDTO;
import org.factoriaf5.digital_academy.funko_shop.product.ProductRepository;
import org.factoriaf5.digital_academy.funko_shop.review.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ProductRepository productRepository;

    private Long userId;
    private Long productId;
    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = 1L;
        productId = 2L;

        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
    }

    @Test
    public void testAddProductToFavorite() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(favoriteRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<FavoriteDTO> favorites = favoriteService.addProductToFavorite(userId, productId);

        assertNotNull(favorites);

        ArgumentCaptor<Favorite> favoriteCaptor = ArgumentCaptor.forClass(Favorite.class);
        verify(favoriteRepository).save(favoriteCaptor.capture());
        assertEquals(userId, favoriteCaptor.getValue().getUser().getId());
        assertEquals(productId, favoriteCaptor.getValue().getProduct().getId());

        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> favoriteService.addProductToFavorite(userId, productId));

        when(favoriteRepository.findByUserId(userId)).thenThrow(new RuntimeException("Test Exception"));
        assertThrows(RuntimeException.class, () -> favoriteService.addProductToFavorite(userId, productId));
    }

    @Test
    public void testRemoveProductFromFavorite() {
        Favorite favorite = new Favorite();
        favorite.setId(3L);
        favorite.setProduct(product);
        when(favoriteRepository.findByUserId(userId)).thenReturn(Collections.singletonList(favorite));

        favoriteService.removeProductFromFavorite(userId, productId);

        verify(favoriteRepository).deleteById(favorite.getId());
    }

    @Test
    public void testGetFavoriteByUserId() {
    }

    @Test
    public void testCheckFavorite() {
        when(favoriteRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.of(new Favorite()));

        Boolean isFavorite = favoriteService.checkFavorite(userId, productId);

        assertTrue(isFavorite);
        verify(favoriteRepository).findByUserIdAndProductId(userId, productId);
    }

    @Test
    void testConvertToProductDTO_WithoutOrderItemsAndReviews() {

        assertThrows(NullPointerException.class, () -> favoriteService.convertToProductDTO(null));
        assertThrows(NullPointerException.class, () -> favoriteService.convertToProductDTO(new Product()));
        assertThrows(NullPointerException.class, () -> favoriteService.convertToProductDTO(new Product(){{ setId(1L); }}));
        assertThrows(NullPointerException.class, () -> favoriteService.convertToProductDTO(new Product(){{ setId(1L); setName("Test"); }}));
        assertThrows(NullPointerException.class, () -> favoriteService.convertToProductDTO(new Product(){{ setId(1L); setName("Test"); setPrice(10.0f); }}));
        assertThrows(NullPointerException.class, () -> favoriteService.convertToProductDTO(new Product(){{ setId(1L); setName("Test"); setPrice(10.0f); setStock(10); }}));

        product.setOrderItems(Collections.emptyList());

        ProductDTO result = favoriteService.convertToProductDTO(product);

        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(Optional.empty(), result.getImageHash());
        assertEquals(Optional.empty(), result.getImageHash2());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(favoriteService.calculateDiscountedPrice(product.getPrice(), product.getDiscount()), result.getDiscountedPrice());
        assertEquals(product.getStock(), result.getStock());
        assertEquals(product.getCreatedAt(), result.getCreatedAt());
        assertEquals(favoriteService.convertToCategoryDTO(product.getCategory()), result.getCategory());
        assertEquals(product.getDiscount(), result.getDiscount());
        assertEquals(0, result.getTotalReviews());
        assertEquals(0.0, result.getAverageRating(), 0.0);
    }

    @Test
    void testConvertToProductDTO_WithOrderItemsAndReviews() {
        List<OrderItem> orderItems = Arrays.asList(new OrderItem(), new OrderItem());
        product.setOrderItems(orderItems);

        List<Review> reviews = Arrays.asList();

        ProductDTO result = favoriteService.convertToProductDTO(product);

        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(favoriteService.calculateDiscountedPrice(product.getPrice(), product.getDiscount()), result.getDiscountedPrice());
        assertEquals(product.getStock(), result.getStock());
        assertEquals(product.getCreatedAt(), result.getCreatedAt());
        assertEquals(favoriteService.convertToCategoryDTO(product.getCategory()), result.getCategory());
        assertEquals(product.getDiscount(), result.getDiscount());
        assertEquals(reviews.size(), result.getTotalReviews());
        assertEquals(reviews.stream().mapToInt(Review::getRating).average().orElse(0.0), result.getAverageRating(), 0.0);
    }
}
