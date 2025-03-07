package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotNull @NotBlank(message = "username can not be empty")
    @Size(min = 3, max = 50)
    @NotEmpty(message = "username not be empty")
    private String username;

    @NotNull @NotBlank(message = "password can not be empty")
    @Size(min = 6, max = 100)
    @NotEmpty(message = "password not be empty")
    private String password;
}
