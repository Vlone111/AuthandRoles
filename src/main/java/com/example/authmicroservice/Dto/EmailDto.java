package com.example.authmicroservice.Dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.Email;
@Data
public class EmailDto {
    @NotBlank(message = "Email не может быть пустым")
    @Email
    public String email;

}
