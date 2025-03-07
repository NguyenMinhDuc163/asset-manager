package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.LiquidateMaterial;
import com.ptit.asset.dto.request.LiquidateMaterialCreateRequestDTO;
import com.ptit.asset.service.endservice.LiquidateMaterialService;
import com.ptit.asset.service.manager.LiquidateMaterialManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiquidateMaterialServiceImpl implements LiquidateMaterialService {

    @Autowired
    private LiquidateMaterialManagement liquidateMaterialManagement;

    @Override
    public Try<LiquidateMaterial> getOne(Long id) {
        return liquidateMaterialManagement.getOne(id);
    }

    @Override
    public Try<LiquidateMaterial> create(LiquidateMaterialCreateRequestDTO dto) {
        return liquidateMaterialManagement.create(dto);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return liquidateMaterialManagement.delete(id);
    }

    @Override
    public List<LiquidateMaterial> fetchAll() {
        return liquidateMaterialManagement.fetchAll();
    }
}
