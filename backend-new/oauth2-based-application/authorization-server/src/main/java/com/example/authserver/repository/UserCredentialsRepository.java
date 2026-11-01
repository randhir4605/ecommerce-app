package com.example.authserver.repository;
import com.example.app.entity.UserCredentials;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {

    @EntityGraph(attributePaths = {"user", "user.userRoles.role"})
    Optional<UserCredentials> findByUsernameIgnoreCase(String username);
}
