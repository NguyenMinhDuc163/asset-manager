package com.ptit.asset.service.manager;

import com.ptit.asset.domain.Campus;
import com.ptit.asset.dto.request.CampusCreateRequestDTO;
import io.vavr.control.Try;

import java.util.List;

public interface CampusManagement {

    Try<Campus> getOne(Long id);

    Try<Campus> create(CampusCreateRequestDTO dto);

    Try<Campus> update(Campus campus);

    Try<Boolean> delete(Long id);

    List<Campus> fetchAll();

}
