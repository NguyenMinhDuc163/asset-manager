package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiquidateUpdateRequestDTO {

    @NotNull
    private Long id;

    @NotNull
    private Instant time;

    // @NotNull no require because can update without relative object
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        private Long userId;
    }
}
