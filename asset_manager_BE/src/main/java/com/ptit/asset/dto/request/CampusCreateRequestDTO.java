package com.ptit.asset.dto.request;

import com.ptit.asset.domain.enumeration.CampusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampusCreateRequestDTO {

    @NotNull @NotBlank(message = "name is mandatory") @Size(min = 2, max = 15)
    private String name;

    private String description;

    @NotNull @NotBlank(message = "contact phone is mandatory")
    private String contactPhone;

    @Email @NotNull @NotBlank(message = "contact email is mandatory")
    private String contactEmail;

    @NotNull
    private CampusType campusType;

    private String location;

    private String mapUrl;
}
