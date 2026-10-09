
package com.example.backend.repository;

import com.example.backend.entity.DummyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyUserRepository extends JpaRepository<DummyUser, Long> {
}