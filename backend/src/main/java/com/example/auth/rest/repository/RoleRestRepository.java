package com.example.auth.rest.repository;

import com.example.auth.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface RoleRestRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
