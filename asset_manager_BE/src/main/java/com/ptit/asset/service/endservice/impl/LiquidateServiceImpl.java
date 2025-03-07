package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.Liquidate;
import com.ptit.asset.dto.request.LiquidateChangeStatusRequestDTO;
import com.ptit.asset.dto.request.LiquidateCreateRequestDTO;
import com.ptit.asset.dto.request.LiquidateUpdateRequestDTO;
import com.ptit.asset.service.endservice.LiquidateService;
import com.ptit.asset.service.manager.LiquidateManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiquidateServiceImpl implements LiquidateService {

    @Autowired
    private LiquidateManagement liquidateManagement;


    @Override
    public Try<Liquidate> getOne(Long id) {
        return liquidateManagement.getOne(id);
    }

    @Override
    public Try<Liquidate> create(LiquidateCreateRequestDTO dto) {
        return liquidateManagement.create(dto);
    }

    @Override
    public Try<Liquidate> update(LiquidateUpdateRequestDTO dto) {
        return liquidateManagement.update(dto);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return liquidateManagement.delete(id);
    }

    @Override
    public List<Liquidate> fetchAll() {
        return liquidateManagement.fetchAll();
    }

    @Override
    public Try<Boolean> changeStatus(LiquidateChangeStatusRequestDTO dto) {
        return liquidateManagement.changeStatus(dto);
    }
}
