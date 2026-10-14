package com.example.backend.security;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserAccountService {
	
	private final UserAccountRepository userAccountRepository;
	
	public UserAccountService(UserAccountRepository userAccountRepository) {
		this.userAccountRepository=userAccountRepository;
	}
	
	public Optional<UserAccountRecord> getUserAccount(String username) {
		return userAccountRepository.findUserAccountRecordByUsername(username);
	}

}
