package com.example.authmicroservice.Dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailConfirmRequest {
    @Email
    private String email;

    @NotBlank(message = "Код подтверждения не может быть пустым")
    @NotEmpty
    private String otp;
}
