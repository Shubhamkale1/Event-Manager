package com.shubham.event_manager.controller;

import com.shubham.event_manager.dto.CategoryDTO;
import com.shubham.event_manager.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories",
        description = "Event category management")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(
            summary = "Get all categories",
            description = "Returns categories ordered " +
                    "by event count descending"
    )
    public ResponseEntity<List<CategoryDTO>>
    getAllCategories() {
        return ResponseEntity.ok(
                categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<CategoryDTO>
    getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(
                categoryService.getCategoryById(id));
    }

    @GetMapping("/event/{eventId}")
    @Operation(
            summary = "Get categories for an event"
    )
    public ResponseEntity<List<CategoryDTO>>
    getCategoriesByEvent(
            @PathVariable Long eventId) {
        return ResponseEntity.ok(
                categoryService
                        .getCategoriesByEvent(eventId));
    }

    @PostMapping
    @Operation(
            summary = "Create a category",
            description = "Admin only",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<CategoryDTO>
    createCategory(
            @Valid @RequestBody CategoryDTO dto) {
        return new ResponseEntity<>(
                categoryService.createCategory(dto),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a category",
            description = "Admin only",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<CategoryDTO>
    updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(
                categoryService.updateCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a category",
            description = "Admin only",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}