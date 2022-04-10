package com.example.family.security;

import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Size;

@Data
public class RegistrationForm {
    @NotNull
    @Size(min = 3, message = "")
    private String username;

    @NotNull
    @Size(min = 4, message = "")
    private String password;

    @NotNull
    @Size(min = 4, message = "")
    private String checkpassword;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(
                username, passwordEncoder.encode(password));
    }

}
