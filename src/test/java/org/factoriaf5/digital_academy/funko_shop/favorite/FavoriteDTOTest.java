package org.factoriaf5.digital_academy.funko_shop.favorite;

import org.factoriaf5.digital_academy.funko_shop.product.ProductDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteDTOTest {

    @Test
    void testFavoriteDTOConstructorAndGetters() {
        Long id = 1L;
        ProductDTO product = new ProductDTO(1L, "Funko Pop", java.util.Optional.empty(), java.util.Optional.empty(), null, 20.0f, 10, 0, null, null, 0, 0, id);
        Long userId = 100L;

        FavoriteDTO favoriteDTO = new FavoriteDTO(id, product, userId);

        assertEquals(id, favoriteDTO.getId());
        assertEquals(product, favoriteDTO.getProduct());
        assertEquals(userId, favoriteDTO.getUser());
    }

    @Test
    void testFavoriteDTOSetters() {
         
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        ProductDTO product = new ProductDTO(2L, "Funko Pop Batman", java.util.Optional.empty(), java.util.Optional.empty(), null, 25.0f, 5, 10, null, null, 0, 0, 0);
        Long userId = 101L;

        favoriteDTO.setId(2L);
        favoriteDTO.setProduct(product);
        favoriteDTO.setUser(userId);

        assertEquals(2L, favoriteDTO.getId());
        assertEquals(product, favoriteDTO.getProduct());
        assertEquals(userId, favoriteDTO.getUser());
    }

    @Test
    void testFavoriteDTOBuilder() {
        ProductDTO product = new ProductDTO(3L, "Funko Pop Superman", java.util.Optional.empty(), java.util.Optional.empty(), null, 30.0f, 15, 5, null, null, 0, 0, 0);
        Long userId = 102L;

        FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                                            .id(3L)
                                            .product(product)
                                            .user(userId)
                                            .build();

        assertEquals(3L, favoriteDTO.getId());
        assertEquals(product, favoriteDTO.getProduct());
        assertEquals(userId, favoriteDTO.getUser());
    }

    @Test
    void testEmptyFavoriteDTO() {
        FavoriteDTO favoriteDTO = new FavoriteDTO();

        assertNull(favoriteDTO.getId());
        assertNull(favoriteDTO.getProduct());
        assertNull(favoriteDTO.getUser());
    }
}
