package org.factoriaf5.digital_academy.funko_shop.product;

import org.factoriaf5.digital_academy.funko_shop.category.Category;
import org.factoriaf5.digital_academy.funko_shop.category.CategoryRepository;
import org.factoriaf5.digital_academy.funko_shop.category.category_exceptions.CategoryNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.product.product_exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_Success() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Funko Pop");
        productDTO.setImageHash(Optional.of("hash123"));
        productDTO.setImageHash2(Optional.of("hash456"));
        productDTO.setDescription("Description");
        productDTO.setPrice(20.0f);
        productDTO.setStock(10);
        productDTO.setDiscount(10);
        Category category = new Category();
        category.setId(1L);

        Product product = new Product();
        product.setName("Funko Pop");
        product.setPrice(20.0f);
        product.setStock(10);
        product.setDiscount(10);
        product.setCategory(category);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.createProduct(productDTO, 1L);

        assertNotNull(result);
        assertEquals("Funko Pop", result.getName());
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_CategoryNotFound() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Funko Pop");

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productService.createProduct(productDTO, 1L));
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    void getProductById_Success() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Funko Pop");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Funko Pop", result.getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepository).findById(1L);
    }

    @Test
    void getAllProducts_Success() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Funko Pop 1");
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Funko Pop 2");

        List<Product> products = Arrays.asList(product1, product2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductDTO> result = productService.getAllProducts(pageable);

        assertEquals(2, result.getContent().size());
        verify(productRepository).findAll(pageable);
    }

    @Test
    void updateProduct_Success() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Old Funko Pop");
        existingProduct.setDescription("Old Description");

        ProductDTO updatedProductDTO = new ProductDTO();
        updatedProductDTO.setName("Updated Funko Pop");
        updatedProductDTO.setDescription("Updated Description");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        ProductDTO result = productService.updateProduct(1L, updatedProductDTO);

        assertNotNull(result);
        assertEquals("Updated Funko Pop", result.getName());
        assertEquals("Updated Description", result.getDescription());
        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_ProductNotFound() {
        ProductDTO updatedProductDTO = new ProductDTO();
        updatedProductDTO.setName("Updated Funko Pop");

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, updatedProductDTO));
        verify(productRepository).findById(1L);
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository).existsById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository).existsById(1L);
        verify(productRepository, never()).deleteById(1L);
    }

    @Test
    void getDiscountedProducts_Success() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Funko Pop");
        product1.setDiscount(20);
        product1.setPrice(100.0f);

        when(productRepository.findByDiscount()).thenReturn(Arrays.asList(product1));

        List<ProductDTO> discountedProducts = productService.getDiscountedProducts();

        assertEquals(1, discountedProducts.size());
        assertEquals(80.0f, discountedProducts.get(0).getDiscountedPrice());
        verify(productRepository).findByDiscount();
    }

    @Test
    void getNewProducts_Success() {
        Product newProduct = new Product();
        newProduct.setId(1L);
        newProduct.setName("New Funko Pop");

        when(productRepository.findNewProducts(any(Date.class))).thenReturn(Collections.singletonList(newProduct));

        List<ProductDTO> newProducts = productService.getNewProducts();

        assertEquals(1, newProducts.size());
        assertEquals("New Funko Pop", newProducts.get(0).getName());
        verify(productRepository).findNewProducts(any(Date.class));
    }
}
