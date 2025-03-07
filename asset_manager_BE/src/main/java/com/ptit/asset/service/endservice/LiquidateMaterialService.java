package com.ptit.asset.service.endservice;

import com.ptit.asset.domain.LiquidateMaterial;
import com.ptit.asset.dto.request.LiquidateMaterialCreateRequestDTO;
import io.vavr.control.Try;

import java.util.List;

public interface LiquidateMaterialService {

    Try<LiquidateMaterial> getOne(Long id);

    Try<LiquidateMaterial> create(LiquidateMaterialCreateRequestDTO dto);

    Try<Boolean> delete(Long id);

    List<LiquidateMaterial> fetchAll();

}
