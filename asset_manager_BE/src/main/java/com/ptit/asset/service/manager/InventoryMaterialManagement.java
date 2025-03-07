package com.ptit.asset.service.manager;

import com.ptit.asset.domain.InventoryMaterial;
import com.ptit.asset.dto.request.InventoryMaterialCreateRequestDTO;
import com.ptit.asset.dto.response.ScanQrCodeInfoResponseDTO;
import io.vavr.control.Try;

import java.util.List;

public interface InventoryMaterialManagement {

    Try<InventoryMaterial> getOne(Long id);

//    Try<InventoryMaterial> create(InventoryMaterialCreateRequestDTO dto);
    Try<ScanQrCodeInfoResponseDTO> create(InventoryMaterialCreateRequestDTO dto);

    Try<Boolean> delete(Long id);

    List<InventoryMaterial> fetchAll();

    List<InventoryMaterial> fetchByUser(Long userId);

}
