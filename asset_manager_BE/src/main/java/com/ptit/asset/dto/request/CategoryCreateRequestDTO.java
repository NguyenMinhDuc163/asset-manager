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
public class CategoryCreateRequestDTO {

    @NotNull @NotBlank(message = "name is mandatory") @Size(min = 2, max = 50)
    private String name;

    private String description;

    @NotNull // require because this for action create
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        @NotNull
        private Long groupId;
    }
}
