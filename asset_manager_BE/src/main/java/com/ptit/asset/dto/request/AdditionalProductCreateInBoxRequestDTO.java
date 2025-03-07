package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalProductCreateInBoxRequestDTO {

    @NotNull
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {

        @NotNull
        private Long additionalId;
        @NotNull
        private List<Record> recordList;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Record {
            @NotNull
            private Long productId;
            @NotNull
            private Double price;
            @NotNull
            private Set<String> listMaterialCode;
        }
    }

}
