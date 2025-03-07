package com.ptit.asset.service.endservice;

import com.ptit.asset.domain.Liquidate;
import com.ptit.asset.dto.request.LiquidateChangeStatusRequestDTO;
import com.ptit.asset.dto.request.LiquidateCreateRequestDTO;
import com.ptit.asset.dto.request.LiquidateUpdateRequestDTO;
import io.vavr.control.Try;

import java.util.List;

public interface LiquidateService {

    Try<Liquidate> getOne(Long id);

    Try<Liquidate> create(LiquidateCreateRequestDTO dto);

    Try<Liquidate> update(LiquidateUpdateRequestDTO dto);

    Try<Boolean> delete(Long id);

    List<Liquidate> fetchAll();

    Try<Boolean> changeStatus(LiquidateChangeStatusRequestDTO dto);

}
