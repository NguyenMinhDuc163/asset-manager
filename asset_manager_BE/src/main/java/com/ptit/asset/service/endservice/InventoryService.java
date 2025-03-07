package com.ptit.asset.service.endservice;

import com.ptit.asset.domain.Inventory;
import com.ptit.asset.dto.request.InventoryChangeStatusRequestDTO;
import com.ptit.asset.dto.request.InventoryCreateRequestDTO;
import io.vavr.control.Try;

import java.util.List;

public interface InventoryService {

    Try<Inventory> getOne(Long id);

    Try<Inventory> create(InventoryCreateRequestDTO dto);

    Try<Boolean> delete(Long id);

    List<Inventory> fetchAll();

    Try<Boolean> changeStatus(InventoryChangeStatusRequestDTO dto);

}
