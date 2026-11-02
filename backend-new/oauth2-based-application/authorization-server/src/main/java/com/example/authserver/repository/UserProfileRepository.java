package com.example.authserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{

	Optional<UserProfile> findByUser_Id(Long userId);
}
