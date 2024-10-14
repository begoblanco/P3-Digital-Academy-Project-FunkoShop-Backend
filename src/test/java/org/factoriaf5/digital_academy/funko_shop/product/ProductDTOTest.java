package org.factoriaf5.digital_academy.funko_shop.product;

import org.factoriaf5.digital_academy.funko_shop.category.CategoryDTO;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import jakarta.validation.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

    @Test
    void testProductDTO_AllFields() {
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Funko Pop", "imageHash", true);
        Date currentDate = new Date();

        ProductDTO productDTO = new ProductDTO(
                1L,
                "Test Product",
                Optional.of("imageHash1"),
                Optional.of("imageHash2"),
                "Test description",
                100.0f,
                80.0f,
                10,
                currentDate,
                categoryDTO,
                20,
                100,
                4.5);

        assertEquals(1L, productDTO.getId());
        assertEquals("Test Product", productDTO.getName());
        assertTrue(productDTO.getImageHash().isPresent());
        assertEquals("imageHash1", productDTO.getImageHash().get());
        assertEquals(100.0f, productDTO.getPrice());
        assertEquals(80.0f, productDTO.getDiscountedPrice());
        assertEquals(10, productDTO.getStock());
        assertEquals(currentDate, productDTO.getCreatedAt());
        assertEquals(20, productDTO.getDiscount());
        assertEquals(100, productDTO.getTotalReviews());
        assertEquals(4.5, productDTO.getAverageRating());
    }

    @Test
    void testPriceConstraint() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPrice(-10.0f);
        productDTO.setStock(10);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertFalse(violations.isEmpty());
        assertEquals("Price must be positive", violations.iterator().next().getMessage());
    }

    @Test
    void testStockConstraint() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setStock(-5);
        productDTO.setPrice(10.0f);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);

        assertFalse(violations.isEmpty());
        assertEquals("Stock must be positive or zero", violations.iterator().next().getMessage());
    }
}
