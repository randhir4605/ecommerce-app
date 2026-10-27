package com.example.auth.entity.projection;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.rest.core.config.Projection;

import com.example.auth.entity.User;

@Projection(name = "userDetail", types = User.class)
public interface UserDetailProjection {

    Long getId();
    String getLastLoginAt();
    Instant getCreatedAt();
    Instant getUpdatedAt();
    AccountStatusSummary getAccountStatus();

    UserProfileSummary getProfile();

    List<UserAddressSummary> getAddresses();

    List<UserRoleSummary> getUserRoles();

    interface AccountStatusSummary {
        String getCode();
        String getDescription();
    }

    interface UserProfileSummary {
        Long getId();
        String getFirstName();
        String getLastName();
        String getGender();
        LocalDate getDob();
        String getPhone();
        String getEmail();
    }

    interface UserAddressSummary {
        Long getId();
        String getAddressLabel();
        String getRecipientName();
        String getContactPhone();
        String getAddressLine1();
        String getAddressLine2();
        String getLandmark();
        String getLocality();
        String getCity();
        String getDistrict();
        String getStateRegion();
        String getPostalCode();
        String getCountryCode();
        String getDeliveryInstructions();
    }

    interface UserRoleSummary {
        Long getId();
        RoleSummary getRole();
    }

    interface RoleSummary {
        Long getId();
        String getRoleName();
    }
}