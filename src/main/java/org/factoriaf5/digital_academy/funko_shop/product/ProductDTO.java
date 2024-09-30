package org.factoriaf5.digital_academy.funko_shop.product;

import org.factoriaf5.digital_academy.funko_shop.category.CategoryDTO;

import jakarta.validation.constraints.Positive;
import lombok.*;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    
    private Long id;
    private String name;
    private String imageHash;
    private String description;
    @Positive(message = "Price must be positive")
    private float price;
    private float discountedPrice;
    @Positive(message = "Stock must be positive or zero")
    private int stock;
    private Date createdAt;
    private CategoryDTO category;
    private int discount;
}
