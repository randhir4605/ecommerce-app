package com.example.backend.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{
	

	public Optional<UserAccount> findByUsername(String username);
	@Query("""
		    SELECT new com.example.backend.security.UserAccountRecord(
		        u.id, u.username,c.password, u.email, p.fullName, p.phone,p.address
		    )
		    FROM UserAccount u
		    LEFT JOIN u.profile p
		    LEFT JOIN u.credentials c
		    WHERE u.username = :username
		    """)
	public Optional<UserAccountRecord> findUserAccountRecordByUsername(@Param("username") String username);
}
