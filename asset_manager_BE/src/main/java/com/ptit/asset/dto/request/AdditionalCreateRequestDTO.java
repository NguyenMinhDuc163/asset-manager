package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalCreateRequestDTO {

    @NotNull
    private Instant time;

    @NotNull // require because this for action create
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        @NotNull
        private Long userId;
        @NotNull
        private Long organizationId;
    }

}
