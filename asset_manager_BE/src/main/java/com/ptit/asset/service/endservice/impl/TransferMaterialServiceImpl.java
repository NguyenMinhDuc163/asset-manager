package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.TransferMaterial;
import com.ptit.asset.dto.request.TransferMaterialCreateRequestDTO;
import com.ptit.asset.service.endservice.TransferMaterialService;
import com.ptit.asset.service.manager.TransferMaterialManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferMaterialServiceImpl implements TransferMaterialService {

    @Autowired
    private TransferMaterialManagement transferMaterialManagement;

    @Override
    public Try<TransferMaterial> getOne(Long id) {
        return transferMaterialManagement.getOne(id);
    }

    @Override
    public Try<TransferMaterial> create(TransferMaterialCreateRequestDTO dto) {
        return transferMaterialManagement.create(dto);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return transferMaterialManagement.delete(id);
    }

    @Override
    public List<TransferMaterial> fetchAll() {
        return transferMaterialManagement.fetchAll();
    }
}
