package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import com.example.backend.repository.DummyUserRepository;
import com.example.backend.entity.DummyUser;

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
		return new ResponseEntity<String>("Hello from backend",HttpStatus.OK);
	}

	@GetMapping("/db_test")
	public ResponseEntity<List<DummyUser>> dbTest(){
		return new ResponseEntity<List<DummyUser>>(dummyUserRepository.findAll(),HttpStatus.OK);
	}
}