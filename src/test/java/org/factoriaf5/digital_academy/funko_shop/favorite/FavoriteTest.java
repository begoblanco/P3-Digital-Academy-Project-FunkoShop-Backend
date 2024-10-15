package org.factoriaf5.digital_academy.funko_shop.favorite;

import org.factoriaf5.digital_academy.funko_shop.product.Product;
import org.factoriaf5.digital_academy.funko_shop.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteTest {

    @Test
    void testFavoriteConstructorAndGetters() {
        Long id = 1L;
        Product product = new Product();
        User user = new User();

        Favorite favorite = new Favorite(id, product, user);

        assertEquals(id, favorite.getId());
        assertEquals(product, favorite.getProduct());
        assertEquals(user, favorite.getUser());
    }

    @Test
    void testFavoriteSetters() {
        Favorite favorite = new Favorite();
        Product product = new Product();
        User user = new User();

        favorite.setId(2L);
        favorite.setProduct(product);
        favorite.setUser(user);

        assertEquals(2L, favorite.getId());
        assertEquals(product, favorite.getProduct());
        assertEquals(user, favorite.getUser());
    }

    @Test
    void testFavoriteBuilder() {
        Product product = new Product();
        User user = new User();

        Favorite favorite = Favorite.builder()
                                    .id(3L)
                                    .product(product)
                                    .user(user)
                                    .build();

        assertEquals(3L, favorite.getId());
        assertEquals(product, favorite.getProduct());
        assertEquals(user, favorite.getUser());
    }

    @Test
    void testEmptyFavorite() {
        Favorite favorite = new Favorite();

        assertNull(favorite.getId());
        assertNull(favorite.getProduct());
        assertNull(favorite.getUser());
    }
}
