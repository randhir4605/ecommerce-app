package com.example;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.entity.UserCredentials;

public interface UserCredentialsRepository
        extends JpaRepository<UserCredentials, Long> {

    @EntityGraph(attributePaths = {"user","user.userRoles.role"})
    Optional<UserCredentials> findByUsernameIgnoreCase(String username);
}