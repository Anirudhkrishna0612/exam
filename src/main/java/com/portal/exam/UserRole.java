package com.portal.exam;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn; // Added import for @JoinColumn
import javax.persistence.ManyToOne;
import javax.persistence.Table; // Added import for @Table

@Entity
@Table(name = "user_role") // ADDED: Explicitly define table name
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // CHANGED: For MySQL AUTO_INCREMENT
    private Long userRoleId;

    // User
    // FetchType.EAGER is kept as per your original code
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id") // ADDED: Explicitly define foreign key column
    private User user;

    // Role
    // FetchType.EAGER is default for @ManyToOne, added explicitly for clarity
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id") // ADDED: Explicitly define foreign key column
    private Role role;

    // --- Constructors ---

    public UserRole() {

    }

    // All-argument constructor
    public UserRole(Long userRoleId, User user, Role role) {
        this.userRoleId = userRoleId;
        this.user = user;
        this.role = role;
    }

    // --- Getters and Setters (kept as is) ---

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}