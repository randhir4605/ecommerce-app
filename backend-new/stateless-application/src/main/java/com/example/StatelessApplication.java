package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.app.entity.User;
import com.example.app.entity.UserCredentials;

@SpringBootApplication
@EntityScan(basePackageClasses = User.class)
public class StatelessApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatelessApplication.class, args);
	}
	
	//Debug bean
	@Bean
	CommandLineRunner verifyCredentials(UserCredentialsRepository repository,PasswordEncoder passwordEncoder) {

	    return args -> {
	        UserCredentials credentials = repository.findByUsernameIgnoreCase("demo_support3").orElseThrow();
	        System.out.println("Username found: "+ credentials.getUsername());
	        System.out.println("Password matches: "+ passwordEncoder.matches("support123",credentials.getPasswordHash()));
	    };
	}

}
