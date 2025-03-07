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
public class PlaceCreateRequestDTO {

    @NotNull @NotBlank(message = "code is mandatory") @Size(min = 3, max = 20)
    private String code;

    private String nameSpecification;
    private String description;
    private int floor;
    private String direction;

    @NotNull // require because this for action create
    private Embedded embedded;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Embedded {

        @NotNull
        private Long typePlaceId;
        @NotNull
        private Long campusId;

        private Long departmentId;
    }
}
