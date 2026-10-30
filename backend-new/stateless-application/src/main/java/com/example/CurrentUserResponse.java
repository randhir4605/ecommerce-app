package com.example;

import java.time.LocalDate;

public record CurrentUserResponse(
        Long id,
        String username,
        String accountStatus,
        Profile profile) {

    public record Profile(
            String firstName,
            String lastName,
            String gender,
            LocalDate dob,
            String phone,
            String email) {}
}