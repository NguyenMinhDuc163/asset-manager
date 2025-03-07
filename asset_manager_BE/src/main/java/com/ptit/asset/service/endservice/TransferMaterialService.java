package com.ptit.asset.service.endservice;

import com.ptit.asset.domain.TransferMaterial;
import com.ptit.asset.dto.request.TransferMaterialCreateRequestDTO;
import io.vavr.control.Try;

import java.util.List;

public interface TransferMaterialService {

    Try<TransferMaterial> getOne(Long id);

    Try<TransferMaterial> create(TransferMaterialCreateRequestDTO dto);

    Try<Boolean> delete(Long id);

    List<TransferMaterial> fetchAll();

}
