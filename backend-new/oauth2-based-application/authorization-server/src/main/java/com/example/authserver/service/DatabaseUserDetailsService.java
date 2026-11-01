package com.example.authserver.service;
import com.example.app.entity.Role;
import com.example.app.entity.UserCredentials;
import com.example.app.entity.UserRole;
import com.example.authserver.repository.UserCredentialsRepository;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserCredentialsRepository repository;

    public DatabaseUserDetailsService(UserCredentialsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        UserCredentials credentials = repository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var user = credentials.getUser();
        Set<SimpleGrantedAuthority> authorities = new LinkedHashSet<>();

        for (UserRole userRole : user.getUserRoles()) {
            Role role = userRole.getRole();
            if (role == null || role.getRoleName() == null) continue;

            String roleName = role.getRoleName().toUpperCase(Locale.ROOT);
            if (!roleName.startsWith("ROLE_")) roleName = "ROLE_" + roleName;
            authorities.add(new SimpleGrantedAuthority(roleName));
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(credentials.getUsername())
                .password(credentials.getPasswordHash())
                .disabled(!"active".equalsIgnoreCase(user.getAccountStatus()))
                .authorities(authorities)
                .build();
    }
}
