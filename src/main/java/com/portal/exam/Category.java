// src/main/java/com/portal/exam/Category.java
package com.portal.exam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
// **CRITICAL FIX: Changed to javax.validation.constraints for compatibility with your environment**
import javax.validation.constraints.NotBlank; 
import javax.validation.constraints.Size;   


// Lombok annotations for boilerplate code (if Lombok is set up in pom.xml)
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

@Entity
@Table(name = "categories") // Table name in the database
// @Getter // Uncomment if using Lombok
// @Setter // Uncomment if using Lombok
// @NoArgsConstructor // Uncomment if using Lombok
// @AllArgsConstructor // Uncomment if using Lombok
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    private Long cid; // Category ID

    @NotBlank(message = "Title cannot be blank") // Validation: field cannot be null or empty
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters") // Validation: size constraints
    @Column(length = 100) // Database column constraint
    private String title;

    @Column(length = 500) // Database column constraint
    private String description;

    // --- Constructors (if not using Lombok) ---
    public Category() {
        // Default constructor for JPA
    }

    public Category(Long cid, String title, String description) {
        this.cid = cid;
        this.title = title;
        this.description = description;
    }

    // --- Getters and Setters (if not using Lombok) ---
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
               "cid=" + cid +
               ", title='" + title + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}
