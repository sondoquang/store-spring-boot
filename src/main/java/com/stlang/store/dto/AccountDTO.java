package com.stlang.store.dto;

import com.stlang.store.domain.Authority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountDTO {

    @NotBlank(message = "Username is not empty!")
    @Size( min= 6 , max = 100)
    private String username;

    @Size( min= 6 , max = 100)
    @NotBlank(message = "FullName is not empty!")
    private String fullname;

    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email don't match format !")
    @NotBlank(message = "Email's account is not empty!")
    private String email;

    @NotBlank(message = "Photo's account is not empty!")
    private String photo;

    private String refreshToken;


}
