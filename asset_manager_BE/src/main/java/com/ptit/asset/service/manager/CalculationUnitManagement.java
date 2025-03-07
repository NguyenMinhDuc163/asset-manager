package com.ptit.asset.service.manager;

import com.ptit.asset.domain.CalculationUnit;
import com.ptit.asset.dto.request.CalculationUnitCreateRequestDTO;
import io.vavr.control.Try;

import java.util.List;

public interface CalculationUnitManagement {

    Try<CalculationUnit> getOne(Long id);

    Try<CalculationUnit> create(CalculationUnitCreateRequestDTO dto);

    Try<CalculationUnit> update(CalculationUnit calculationUnit);

    Try<Boolean> delete(Long id);

    List<CalculationUnit> fetchAll();
}
