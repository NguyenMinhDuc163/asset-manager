package com.ptit.asset.service.manager;

import com.ptit.asset.domain.TransferMaterial;
import com.ptit.asset.dto.request.TransferMaterialCreateRequestDTO;
import io.vavr.control.Try;

import java.util.List;

public interface TransferMaterialManagement {

    Try<TransferMaterial> getOne(Long id);

    Try<TransferMaterial> create(TransferMaterialCreateRequestDTO dto);

    Try<Boolean> delete(Long id);

    List<TransferMaterial> fetchAll();

}
