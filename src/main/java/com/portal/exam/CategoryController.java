// src/main/java/com/portal/exam/CategoryController.java
package com.portal.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid; // For @Valid annotation

import java.util.List;

@RestController // Marks this class as a REST Controller
@RequestMapping("/category") // Base path for all category related endpoints
// @CrossOrigin("*") // Removed @CrossOrigin as it's handled globally in SecurityConfig
public class CategoryController {

    @Autowired
    private CategoryService categoryService; // Inject the CategoryService

    // Add new category
    // POST http://localhost:8060/category/
    @PostMapping("/")
    public ResponseEntity<Category> addCategory(@Valid @RequestBody Category category) {
        try {
            Category addedCategory = this.categoryService.addCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedCategory);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all categories
    // GET http://localhost:8060/category/
    @GetMapping("/")
    public ResponseEntity<List<Category>> getCategories() {
        try {
            List<Category> categories = this.categoryService.getCategories();
            if (categories.isEmpty()) {
                return ResponseEntity.noContent().build(); // HTTP 204 No Content
            }
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get single category by ID
    // GET http://localhost:8060/category/{categoryId}
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable("categoryId") Long categoryId) {
        try {
            Category category = this.categoryService.getCategory(categoryId);
            if (category == null) {
                return ResponseEntity.notFound().build(); // HTTP 404 Not Found
            }
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update category
    // PUT http://localhost:8060/category/
    @PutMapping("/")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category category) {
        // You might want to check if category.getCid() is not null for updates
        // The save() method in JpaRepository will update if ID exists.
        try {
            if (category.getCid() == null) {
                return ResponseEntity.badRequest().build(); // ID is required for update
            }
            Category updatedCategory = this.categoryService.updateCategory(category);
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete category
    // DELETE http://localhost:8060/category/{categoryId}
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        try {
            this.categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok().build(); // HTTP 200 OK
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
