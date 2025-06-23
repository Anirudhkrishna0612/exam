// src/main/java/com/portal/exam/CategoryServiceImpl.java
package com.portal.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marks this as a Spring Service component
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository; // Inject the repository

    @Override
    public Category addCategory(Category category) {
        return this.categoryRepository.save(category); // Save the new category
    }

    @Override
    public Category updateCategory(Category category) {
        // For update, ensure the category exists before saving
        // save() method in JpaRepository will update if ID exists, otherwise insert
        return this.categoryRepository.save(category); 
    }

    @Override
    public List<Category> getCategories() {
        return this.categoryRepository.findAll(); // Retrieve all categories
    }

    @Override
    public Category getCategory(Long categoryId) {
        // findById returns an Optional, so handle the case where it might not exist
        return this.categoryRepository.findById(categoryId).orElse(null); // Or throw an exception
    }

    @Override
    public void deleteCategory(Long categoryId) {
        // You can fetch the category first to ensure it exists before deleting,
        // or just delete by ID directly.
        this.categoryRepository.deleteById(categoryId);
    }
}
