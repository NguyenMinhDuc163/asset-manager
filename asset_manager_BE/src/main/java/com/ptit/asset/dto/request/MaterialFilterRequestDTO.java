package com.ptit.asset.dto.request;

import com.ptit.asset.domain.enumeration.MaterialStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialFilterRequestDTO {

    private MaterialStatus materialStatus;
    private Long categoryId;
    private Long placeId;
    private Long departmentId;
    private Long productId;

}
