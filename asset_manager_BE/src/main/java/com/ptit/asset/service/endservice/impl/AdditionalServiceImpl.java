package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.Additional;
import com.ptit.asset.dto.request.AdditionalChangeStatusRequestDTO;
import com.ptit.asset.dto.request.AdditionalCreateRequestDTO;
import com.ptit.asset.dto.request.AdditionalUpdateRequestDTO;
import com.ptit.asset.service.endservice.AdditionalService;
import com.ptit.asset.service.manager.AdditionalManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionalServiceImpl implements AdditionalService {

    @Autowired
    private AdditionalManagement additionalManagement;

    @Override
    public Try<Additional> getOne(Long id) {
        return additionalManagement.getOne(id);
    }

    @Override
    public Try<Additional> create(AdditionalCreateRequestDTO dto) {
        return additionalManagement.create(dto);
    }

    @Override
    public Try<Additional> update(AdditionalUpdateRequestDTO dto) {
        return additionalManagement.update(dto);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return additionalManagement.delete(id);
    }

    @Override
    public List<Additional> fetchAll() {
        return additionalManagement.fetchAll();
    }

    @Override
    public Try<Boolean> changeStatus(AdditionalChangeStatusRequestDTO dto) {
        return additionalManagement.changeStatus(dto);
    }
}
