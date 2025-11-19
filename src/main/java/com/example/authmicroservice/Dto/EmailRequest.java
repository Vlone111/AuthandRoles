package com.example.authmicroservice.Dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.Email;

@Data
public class EmailRequest {
    @NotBlank(message = "Email не может быть пустым")
    @Email
    private String email;

}
