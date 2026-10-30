package com.example;

import com.example.app.entity.Permission;
import com.example.app.entity.Role;
import com.example.app.entity.RolePermission;
import com.example.app.entity.User;
import com.example.app.entity.UserCredentials;
import com.example.app.entity.UserRole;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserDetailsService implements UserDetailsService {

	private final UserCredentialsRepository repository;

	public AppUserDetailsService(UserCredentialsRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {

		UserCredentials credentials = repository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		User user = credentials.getUser();
		Set<GrantedAuthority> authorities = getAuthorities(user);
		boolean active = "active".equalsIgnoreCase(user.getAccountStatus());
		return org.springframework.security.core.userdetails.User.withUsername(credentials.getUsername())
				.password(credentials.getPasswordHash()).disabled(!active).authorities(authorities).build();
	}

	private Set<GrantedAuthority> getAuthorities(User user) {
		Set<GrantedAuthority> authorities = new LinkedHashSet<>();
		for (UserRole userRole : user.getUserRoles()) {
			Role role = userRole.getRole();
			if (role == null) {
				continue;
			}
			String roleName = role.getRoleName();
			if (roleName != null && !roleName.isBlank()) {
				if (!roleName.startsWith("ROLE_")) {
					roleName = "ROLE_" + roleName;
				}
				authorities.add(new SimpleGrantedAuthority(roleName));
			}
			for (RolePermission rolePermission : role.getRolePermissions()) {
				Permission permission = rolePermission.getPermission();
				if (permission != null && permission.getPermissionName() != null) {
					authorities.add(new SimpleGrantedAuthority(permission.getPermissionName()));
				}
			}
		}

		return authorities;
	}
}