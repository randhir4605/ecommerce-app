package com.example.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entity.DummyUser;
import com.example.backend.repository.DummyUserRepository;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
public class BackendApplication {

	@Autowired
	DummyUserRepository dummyUserRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@GetMapping("/")
	public ResponseEntity<String> hello(){
		return new ResponseEntity<>("Hello from backend",HttpStatus.OK);
	}

	@GetMapping("/db_test")
	public ResponseEntity<List<DummyUser>> dbTest(){
		return new ResponseEntity<>(dummyUserRepository.findAll(),HttpStatus.OK);
	}

	@GetMapping("/api/auth/me")
	public ResponseEntity<?> currentUser(
			org.springframework.security.core.Authentication authentication) {

		if (authentication == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok(
			java.util.Map.of(
				"authenticated", authentication.isAuthenticated(),
				"name", authentication.getName(),
				"authorities", authentication.getAuthorities()
			)
		);
	}
}