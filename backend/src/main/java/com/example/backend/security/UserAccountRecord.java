package com.example.backend.security;


public record UserAccountRecord(
		Long id,
		String username,
		String password,
		String email,
		String fullname,
		String phone,
		String address
		
		) {}