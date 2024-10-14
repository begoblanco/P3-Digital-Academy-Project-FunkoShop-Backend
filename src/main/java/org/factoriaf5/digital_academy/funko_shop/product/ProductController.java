package org.factoriaf5.digital_academy.funko_shop.product;

import org.factoriaf5.digital_academy.funko_shop.firebase.ImageService;
import org.factoriaf5.digital_academy.funko_shop.product.product_exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api-endpoint}/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDto) {
        Long categoryId = productDto.getCategory() != null ? productDto.getCategory().getId() : null;

        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        try {

            String imageUrl1 = productDto.getImageHash()
                .map(imageHash -> imageService.uploadBase64(imageHash)
                .orElseThrow(() -> new IllegalArgumentException("Failed to upload image 1")))
                .orElse("https://iili.io/2HTt1PR.jpg");
            productDto.setImageHash(Optional.ofNullable(imageUrl1));

            String imageUrl2 = productDto.getImageHash2()
                .map(imageHash2 -> imageService.uploadBase64(imageHash2)
                .orElseThrow(() -> new IllegalArgumentException("Failed to upload image 2")))
                .orElse("https://iili.io/2HTt1PR.jpg");
            productDto.setImageHash2(Optional.ofNullable(imageUrl2));

            ProductDTO createdProduct = productService.createProduct(productDto, categoryId);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @PageableDefault(size = 8, sort = { "categoryId", "name" }) Pageable pageable) {
        Page<ProductDTO> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        try {
            ProductDTO product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 8, sort = { "categoryId", "name" }) Pageable pageable) {
        Page<ProductDTO> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/keyword/{keyword}")
    public ResponseEntity<Page<ProductDTO>> getProductsByKeyword(
            @PathVariable String keyword,
            @PageableDefault(size = 8, sort = { "categoryId", "name" }) Pageable pageable) {
        Page<ProductDTO> products = productService.searchProductsByKeyword(keyword, pageable);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductDTO productDto) {
        Long categoryId = productDto.getCategory() != null ? productDto.getCategory().getId() : null;

        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        try {
            if (productDto.getImageHash() != null && productDto.getImageHash().isPresent()) {
                String imageUrl1 = imageService.uploadBase64(productDto.getImageHash().get())
                        .orElseThrow(() -> new IllegalArgumentException("Failed to upload image 1"));
                productDto.setImageHash(Optional.of(imageUrl1));
            }

            if (productDto.getImageHash2() != null && productDto.getImageHash2().isPresent()) {
                String imageUrl2 = imageService.uploadBase64(productDto.getImageHash2().get())
                        .orElseThrow(() -> new IllegalArgumentException("Failed to upload image 2"));
                productDto.setImageHash2(Optional.of(imageUrl2));
            }

            ProductDTO updatedProduct = productService.updateProduct(id, productDto);

            return ResponseEntity.ok(updatedProduct);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/discounted")
    public ResponseEntity<List<ProductDTO>> getDiscountedProducts() {
        List<ProductDTO> products = productService.getDiscountedProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/new")
    public ResponseEntity<List<ProductDTO>> getNewProducts() {
        List<ProductDTO> products = productService.getNewProducts();
        return ResponseEntity.ok(products);
    }
}
