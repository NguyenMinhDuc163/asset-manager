package com.ptit.asset.dto.request;

import com.ptit.asset.domain.enumeration.ProductType;
import com.ptit.asset.domain.enumeration.TimeAllocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDTO {

    @NotNull @NotBlank(message = "name is mandatory") @Size(min = 2, max = 200)
    private String name;
    private String description;
    private String origin;
    @NotNull
    private ProductType type;
    @NotNull
    private TimeAllocationType timeAllocationType;
    @NotNull
    private int allocationDuration;

    @NotNull // require because this for action create
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        @NotNull
        private Long categoryId;
        @NotNull
        private Long calculationUnitId;
    }

}
