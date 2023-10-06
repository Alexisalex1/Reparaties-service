package nl.backend.reparatieservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDto {
    @NotBlank
    @Size(min = 5, max = 20)
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    public String username;
    @NotBlank
    @Size(min = 6, max = 20)
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    public String password;
}
