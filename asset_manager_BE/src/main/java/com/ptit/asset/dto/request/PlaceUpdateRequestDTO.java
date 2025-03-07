package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceUpdateRequestDTO {

    @NotNull
    private Long id;

    @Size(min = 3, max = 20)
    private String code;

    private String nameSpecification;
    private String description;
    private int floor;
    private String direction;

    // @NotNull no require because can update without relative object
    private Embedded embedded;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Embedded {

        private Long typePlaceId;
        private Long campusId;
        private Long departmentId;
    }
}
