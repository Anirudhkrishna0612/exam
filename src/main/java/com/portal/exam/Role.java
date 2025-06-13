package com.portal.exam;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column; // Added import for @Column
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue; // Added import for @GeneratedValue
import javax.persistence.GenerationType; // Added import for GenerationType
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ADDED: For MySQL AUTO_INCREMENT
    private Long roleId;

    @Column // Added @Column for explicit mapping
    private String roleName;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "role")
    private Set<UserRole> userRoles = new HashSet<>();


    // --- Constructors ---

    public Role() {

    }

    public Role(Long roleId, String roleName) {
        super();
        this.roleId = roleId;
        this.roleName = roleName;
    }

    // --- Getters and Setters (kept as is) ---

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}