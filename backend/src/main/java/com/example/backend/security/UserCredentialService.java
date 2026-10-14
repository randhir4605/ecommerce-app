package com.example.backend.security;

import java.util.Optional;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialService {

	
	
	
	/*
	 * private final JdbcClient jdbcClient;
	 * 
	 * public UserCredentialService(DataSource dataSource) { this.jdbcClient =
	 * JdbcClient.create(dataSource); }
	 */

   
	/*
	 * public Optional<UserCredentials> findByUsername(String username) { return
	 * jdbcClient.sql(""" SELECT username, password FROM users WHERE username =
	 * :username """) .param("username", username) .query((resultSet, rowNumber) ->
	 * new UserCredentials( resultSet.getString("username"),
	 * resultSet.getString("password") ) ) .optional(); }
	 */
}