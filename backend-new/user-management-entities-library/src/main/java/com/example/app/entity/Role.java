package com.example.app.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "role")
    @Fetch(FetchMode.SUBSELECT)
    private List<RolePermission> rolePermissions = new ArrayList<>();

    protected Role() {}
    public Long getId() { return id; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public List<UserRole> getUserRoles() { return userRoles; }
    public List<RolePermission> getRolePermissions() { return rolePermissions; }
    public void addPermission(Permission permission) {
        RolePermission link = new RolePermission(this, permission);
        rolePermissions.add(link);
        permission.getRolePermissions().add(link);
    }
}
