package com.ptit.asset.dto.request;

import com.ptit.asset.domain.enumeration.MaterialStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialCreateRequestDTO {

    @NotNull @NotBlank(message = "credential code is mandatory") @Size(min = 5)
    private String credentialCode;
    // qrCode generated from external
    @NotNull
    private MaterialStatus status;
    @NotNull
    private Instant timeStartDepreciation;
    // opt
    private Boolean haveInclude;
    @Size(min = 5)
    private String parentCode;

    @NotNull // require because this for action create
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        @NotNull
        private Long additionalId;
        @NotNull
        private Long productId;
        private Long placeId;
        private Long userId;
    }
}
