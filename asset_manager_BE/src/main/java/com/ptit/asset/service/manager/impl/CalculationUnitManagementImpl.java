package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.CalculationUnit;
import com.ptit.asset.dto.request.CalculationUnitCreateRequestDTO;
import com.ptit.asset.repository.CalculationUnitRepository;
import com.ptit.asset.service.manager.CalculationUnitManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculationUnitManagementImpl implements CalculationUnitManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private CalculationUnitRepository calculationUnitRepository;


    @Override
    public Try<CalculationUnit> getOne(Long id) {
        return Try.of(() -> calculationUnitRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get calculation unit by id: "+id)));
    }

    @Override
    public Try<CalculationUnit> create(CalculationUnitCreateRequestDTO dto) {
        return Try.of(() -> {
            val calculationUnit = centralMapper.toCalculationUnit(dto);
            return calculationUnitRepository.save(calculationUnit);
        }).orElse(() -> Try.failure(new Exception("Failure when save calculation unit")));
    }

    @Override
    public Try<CalculationUnit> update(CalculationUnit calculationUnit) {
        val calculationUnitResult = calculationUnitRepository.findById(calculationUnit.getId());
        if (! calculationUnitResult.isPresent()){
            return Try.failure(new Exception("Failure when find calculation unit to update with id: "+calculationUnit.getId()));
        }
        val calculationUnitUpdate = centralMapper.toCalculationUnitUpdate(calculationUnitResult.get(), calculationUnit);
        return Try.of(() -> calculationUnitRepository.save(calculationUnitUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update calculation unit")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            calculationUnitRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete calculation unit with id: "+id)));
    }

    @Override
    public List<CalculationUnit> fetchAll() {
        return calculationUnitRepository.findAll();
    }
}
