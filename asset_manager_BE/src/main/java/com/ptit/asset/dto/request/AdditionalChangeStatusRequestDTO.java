package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalChangeStatusRequestDTO {

    @NotNull
    private Long id;
    @NotNull
    private Boolean status;
}
