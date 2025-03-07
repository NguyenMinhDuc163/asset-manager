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
public class GroupCreateRequestDTO {

    @NotNull
    @NotBlank(message = "code is mandatory") @Size(min = 2, max = 50)
    private String code;

    private String description;
}
