package com.example.auth.rest.repository;

import com.example.auth.entity.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserRoleRestRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser_Id(Long userId);
}
