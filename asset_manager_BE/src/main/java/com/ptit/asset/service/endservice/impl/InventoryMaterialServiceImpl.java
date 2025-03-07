package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.InventoryMaterial;
import com.ptit.asset.dto.request.InventoryMaterialCreateRequestDTO;
import com.ptit.asset.dto.response.ScanQrCodeInfoResponseDTO;
import com.ptit.asset.service.endservice.InventoryMaterialService;
import com.ptit.asset.service.manager.InventoryMaterialManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryMaterialServiceImpl implements InventoryMaterialService {

    @Autowired
    private InventoryMaterialManagement inventoryMaterialManagement;

    @Override
    public Try<InventoryMaterial> getOne(Long id) {
        return inventoryMaterialManagement.getOne(id);
    }

//    @Override
//    public Try<InventoryMaterial> create(InventoryMaterialCreateRequestDTO dto) {
//        return inventoryMaterialManagement.create(dto);
//    }
@Override
public Try<ScanQrCodeInfoResponseDTO> create(InventoryMaterialCreateRequestDTO dto) {
    return inventoryMaterialManagement.create(dto);
}

    @Override
    public Try<Boolean> delete(Long id) {
        return inventoryMaterialManagement.delete(id);
    }

    @Override
    public List<InventoryMaterial> fetchAll() {
        return inventoryMaterialManagement.fetchAll();
    }

    @Override
    public List<InventoryMaterial> fetchByUser(Long userId) {
        return inventoryMaterialManagement.fetchByUser(userId);
    }
}
