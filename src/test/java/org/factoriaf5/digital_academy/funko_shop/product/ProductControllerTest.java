// package org.factoriaf5.digital_academy.funko_shop.product;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.factoriaf5.digital_academy.funko_shop.category.CategoryDTO;
// import org.factoriaf5.digital_academy.funko_shop.product.product_exceptions.ProductNotFoundException;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.Arrays;
// import java.util.List;

// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(ProductController.class)
// @AutoConfigureWebMvc
// public class ProductControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private ProductService productService;

//     @Autowired
//     private ObjectMapper objectMapper;

//     private ProductDTO productDTO;
//     private static final String API_ENDPOINT = "/api/v1/products";

//     @BeforeEach
//     void setUp() {
//         productDTO = new ProductDTO();
//         productDTO.setId(1L);
//         productDTO.setName("Test Product");
//         productDTO.setDescription("Test Description");
//         productDTO.setStock(100);
//         productDTO.setImageHash("test-hash");
//         productDTO.setPrice(19.99f);
//         CategoryDTO categoryDTO = new CategoryDTO();
//         categoryDTO.setId(1L);
//         productDTO.setCategory(categoryDTO);
//         productDTO.setDiscount(null);
//         productDTO.setAvailable(false);
//     }

//     @Test
//     void createProduct_ShouldReturnCreatedProduct() throws Exception {
//         when(productService.createProduct(any(ProductDTO.class), anyLong(), any())).thenReturn(productDTO);

//         mockMvc.perform(post(API_ENDPOINT)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(productDTO)))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.name").value("Test Product"));
//     }

//     @Test
//     void getAllProducts_ShouldReturnProductPage() throws Exception {
//         List<ProductDTO> products = Arrays.asList(productDTO);
//         Page<ProductDTO> productPage = new PageImpl<>(products);

//         when(productService.getAllProducts(any(Pageable.class))).thenReturn(productPage);

//         mockMvc.perform(get(API_ENDPOINT))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.content[0].id").value(1))
//                 .andExpect(jsonPath("$.content[0].name").value("Test Product"));
//     }

//     @Test
//     void getProductById_ShouldReturnProduct() throws Exception {
//         when(productService.getProductById(1L)).thenReturn(productDTO);

//         mockMvc.perform(get(API_ENDPOINT + "/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.name").value("Test Product"));
//     }

//     @Test
//     void getProductById_ShouldReturnNotFound() throws Exception {
//         when(productService.getProductById(99L)).thenThrow(new ProductNotFoundException("Product not found"));

//         mockMvc.perform(get(API_ENDPOINT + "/99"))
//                 .andExpect(status().isNotFound());
//     }

//     @Test
//     void getProductsByCategory_ShouldReturnProductPage() throws Exception {
//         List<ProductDTO> products = Arrays.asList(productDTO);
//         Page<ProductDTO> productPage = new PageImpl<>(products);

//         when(productService.getProductsByCategory(anyLong(), any(Pageable.class))).thenReturn(productPage);

//         mockMvc.perform(get(API_ENDPOINT + "/category/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.content[0].id").value(1))
//                 .andExpect(jsonPath("$.content[0].name").value("Test Product"));
//     }

//     @Test
//     void getProductsByKeyword_ShouldReturnProductPage() throws Exception {
//         List<ProductDTO> products = Arrays.asList(productDTO);
//         Page<ProductDTO> productPage = new PageImpl<>(products);

//         when(productService.searchProductsByKeyword(anyString(), any(Pageable.class))).thenReturn(productPage);

//         mockMvc.perform(get(API_ENDPOINT + "/keyword/test"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.content[0].id").value(1))
//                 .andExpect(jsonPath("$.content[0].name").value("Test Product"));
//     }

//     @Test
//     void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
//         when(productService.updateProduct(anyLong(), any(ProductDTO.class))).thenReturn(productDTO);

//         mockMvc.perform(put(API_ENDPOINT + "/1")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(productDTO)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.name").value("Test Product"));
//     }

//     @Test
//     void deleteProduct_ShouldReturnNoContent() throws Exception {
//         doNothing().when(productService).deleteProduct(1L);

//         mockMvc.perform(delete(API_ENDPOINT + "/1"))
//                 .andExpect(status().isNoContent());
//     }

//     @Test
//     void createProduct_ShouldReturnBadRequest_WhenNameIsEmpty() throws Exception {
//         productDTO.setName(""); // Nombre vacío

//         mockMvc.perform(post(API_ENDPOINT)
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(productDTO)))
//                 .andExpect(status().isBadRequest());
//     }
// }
