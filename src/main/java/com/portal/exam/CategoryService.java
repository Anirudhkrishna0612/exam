// src/main/java/com/portal/exam/CategoryService.java
package com.portal.exam;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    // Add a new category
    Category addCategory(Category category);

    // Update an existing category
    Category updateCategory(Category category);

    // Get all categories
    List<Category> getCategories();

    // Get a single category by ID
    Category getCategory(Long categoryId);

    // Delete a category by ID
    void deleteCategory(Long categoryId);
}
