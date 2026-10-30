package com.example;

import com.example.app.entity.UserCredentials;
import com.example.app.entity.UserProfile;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrentUserService {

    private final UserCredentialsRepository credentialsRepository;
    private final UserProfileRepository profileRepository;

    public CurrentUserService(UserCredentialsRepository credentialsRepository, UserProfileRepository profileRepository) {
        this.credentialsRepository = credentialsRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse getCurrentUser(String username) {
        UserCredentials credentials = credentialsRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long userId = credentials.getUser().getId();

        UserProfile profile = profileRepository.findByUser_Id(userId).orElse(null);

        CurrentUserResponse.Profile profileResponse = profile == null ? null :
                new CurrentUserResponse.Profile(
                        profile.getFirstName(),
                        profile.getLastName(),
                        profile.getGender(),
                        profile.getDob(),
                        profile.getPhone(),
                        profile.getEmail());

        return new CurrentUserResponse(
                userId,
                credentials.getUsername(),
                credentials.getUser().getAccountStatus(),
                profileResponse);
    }
}