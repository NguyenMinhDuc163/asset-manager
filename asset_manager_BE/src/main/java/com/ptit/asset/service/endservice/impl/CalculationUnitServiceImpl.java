package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.CalculationUnit;
import com.ptit.asset.dto.request.CalculationUnitCreateRequestDTO;
import com.ptit.asset.service.endservice.CalculationUnitService;
import com.ptit.asset.service.manager.CalculationUnitManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculationUnitServiceImpl implements CalculationUnitService {

    @Autowired
    private CalculationUnitManagement calculationUnitManagement;

    @Override
    public Try<CalculationUnit> getOne(Long id) {
        return calculationUnitManagement.getOne(id);
    }

    @Override
    public Try<CalculationUnit> create(CalculationUnitCreateRequestDTO dto) {
        return calculationUnitManagement.create(dto);
    }

    @Override
    public Try<CalculationUnit> update(CalculationUnit calculationUnit) {
        return calculationUnitManagement.update(calculationUnit);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return calculationUnitManagement.delete(id);
    }

    @Override
    public List<CalculationUnit> fetchAll() {
        return calculationUnitManagement.fetchAll();
    }
}
