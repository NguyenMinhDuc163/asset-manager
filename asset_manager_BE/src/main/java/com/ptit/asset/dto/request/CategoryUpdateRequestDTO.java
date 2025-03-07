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
public class CategoryUpdateRequestDTO {

    @NotNull
    private Long id;

    @NotNull
    @NotBlank(message = "name is mandatory") @Size(min = 2, max = 50)
    private String name;

    private String description;

    // @NotNull no require because can update without relative object
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        private Long groupId;
    }
}
