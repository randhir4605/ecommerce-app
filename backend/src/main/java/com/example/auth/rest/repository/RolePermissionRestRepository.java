package com.example.auth.rest.repository;

import com.example.auth.entity.RolePermission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface RolePermissionRestRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRole_Id(Long roleId);
}
