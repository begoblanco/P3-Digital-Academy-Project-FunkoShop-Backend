package org.factoriaf5.digital_academy.funko_shop.favorite;

import org.factoriaf5.digital_academy.funko_shop.product.Product;
import org.factoriaf5.digital_academy.funko_shop.product.ProductRepository;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private Favorite favorite;
    private Product product;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, null, null, null, null, null, null, null, null, null);
        product = new Product();
        product.setId(1L);
        product.setCategory(null);
        product.setOrderItems(new ArrayList<>());
        favorite = new Favorite();
        favorite.setId(1L);
        favorite.setUser(user);
        favorite.setProduct(product);
    }

    @Test
    void addProductToFavorite_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);
        when(favoriteRepository.findByUserId(1L)).thenReturn(Arrays.asList(favorite));

        List<FavoriteDTO> result = favoriteService.addProductToFavorite(1L, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getProduct().getId());
        verify(productRepository).findById(1L);
        verify(favoriteRepository).save(any(Favorite.class));
        verify(favoriteRepository).findByUserId(1L);
    }

    @Test
    void removeProductFromFavorite_Success() {
        when(favoriteRepository.findByUserId(1L)).thenReturn(Arrays.asList(favorite));

        favoriteService.removeProductFromFavorite(1L, 1L);

        verify(favoriteRepository).deleteById(1L);
    }

    @Test
    void removeProductFromFavorite_FavoriteNotFound() {
        when(favoriteRepository.findByUserId(1L)).thenReturn(Arrays.asList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            favoriteService.removeProductFromFavorite(1L, 1L);
        });

        assertEquals("Favorite not found for productId: 1", exception.getMessage());
        verify(favoriteRepository, never()).deleteById(anyLong());
    }

    @Test
    void getFavoriteByUserId_Success() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Favorite> page = new PageImpl<>(Arrays.asList(favorite));
        when(favoriteRepository.findByUserId(1L, pageable)).thenReturn(page);

        Page<FavoriteDTO> result = favoriteService.getFavoriteByUserId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getProduct().getId());
        verify(favoriteRepository).findByUserId(1L, pageable);
    }

    @Test
    void checkFavorite_Exists() {
        when(favoriteRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.of(favorite));

        Boolean result = favoriteService.checkFavorite(1L, 1L);

        assertTrue(result);
        verify(favoriteRepository).findByUserIdAndProductId(1L, 1L);
    }

    @Test
    void checkFavorite_NotExists() {
        when(favoriteRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

        Boolean result = favoriteService.checkFavorite(1L, 1L);

        assertFalse(result);
        verify(favoriteRepository).findByUserIdAndProductId(1L, 1L);
    }
}
