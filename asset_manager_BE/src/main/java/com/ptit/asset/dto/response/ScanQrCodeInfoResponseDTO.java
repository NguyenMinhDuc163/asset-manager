package com.ptit.asset.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScanQrCodeInfoResponseDTO {

    private String credentialCode;
    private String nameDetail;
    private String status;
    private String currentPlace;
    // todo: for detail information
    private String message;

}
