 package org.factoriaf5.digital_academy.funko_shop.product;

 import org.factoriaf5.digital_academy.funko_shop.category.CategoryDTO;
import org.factoriaf5.digital_academy.funko_shop.firebase.ImageService;
import org.factoriaf5.digital_academy.funko_shop.product.product_exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        ProductDTO productDto = new ProductDTO();
        productDto.setCategory(new CategoryDTO(1L));
        productDto.setImageHash(Optional.of("base64Image1"));
        productDto.setImageHash2(Optional.of("base64Image2"));

        when(imageService.uploadBase64(any())).thenReturn(Optional.of("uploadedImageUrl"));
        when(productService.createProduct(any(), any())).thenReturn(productDto);

        ResponseEntity<ProductDTO> response = productController.createProduct(productDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }

    @Test
    void createProduct_ShouldThrowIllegalArgumentException_WhenCategoryIsNull() {
        ProductDTO productDto = new ProductDTO();
        productDto.setCategory(null);

        assertThrows(IllegalArgumentException.class, () -> productController.createProduct(productDto));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenFound() {
        Long productId = 1L;
        ProductDTO productDto = new ProductDTO();

        when(productService.getProductById(productId)).thenReturn(productDto);

        ResponseEntity<ProductDTO> response = productController.getProductById(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }

    @Test
    void getProductById_ShouldReturnNotFound_WhenProductNotFound() {
        Long productId = 1L;

        when(productService.getProductById(productId)).thenThrow(new ProductNotFoundException("Product not found"));

        ResponseEntity<ProductDTO> response = productController.getProductById(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct1() {
        Long productId = 1L;
        ProductDTO productDto = new ProductDTO();
        productDto.setCategory(new CategoryDTO(1L));

        when(productService.updateProduct(eq(productId), any())).thenReturn(productDto);

        ResponseEntity<ProductDTO> response = productController.updateProduct(productId, productDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() {
        Long productId = 1L;

        doNothing().when(productService).deleteProduct(productId);

        ResponseEntity<Void> response = productController.deleteProduct(productId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getAllProducts_ShouldReturnPageOfProducts() {
        ProductDTO productDto1 = new ProductDTO();
        ProductDTO productDto2 = new ProductDTO();
        Page<ProductDTO> productPage = new PageImpl<>(Arrays.asList(productDto1, productDto2));

        when(productService.getAllProducts(any())).thenReturn(productPage);

        ResponseEntity<Page<ProductDTO>> response = productController.getAllProducts(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() {
        Long productId = 1L;
        ProductDTO productDto = new ProductDTO();
        productDto.setCategory(new CategoryDTO(1L));
        productDto.setImageHash(Optional.of("base64Image1"));
        productDto.setImageHash2(Optional.of("base64Image2"));

        when(imageService.uploadBase64(any())).thenReturn(Optional.of("uploadedImageUrl"));
        when(productService.updateProduct(eq(productId), any())).thenReturn(productDto);

        ResponseEntity<ProductDTO> response = productController.updateProduct(productId, productDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
        verify(productService).updateProduct(eq(productId), any());
    }

    @Test
    void updateProduct_ShouldThrowIllegalArgumentException_WhenCategoryIsNull() {
        Long productId = 1L;
        ProductDTO productDto = new ProductDTO();
        productDto.setCategory(null);

        assertThrows(IllegalArgumentException.class, () -> productController.updateProduct(productId, productDto));
    }

    @Test
    void updateProduct_ShouldReturnBadRequest_WhenImageUploadFails() {
        Long productId = 1L;
        ProductDTO productDto = new ProductDTO();
        productDto.setCategory(new CategoryDTO(1L));
        productDto.setImageHash(Optional.of("base64Image1"));

        when(imageService.uploadBase64(any())).thenThrow(new IllegalArgumentException("Failed to upload image 1"));

        ResponseEntity<ProductDTO> response = productController.updateProduct(productId, productDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateProduct_ShouldReturnBadRequest_WhenUpdateFails() {
        Long productId = 1L;
        ProductDTO productDto = new ProductDTO();
        productDto.setCategory(new CategoryDTO(1L));

        when(imageService.uploadBase64(any())).thenReturn(Optional.of("uploadedImageUrl"));
        when(productService.updateProduct(eq(productId), any())).thenThrow(new RuntimeException("Update failed"));

        ResponseEntity<ProductDTO> response = productController.updateProduct(productId, productDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getDiscountedProducts_ShouldReturnDiscountedProducts() {
        List<ProductDTO> expectedProducts = new ArrayList<>();
        expectedProducts.add(new ProductDTO());
        expectedProducts.add(new ProductDTO());

        when(productService.getDiscountedProducts()).thenReturn(expectedProducts);

        ResponseEntity<List<ProductDTO>> response = productController.getDiscountedProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProducts, response.getBody());
        verify(productService).getDiscountedProducts();
    }

    @Test
    void getNewProducts_ShouldReturnNewProducts() {
        List<ProductDTO> expectedProducts = new ArrayList<>();
        expectedProducts.add(new ProductDTO());
        expectedProducts.add(new ProductDTO());

        when(productService.getNewProducts()).thenReturn(expectedProducts);

        ResponseEntity<List<ProductDTO>> response = productController.getNewProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProducts, response.getBody());
        verify(productService).getNewProducts();
    }

    @Test
    void getProductsByCategory_ShouldReturnProductsByCategory() {
        Long categoryId = 1L;
        Pageable pageable = Pageable.ofSize(8);
        List<ProductDTO> expectedProducts = new ArrayList<>();
        expectedProducts.add(new ProductDTO());
        expectedProducts.add(new ProductDTO());

        Page<ProductDTO> productPage = new PageImpl<>(expectedProducts, pageable, expectedProducts.size());

        when(productService.getProductsByCategory(categoryId, pageable)).thenReturn(productPage);

        ResponseEntity<Page<ProductDTO>> response = productController.getProductsByCategory(categoryId, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productPage, response.getBody());
        verify(productService).getProductsByCategory(categoryId, pageable);
    }

    @Test
    void getProductsByKeyword_ShouldReturnProductsByKeyword() {
        String keyword = "funko";
        Pageable pageable = Pageable.ofSize(8);
        List<ProductDTO> expectedProducts = new ArrayList<>();
        expectedProducts.add(new ProductDTO());
        expectedProducts.add(new ProductDTO());

        Page<ProductDTO> productPage = new PageImpl<>(expectedProducts, pageable, expectedProducts.size());

        when(productService.searchProductsByKeyword(keyword, pageable)).thenReturn(productPage);

        ResponseEntity<Page<ProductDTO>> response = productController.getProductsByKeyword(keyword, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productPage, response.getBody());
        verify(productService).searchProductsByKeyword(keyword, pageable);
    }
}
