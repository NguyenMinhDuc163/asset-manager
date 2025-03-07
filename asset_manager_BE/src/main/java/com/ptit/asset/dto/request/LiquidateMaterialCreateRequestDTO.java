package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiquidateMaterialCreateRequestDTO {

    @NotNull
    private Embedded embedded; // require because this for action create

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        @NotNull
        private Long liquidateId;
        @NotNull
        private Long materialId;
    }
}
