package com.example.authserver.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.authserver.repository.UserCredentialsRepository;
import com.example.authserver.repository.UserProfileRepository;

@Service
@Transactional(readOnly = true)
public class OidcUserInfoService {

    private final UserCredentialsRepository credentialsRepository;
    private final UserProfileRepository profileRepository;

    public OidcUserInfoService(UserCredentialsRepository credentialsRepository,
                               UserProfileRepository profileRepository) {
        this.credentialsRepository = credentialsRepository;
        this.profileRepository = profileRepository;
    }

    public OidcUserInfo loadUser(String username, Set<String> scopes) {
        var credentials = credentialsRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        var profile = profileRepository.findByUser_Id(credentials.getUser().getId()).orElse(null);

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("sub", username);

        if (scopes.contains(OidcScopes.PROFILE)) {
            putIfPresent(claims, "preferred_username", username);

            if (profile != null) {
                String fullName = Stream.of(profile.getFirstName(), profile.getLastName())
                        .filter(value -> value != null && !value.isBlank())
                        .collect(Collectors.joining(" "));

                putIfPresent(claims, "name", fullName);
                putIfPresent(claims, "given_name", profile.getFirstName());
                putIfPresent(claims, "family_name", profile.getLastName());
                putIfPresent(claims, "gender", profile.getGender());

                if (profile.getDob() != null) {
                    claims.put("birthdate", profile.getDob().toString());
                }
            }
        }

        if (profile != null && scopes.contains(OidcScopes.EMAIL)) {
            putIfPresent(claims, "email", profile.getEmail());
        }

        if (profile != null && scopes.contains(OidcScopes.PHONE)) {
            putIfPresent(claims, "phone_number", profile.getPhone());
        }

        return new OidcUserInfo(claims);
    }

    private void putIfPresent(Map<String, Object> claims, String key, String value) {
        if (value != null && !value.isBlank()) {
            claims.put(key, value);
        }
    }
}