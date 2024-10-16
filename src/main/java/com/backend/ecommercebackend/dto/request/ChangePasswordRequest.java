package com.backend.ecommercebackend.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {

    @NotBlank(message = "New password is required")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d_!@#$%^&*()+=-]{8,}$",
            message = "Password must be at least 8 characters long, contain at least one letter and one digit"
    )
     String newPassword;

    @NotBlank(message = "Confirm password is required")
    String confirmPassword;
}
