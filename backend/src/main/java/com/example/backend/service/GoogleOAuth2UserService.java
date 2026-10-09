package com.example.backend.service;

import com.example.backend.entity.AppUser;
import com.example.backend.repository.AppUserRepository;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoogleOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate =
            new DefaultOAuth2UserService();

    private final AppUserRepository appUserRepository;

    public GoogleOAuth2UserService(
            AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        OAuth2User googleUser = delegate.loadUser(request);

        String email = googleUser.getAttribute("email");
        String googleSubject = googleUser.getAttribute("sub");

        if (email == null || googleSubject == null) {
            throw new OAuth2AuthenticationException(
                    "Google did not provide the required user identity");
        }

        Boolean verified =
                googleUser.getAttribute("email_verified");

        if (!Boolean.TRUE.equals(verified)) {
            throw new OAuth2AuthenticationException(
                    "Google email is not verified");
        }

        String name = googleUser.getAttribute("name");
        String picture = googleUser.getAttribute("picture");

        AppUser user = appUserRepository.findByEmail(email)
                .orElseGet(AppUser::new);

        user.setEmail(email);
        user.setDisplayName(name);
        user.setAvatarUrl(picture);
        user.setEmailVerified(true);

        appUserRepository.save(user);

        return googleUser;
    }
}