package com.ptit.asset.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private Long id;// add more

    private String fullName;
    private String phone;
    private String email;

    private String username;
    private String token;
    private List<String> roles;
}
