package org.factoriaf5.digital_academy.funko_shop.product;

import org.factoriaf5.digital_academy.funko_shop.category.CategoryDTO;
import org.factoriaf5.digital_academy.funko_shop.discount.DiscountDTO;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;



public class ProductDTOTest {

    @Test
    public void testProductDTOConstructorAndGetters() {
        CategoryDTO category = new CategoryDTO(1L, "CategoryName", null);
        DiscountDTO discount = new DiscountDTO();
        
        ProductDTO product = new ProductDTO(1L, "ProductName", "ImageHash", "Description", 100.0f, 10, true, category, discount);
        
        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("ProductName");
        assertThat(product.getImageHash()).isEqualTo("ImageHash");
        assertThat(product.getDescription()).isEqualTo("Description");
        assertThat(product.getPrice()).isEqualTo(100.0f);
        assertThat(product.getStock()).isEqualTo(10);
        assertThat(product.isAvailable()).isTrue();
        assertThat(product.getCategory()).isEqualTo(category);
        assertThat(product.getDiscount()).isEqualTo(discount);
    }

    @Test
    public void testProductDTOSetters() {
        CategoryDTO category = new CategoryDTO(1L, "CategoryName", null);
        DiscountDTO discount = new DiscountDTO();
        
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("ProductName");
        product.setImageHash("ImageHash");
        product.setDescription("Description");
        product.setPrice(100.0f);
        product.setStock(10);
        product.setAvailable(true);
        product.setCategory(category);
        product.setDiscount(discount);
        
        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("ProductName");
        assertThat(product.getImageHash()).isEqualTo("ImageHash");
        assertThat(product.getDescription()).isEqualTo("Description");
        assertThat(product.getPrice()).isEqualTo(100.0f);
        assertThat(product.getStock()).isEqualTo(10);
        assertThat(product.isAvailable()).isTrue();
        assertThat(product.getCategory()).isEqualTo(category);
        assertThat(product.getDiscount()).isEqualTo(discount);
    }

    @Test
    public void testProductDTOEquality() {
        CategoryDTO category = new CategoryDTO(1L, "CategoryName", null);
        DiscountDTO discount = new DiscountDTO();
        
        ProductDTO product1 = new ProductDTO(1L, "ProductName", "ImageHash", "Description", 100.0f, 10, true, category, discount);
        ProductDTO product2 = new ProductDTO(1L, "ProductName", "ImageHash", "Description", 100.0f, 10, true, category, discount);
        
        assertThat(product1).isEqualTo(product2);
        assertThat(product1.hashCode()).isEqualTo(product2.hashCode());
    }
}