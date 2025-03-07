package com.ptit.asset.dto.response;

import com.ptit.asset.domain.Material;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class MaterialResponseDTO extends Material {

    private ExtendedInfo extendedInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExtendedInfo {
        private BigDecimal priceOrigin;
        private BigDecimal currentValue;
    }

}
