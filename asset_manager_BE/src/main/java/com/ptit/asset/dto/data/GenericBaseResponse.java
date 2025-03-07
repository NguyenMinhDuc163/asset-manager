package com.ptit.asset.dto.data;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericBaseResponse <T> {

    T data;
    Meta meta;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private String status;
        private Integer code;
        private String message;
    }
}
