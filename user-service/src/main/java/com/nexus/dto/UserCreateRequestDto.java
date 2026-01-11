package com.nexus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {
    @NotBlank(message = "Name required")
        private String name;
    @NotBlank(message = "Email required")
    @Email(message = "Valid email required")
        private String email;
    @NotBlank(message = "Password required")
    @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;
        private String phone;
}
