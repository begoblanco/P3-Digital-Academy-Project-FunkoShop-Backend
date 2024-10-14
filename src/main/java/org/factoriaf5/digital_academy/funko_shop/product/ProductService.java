package org.factoriaf5.digital_academy.funko_shop.product;

import org.factoriaf5.digital_academy.funko_shop.category.Category;
import org.factoriaf5.digital_academy.funko_shop.category.CategoryDTO;
import org.factoriaf5.digital_academy.funko_shop.category.CategoryRepository;
import org.factoriaf5.digital_academy.funko_shop.category.category_exceptions.CategoryNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.order_item.OrderItem;
import org.factoriaf5.digital_academy.funko_shop.product.product_exceptions.ProductNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.review.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductDTO createProduct(ProductDTO productDto, Long categoryId) {
        Category category = getCategoryById(categoryId);

        if (productDto.getStock() < 0) {
            throw new IllegalArgumentException("Stock can't be negative");
        }

        Product product = mapToEntity(productDto);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return mapToDTO(product);
    }

    public Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        Category category = getCategoryById(categoryId);
        return productRepository.findByCategory(category, pageable)
                .map(this::mapToDTO);
    }

    public Page<ProductDTO> searchProductsByKeyword(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable)
                .map(this::mapToDTO);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        updateProductFields(existingProduct, productDto);

        Product updatedProduct = productRepository.save(existingProduct);
        return mapToDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
    }

    public List<ProductDTO> getDiscountedProducts() {
        List<Product> products = productRepository.findByDiscount();

        return products.stream()
                .map(this::mapToDTOWithDiscount)
                .collect(Collectors.toList());
    }

    private ProductDTO mapToDTOWithDiscount(Product product) {
        ProductDTO productDTO = mapToDTO(product);

        if (product.getDiscount() > 0 && product.getDiscount() <= 100) {
            float discountMultiplier = 1 - (product.getDiscount() / 100.0f);
            float discountedPrice = product.getPrice() * discountMultiplier;
            productDTO.setDiscountedPrice(discountedPrice);
        } else {
            productDTO.setDiscountedPrice(product.getPrice());
        }

        return productDTO;
    }

    private void updateProductFields(Product product, ProductDTO productDto) {
        product.setName(productDto.getName());
        if (productDto.getImageHash() != null && productDto.getImageHash().isPresent()
                && productDto.getImageHash().get() != null) {
            product.setImageHash(productDto.getImageHash().get());
        }
        if (productDto.getImageHash2() != null && productDto.getImageHash2().isPresent()
                && productDto.getImageHash2().get() != null) {
            product.setImageHash2(productDto.getImageHash2().get());
        }
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setDiscount(productDto.getDiscount());

        if (productDto.getCategory() != null) {
            product.setCategory(getCategoryById(productDto.getCategory().getId()));
        }
    }

    public List<ProductDTO> getNewProducts() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date cutoffDate = calendar.getTime();

        List<Product> newProducts = productRepository.findNewProducts(cutoffDate);

        return newProducts.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private Product mapToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setImageHash(dto.getImageHash().get());
        product.setImageHash2(dto.getImageHash2().get());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDiscount(dto.getDiscount());

        return product;
    }

    private ProductDTO mapToDTO(Product product) {
        CategoryDTO categoryDTO = null;
        if (product.getCategory() != null) {
            categoryDTO = new CategoryDTO(
                    product.getCategory().getId(),
                    product.getCategory().getName(),
                    product.getCategory().getImageHash(),
                    product.getCategory().isHighlights());
        }

        List<Review> reviews = Optional.ofNullable(product.getOrderItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(OrderItem::getReview)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    
        int totalReviews = reviews.size();
        double averageRating = totalReviews > 0 ? reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0) : 0.0;

        return new ProductDTO(
                product.getId(),
                product.getName(),
                Optional.ofNullable(product.getImageHash()),
                Optional.ofNullable(product.getImageHash2()),
                product.getDescription(),
                product.getPrice(),
                product.getPrice(),
                product.getStock(),
                product.getCreatedAt(),
                categoryDTO,
                product.getDiscount(),
                totalReviews,
                averageRating);
    }

}
