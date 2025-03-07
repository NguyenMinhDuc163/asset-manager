package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotNull @NotBlank(message = "fullName can not empty") @Size(min = 3, max = 50)
    @NotEmpty(message = "fullName not be empty")
    private String fullName;

    @NotNull @NotBlank(message = "phone can not empty") @Size(min = 3, max = 50)
    @NotEmpty(message = "phone not be empty")
    private String phone;

    @Email @NotNull @NotBlank(message = "email can not empty")
    @NotEmpty(message = "email not be empty")
    private String email;

    @NotNull @NotBlank(message = "username can not empty") @Size(min = 3, max = 50)
    @NotEmpty(message = "email not be empty")
    private String username;

    @NotNull @NotBlank(message = "password can not empty")
    @NotEmpty(message = "password not be empty")
    private String password;

    @NotNull
    private Boolean active;

    @NotNull
    @NotEmpty(message = "roles not be empty")
    private Set<String> roles;

    @NotNull // require because this for action create
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
//        @NotNull
        private Long departmentId;
    }

}
