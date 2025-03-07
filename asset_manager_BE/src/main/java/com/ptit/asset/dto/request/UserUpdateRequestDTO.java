package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {

    @NotNull
    private Long id;

    @NotNull @NotBlank(message = "fullName cant not empty") @Size(min = 3, max = 50)
    @NotEmpty(message = "fullName not be empty")
    private String fullName;

    @NotNull @NotBlank(message = "phone cant not empty") @Size(min = 3, max = 50)
    @NotEmpty(message = "phone not be empty")
    private String phone;

    @Email @NotNull @NotBlank(message = "email cant not empty")
    @NotEmpty(message = "email not be empty")
    private String email;

    @NotNull @NotBlank(message = "username cant not empty") @Size(min = 3, max = 50)
    @NotEmpty(message = "username not be empty")
    private String username;

    @NotNull @NotBlank(message = "password cant not empty")
    @NotEmpty(message = "password not be empty")
    private String password;

    @NotNull
    private Boolean active;

    @NotNull
    @NotEmpty(message = "roles not be empty")
    private Set<String> roles;

    // @NotNull no require because can update without relative object
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        private Long departmentId;
    }

}
