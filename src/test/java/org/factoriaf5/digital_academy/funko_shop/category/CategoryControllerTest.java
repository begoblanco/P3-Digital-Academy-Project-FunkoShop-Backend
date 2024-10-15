package org.factoriaf5.digital_academy.funko_shop.category;

import org.factoriaf5.digital_academy.funko_shop.profile.profile_exceptions.ProfileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    private CategoryDTO categoryDTO1;
    private CategoryDTO categoryDTO2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryDTO1 = new CategoryDTO(1L, "Pop Culture", "imageHash1", false);
        categoryDTO2 = new CategoryDTO(2L, "Anime", "imageHash2", true);
    }

    @Test
    void testGetAllCategories() {
        List<CategoryDTO> categories = Arrays.asList(categoryDTO1, categoryDTO2);
        when(categoryService.getAllCategories()).thenReturn(categories);

        ResponseEntity<List<CategoryDTO>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void testGetCategoryById() {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryDTO1);

        ResponseEntity<CategoryDTO> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pop Culture", response.getBody().getName());
        verify(categoryService, times(1)).getCategoryById(1L);
    }

   @Test
void testUpdateCategory_Success() throws Exception {
    when(categoryService.updateCategory(any(CategoryDTO.class))).thenReturn(null); 

    ResponseEntity<?> response = categoryController.updateCategory(categoryDTO1);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(categoryService, times(1)).updateCategory(any(CategoryDTO.class));
}


    @Test
    void testUpdateCategory_ProfileNotFound() throws ProfileNotFoundException {
        doThrow(new ProfileNotFoundException("not found")).when(categoryService).updateCategory(any(CategoryDTO.class));

        ResponseEntity<?> response = categoryController.updateCategory(categoryDTO1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(categoryService, times(1)).updateCategory(categoryDTO1);
    }
}
