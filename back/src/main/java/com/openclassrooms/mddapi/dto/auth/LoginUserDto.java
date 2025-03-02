package com.openclassrooms.mddapi.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginUserDto {
    @NotBlank(message = "Le nom d'utilisateur ou l'email est requis")
    private String usernameOrEmail;

    @NotBlank(message = "Le mot de passe est requis")
    private String password;

    public String getUsernameOrEmail() { return usernameOrEmail; }
    public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
