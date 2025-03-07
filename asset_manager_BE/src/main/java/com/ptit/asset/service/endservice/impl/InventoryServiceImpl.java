package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.Inventory;
import com.ptit.asset.dto.request.InventoryChangeStatusRequestDTO;
import com.ptit.asset.dto.request.InventoryCreateRequestDTO;
import com.ptit.asset.service.endservice.InventoryService;
import com.ptit.asset.service.manager.InventoryManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryManagement inventoryManagement;

    @Override
    public Try<Inventory> getOne(Long id) {
        return inventoryManagement.getOne(id);
    }

    @Override
    public Try<Inventory> create(InventoryCreateRequestDTO dto) {
        return inventoryManagement.create(dto);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return inventoryManagement.delete(id);
    }

    @Override
    public List<Inventory> fetchAll() {
        return inventoryManagement.fetchAll();
    }

    @Override
    public Try<Boolean> changeStatus(InventoryChangeStatusRequestDTO dto) {
        return inventoryManagement.changeStatus(dto);
    }
}
