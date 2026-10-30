package com.example.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_permissions",
    uniqueConstraints = @UniqueConstraint(name = "uq_role_permission",
        columnNames = {"role_id", "permission_id"}),
    indexes = @Index(name = "idx_role_permissions_permission_id", columnList = "permission_id"))
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    protected RolePermission() {}
    public RolePermission(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
    }
    public Long getId() { return id; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Permission getPermission() { return permission; }
    public void setPermission(Permission permission) { this.permission = permission; }
}
