package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id);

    CategoryDTO createCategory(CategoryDTO dto);

    CategoryDTO updateCategory(Long id,
                               CategoryDTO dto);

    void deleteCategory(Long id);

    List<CategoryDTO> getCategoriesByEvent(
            Long eventId);
}