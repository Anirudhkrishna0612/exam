// src/main/java/com/portal/exam/CategoryRepository.java
package com.portal.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Recommended for clarity, though not strictly required for JpaRepository

@Repository // Optional but good practice to indicate it's a repository component
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // JpaRepository provides common CRUD operations like save, findById, findAll, delete, etc.
    // You can add custom query methods here if needed, e.g.,
    // Category findByTitle(String title);
}
