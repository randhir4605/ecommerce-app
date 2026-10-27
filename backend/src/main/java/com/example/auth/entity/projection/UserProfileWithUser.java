package com.example.auth.entity.projection;

import java.time.Instant;
import java.time.LocalDate;

import org.springframework.data.rest.core.config.Projection;

import com.example.auth.entity.UserProfile;

@Projection(name = "profileWithUser", types = UserProfile.class)
public interface UserProfileWithUser {
    Long getId();
    String getFirstName();
    String getLastName();
    String getGender();
    LocalDate getDob();
    String getPhone();
    String getEmail();
    Instant getCreatedAt();
    Instant getUpdatedAt();

    UserSummary getUser();

    interface UserSummary {
        Long getId();
        Instant getLastLoginAt();
        Instant getCreatedAt();
        Instant getUpdatedAt();

        AccountStatusSummary getAccountStatus();

        interface AccountStatusSummary {
            String getCode();
            String getDescription();
        }
    }
}
