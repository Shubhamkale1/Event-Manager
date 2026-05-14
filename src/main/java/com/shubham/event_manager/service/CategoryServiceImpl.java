package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.CategoryDTO;
import com.shubham.event_manager.entity.Category;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.CategoryRepository;
import com.shubham.event_manager.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl
        implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    private CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .icon(category.getIcon())
                .eventCount(category.getEvents().size())
                .createdAt(category.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository
                .findAllOrderByEventCountDesc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        return toDTO(categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found: " + id)));
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(
            CategoryDTO dto) {

        if (categoryRepository.existsByNameIgnoreCase(
                dto.getName())) {
            throw new IllegalArgumentException(
                    "Category already exists: "
                            + dto.getName());
        }

        Category category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .icon(dto.getIcon())
                .build();

        Category saved =
                categoryRepository.save(category);
        log.info("Category created: {}",
                saved.getName());
        return toDTO(saved);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(
            Long id, CategoryDTO dto) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found: " + id));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setIcon(dto.getIcon());

        return toDTO(categoryRepository
                .save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found: " + id));
        categoryRepository.delete(category);
        log.info("Category deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoriesByEvent(
            Long eventId) {

        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException(
                    "Event not found: " + eventId);
        }

        return eventRepository.findById(eventId)
                .get()
                .getCategories()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}