package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferMaterialCreateRequestDTO {

    @NotNull
    private Instant time;
    @Size(min = 2)
    private String reason;

    @NotNull
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
//        @NotNull
        private Long placeFromId;
        @NotNull
        private Long placeTargetId;
        @NotNull
        private Long materialId;
        @NotNull
        private Long userId;
    }

}
