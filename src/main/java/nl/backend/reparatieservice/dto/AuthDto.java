package nl.backend.reparatieservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDto {
    @NotBlank
    public String username;
    @NotBlank
    @Size(min = 6, max = 20)
    public String password;
}
