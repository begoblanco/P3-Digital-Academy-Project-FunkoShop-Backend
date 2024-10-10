package org.factoriaf5.digital_academy.funko_shop.product;

import org.factoriaf5.digital_academy.funko_shop.category.Category;
import org.factoriaf5.digital_academy.funko_shop.category.CategoryDTO;
import org.factoriaf5.digital_academy.funko_shop.category.CategoryRepository;
import org.factoriaf5.digital_academy.funko_shop.category.category_exceptions.CategoryNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.discount.Discount;
import org.factoriaf5.digital_academy.funko_shop.discount.DiscountDTO;
import org.factoriaf5.digital_academy.funko_shop.discount.DiscountRepository;
import org.factoriaf5.digital_academy.funko_shop.discount.discount_exceptions.DiscountNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.product.product_exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductDTO testProductDTO;
    private Category testCategory;
    private Discount testDiscount;

    @BeforeEach
    void setUp() {
        testCategory = new Category(1L, "Test Category", "category-hash", null);
        testDiscount = new Discount(1L, 10.0f, true, null);
        testProduct = new Product(1L, "Test Product", "image-hash", "Description", 100.0f, 10, true, testCategory,
                testDiscount, null, null);
        testProductDTO = new ProductDTO();
        testProductDTO.setId(1L);
        testProductDTO.setName("Test Product");
        testProductDTO.setImageHash("image-hash");
        testProductDTO.setDescription("Description");
        testProductDTO.setPrice(100.0f);
        testProductDTO.setStock(10);
        testProductDTO.setAvailable(true);
        testProductDTO.setCategory(new CategoryDTO(1L));
        testProductDTO.setDiscount(new DiscountDTO(1L));
    }

    @Test
    void createProduct_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(discountRepository.findById(1L)).thenReturn(Optional.of(testDiscount));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO result = productService.createProduct(testProductDTO, 1L, 1L);

        assertNotNull(result);
        assertEquals(testProductDTO.getName(), result.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_CategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productService.createProduct(testProductDTO, 1L, 1L));
    }

    @Test
    void createProduct_DiscountNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(discountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DiscountNotFoundException.class, () -> productService.createProduct(testProductDTO, 1L, 1L));
    }

    @Test
    void createProduct_NullDiscount() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO result = productService.createProduct(testProductDTO, 1L, null);

        assertNotNull(result);
        assertEquals(testProductDTO.getName(), result.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getAllProducts_Success() {
        Page<Product> page = new PageImpl<>(Arrays.asList(testProduct));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ProductDTO> result = productService.getAllProducts(PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(testProductDTO.getName(), result.getContent().get(0).getName());
    }

    @Test
    void getAllProducts_EmptyList() {
        Page<Product> page = new PageImpl<>(Collections.emptyList());
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ProductDTO> result = productService.getAllProducts(PageRequest.of(0, 10));

        assertTrue(result.isEmpty());
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(testProductDTO.getName(), result.getName());
    }

    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated Product");
        updatedDTO.setImageHash("new-hash");
        updatedDTO.setDescription("New Description");
        updatedDTO.setPrice(200.0f);
        updatedDTO.setStock(20);
        updatedDTO.setAvailable(false);

        ProductDTO result = productService.updateProduct(1L, updatedDTO);

        assertNotNull(result);
        assertEquals(updatedDTO.getName(), result.getName());
        assertEquals(updatedDTO.getDescription(), result.getDescription());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setId(1L);

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, updatedDTO));
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void getProductsByCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        Page<Product> page = new PageImpl<>(Arrays.asList(testProduct));
        when(productRepository.findByCategory(eq(testCategory), any(Pageable.class))).thenReturn(page);

        Page<ProductDTO> result = productService.getProductsByCategory(1L, PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(testProductDTO.getName(), result.getContent().get(0).getName());
    }

    @Test
    void getProductsByCategory_CategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> productService.getProductsByCategory(1L, PageRequest.of(0, 10)));
    }

    @Test
    void getProductsByCategory_EmptyList() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        Page<Product> page = new PageImpl<>(Collections.emptyList());
        when(productRepository.findByCategory(eq(testCategory), any(Pageable.class))).thenReturn(page);

        Page<ProductDTO> result = productService.getProductsByCategory(1L, PageRequest.of(0, 10));

        assertTrue(result.isEmpty());
    }

    @Test
    void searchProductsByKeyword_Success() {
        Page<Product> page = new PageImpl<>(Arrays.asList(testProduct));
        when(productRepository.findByNameContainingIgnoreCase(eq("Test"), any(Pageable.class))).thenReturn(page);

        Page<ProductDTO> result = productService.searchProductsByKeyword("Test", PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(testProductDTO.getName(), result.getContent().get(0).getName());
    }

    @Test
    void searchProductsByKeyword_NoResults() {
        Page<Product> page = new PageImpl<>(Collections.emptyList());
        when(productRepository.findByNameContainingIgnoreCase(eq("NonExistent"), any(Pageable.class)))
                .thenReturn(page);

        Page<ProductDTO> result = productService.searchProductsByKeyword("NonExistent", PageRequest.of(0, 10));

        assertTrue(result.isEmpty());
    }

    @Test
    void updateProduct_WithCategory() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(categoryRepository.findById(2L))
                .thenReturn(Optional.of(new Category(2L, "New Category", "new-hash", null)));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated Product");
        updatedDTO.setImageHash("new-hash");
        updatedDTO.setDescription("New Description");
        updatedDTO.setPrice(200.0f);
        updatedDTO.setStock(20);
        updatedDTO.setAvailable(true);
        updatedDTO.setDiscount(null);

        updatedDTO.setCategory(new CategoryDTO(2L));

        ProductDTO result = productService.updateProduct(1L, updatedDTO);

        assertNotNull(result);
        assertEquals(2L, result.getCategory().getId());
    }

    @Test
    void updateProduct_WithDiscount() {
        Product testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Sample Product");
        testProduct.setImageHash("sampleHash");
        testProduct.setDescription("Sample Description");
        testProduct.setPrice(100.0f);
        testProduct.setStock(10);
        testProduct.setAvailable(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(discountRepository.findById(2L)).thenReturn(Optional.of(new Discount(2L, 20.0f, true, null)));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Sample Product");
        updatedDTO.setDescription("Sample Description");
        updatedDTO.setDiscount(new DiscountDTO(2L));

        ProductDTO result = productService.updateProduct(1L, updatedDTO);

        assertNotNull(result);
        assertEquals(2L, result.getDiscount().getId());
    }

    @Test
    void updateProduct_WithCategoryNotFound() {
        Product testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Sample Product");
        testProduct.setImageHash("sampleHash");
        testProduct.setDescription("Sample Description");
        testProduct.setPrice(100.0f);
        testProduct.setStock(10);
        testProduct.setAvailable(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated Product");
        updatedDTO.setDescription("Updated Description");
        updatedDTO.setCategory(new CategoryDTO(2L));

        assertThrows(CategoryNotFoundException.class, () -> productService.updateProduct(1L, updatedDTO));
    }

    @Test
    void updateProduct_WithDiscountNotFound() {
        Product testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Sample Product");
        testProduct.setImageHash("sampleHash");
        testProduct.setDescription("Sample Description");
        testProduct.setPrice(100.0f);
        testProduct.setStock(10);
        testProduct.setAvailable(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(discountRepository.findById(2L)).thenReturn(Optional.empty());

        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated Product");
        updatedDTO.setDescription("Updated Description");
        updatedDTO.setDiscount(new DiscountDTO(2L));

        assertThrows(DiscountNotFoundException.class, () -> productService.updateProduct(1L, updatedDTO));
    }

    @Test
    void updateProduct_WithNullCategory() {
        Product testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Sample Product");
        testProduct.setImageHash("sampleHash");
        testProduct.setDescription("Sample Description");
        testProduct.setPrice(100.0f);
        testProduct.setStock(10);
        testProduct.setAvailable(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated Product");
        updatedDTO.setDescription("Updated Description");
        updatedDTO.setCategory(null);

        ProductDTO result = productService.updateProduct(1L, updatedDTO);

        assertNotNull(result);
        assertNull(result.getCategory());
    }

    @Test
    void updateProduct_WithNullDiscount() {
        Product testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Sample Product");
        testProduct.setImageHash("sampleHash");
        testProduct.setDescription("Sample Description");
        testProduct.setPrice(100.0f);
        testProduct.setStock(10);
        testProduct.setAvailable(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated Product");
        updatedDTO.setDescription("Updated Description");
        updatedDTO.setDiscount(null);

        ProductDTO result = productService.updateProduct(1L, updatedDTO);

        assertNotNull(result);
        assertNull(result.getDiscount());
    }

    @Test
    void updateProduct_WithNullCategoryAndDiscount() {
        Product testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Sample Product");
        testProduct.setImageHash("sampleHash");
        testProduct.setDescription("Sample Description");
        testProduct.setPrice(100.0f);
        testProduct.setStock(10);
        testProduct.setAvailable(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated Product");
        updatedDTO.setDescription("Updated Description");
        updatedDTO.setCategory(null);
        updatedDTO.setDiscount(null);

        ProductDTO result = productService.updateProduct(1L, updatedDTO);

        assertNotNull(result);
        assertNull(result.getCategory());
        assertNull(result.getDiscount());
    }
}
