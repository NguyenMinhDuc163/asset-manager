package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMaterialCreateRequestDTO {

    @NotNull
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
//        @NotNull
//        private Long inventoryId;
        @NotNull
        private Long materialId;
        @NotNull
        private Long userId;
//        @NotNull
//        private Long placeId;
    }
}
