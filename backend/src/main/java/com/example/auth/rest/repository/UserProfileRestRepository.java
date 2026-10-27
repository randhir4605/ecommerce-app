package com.example.auth.rest.repository;

import com.example.auth.entity.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "userProfiles", path = "user-profiles")
public interface UserProfileRestRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser_Id(Long userId);
}
