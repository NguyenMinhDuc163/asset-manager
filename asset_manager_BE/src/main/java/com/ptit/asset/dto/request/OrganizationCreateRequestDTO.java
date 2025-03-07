package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationCreateRequestDTO {

    @NotNull @NotBlank(message = "name is mandatory") @Size(min = 2, max = 15)
    private String name;

    private String contact;
}
