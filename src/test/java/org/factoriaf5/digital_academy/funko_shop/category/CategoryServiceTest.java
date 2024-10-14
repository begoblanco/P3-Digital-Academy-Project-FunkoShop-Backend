package org.factoriaf5.digital_academy.funko_shop.category;

import org.factoriaf5.digital_academy.funko_shop.category.category_exceptions.CategoryException;
import org.factoriaf5.digital_academy.funko_shop.category.category_exceptions.CategoryNotFoundException;
import org.factoriaf5.digital_academy.funko_shop.category.category_exceptions.TooManyCategoriesSelectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_ReturnsListOfCategories() {
        List<Category> categories = Arrays.asList(
                new Category(1L, "Category 1", "img1", false, null),
                new Category(2L, "Category 2", "img2", true, null)
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Category 1", result.get(0).getName());
        assertEquals("Category 2", result.get(1).getName());
    }

    @Test
    void getCategoryById_ReturnsCategoryDTO_WhenCategoryExists() {
        Category category = new Category(1L, "Category 1", "img1", false, null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals("Category 1", result.getName());
        assertEquals("img1", result.getImageHash());
    }

    @Test
    void getCategoryById_ThrowsCategoryNotFoundException_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void updateCategory_ReturnsUpdatedCategoryDTO_WhenValidData() {
        Category existingCategory = new Category(1L, "Category 1", "img1", false, null);
        CategoryDTO updateDTO = new CategoryDTO(1L, "Updated Category", "newImg", true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.countByHighlights(true)).thenReturn(1);
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        CategoryDTO result = categoryService.updateCategory(updateDTO);

        assertNotNull(result);
        assertEquals("Updated Category", result.getName());
        assertEquals("newImg", result.getImageHash());
        assertTrue(result.isHighlights());
    }

    @Test
    void updateCategory_ThrowsTooManyCategoriesSelectedException_WhenHighlightLimitExceeded() {
        CategoryDTO updateDTO = new CategoryDTO(1L, "Category", "img", true);
        when(categoryRepository.countByHighlights(true)).thenReturn(2);

        assertThrows(TooManyCategoriesSelectedException.class, () -> categoryService.updateCategory(updateDTO));
    }

    @Test
    void updateCategory_ThrowsCategoryException_WhenCategoryNotFound() {
        CategoryDTO updateDTO = new CategoryDTO(1L, "Category", "img", true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryException.class, () -> categoryService.updateCategory(updateDTO));
    }
}
