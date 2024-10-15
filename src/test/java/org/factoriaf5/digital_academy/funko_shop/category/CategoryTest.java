package org.factoriaf5.digital_academy.funko_shop.category;

import org.factoriaf5.digital_academy.funko_shop.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();
        Product product1 = new Product();
        Product product2 = new Product();
        products.add(product1);
        products.add(product2);

        category = Category.builder()
                .id(1L)
                .name("Action Figures")
                .imageHash("abc123")
                .highlights(true)
                .products(products)
                .build();
    }

    @Test
    void testCategoryConstructorAndGetters() {
        Long id = 1L;
        String name = "Action Figures";
        String imageHash = "abc123";
        boolean highlights = true;

        Category category = new Category(id, name, imageHash, highlights, products);

        assertEquals(id, category.getId());
        assertEquals(name, category.getName());
        assertEquals(imageHash, category.getImageHash());
        assertTrue(category.isHighlights());
        assertEquals(products, category.getProducts());
    }

    @Test
    void testCategorySetters() {
        Category category = new Category();
        List<Product> newProducts = new ArrayList<>();

        category.setId(2L);
        category.setName("Collectibles");
        category.setImageHash("xyz789");
        category.setHighlights(false);
        category.setProducts(newProducts);

        assertEquals(2L, category.getId());
        assertEquals("Collectibles", category.getName());
        assertEquals("xyz789", category.getImageHash());
        assertFalse(category.isHighlights());
        assertEquals(newProducts, category.getProducts());
    }

    @Test
    void testDefaultValues() {
        Category defaultCategory = new Category();

        assertNull(defaultCategory.getId());
        assertNull(defaultCategory.getName());
        assertNull(defaultCategory.getImageHash());
        assertFalse(defaultCategory.isHighlights()); 
        assertNull(defaultCategory.getProducts());
    }

    @Test
    void testBuilderDefaultHighlights() {
        Category categoryWithDefaultHighlights = Category.builder()
                .id(3L)
                .name("Vinyls")
                .imageHash("hash456")
                .products(new ArrayList<>())
                .build();

        assertFalse(categoryWithDefaultHighlights.isHighlights());
    }

    @Test
    void testProductsRelationship() {
        category.setProducts(products);

        assertNotNull(category.getProducts());
        assertEquals(2, category.getProducts().size());
    }
}
