package com.ptit.asset.service.endservice;

import com.ptit.asset.domain.Additional;
import com.ptit.asset.dto.request.AdditionalChangeStatusRequestDTO;
import com.ptit.asset.dto.request.AdditionalCreateRequestDTO;
import com.ptit.asset.dto.request.AdditionalUpdateRequestDTO;
import io.vavr.control.Try;

import java.util.List;

public interface AdditionalService {

    Try<Additional> getOne(Long id);

    Try<Additional> create(AdditionalCreateRequestDTO dto);

    Try<Additional> update(AdditionalUpdateRequestDTO dto);

    Try<Boolean> delete(Long id);

    List<Additional> fetchAll();

    Try<Boolean> changeStatus(AdditionalChangeStatusRequestDTO dto);

}
