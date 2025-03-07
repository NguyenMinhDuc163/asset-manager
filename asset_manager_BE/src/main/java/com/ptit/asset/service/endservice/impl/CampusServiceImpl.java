package com.ptit.asset.service.endservice.impl;


import com.ptit.asset.domain.Campus;
import com.ptit.asset.dto.request.CampusCreateRequestDTO;
import com.ptit.asset.service.endservice.CampusService;

import com.ptit.asset.service.manager.CampusManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampusServiceImpl implements CampusService {

    @Autowired
    private CampusManagement campusManagement;

    @Override
    public Try<Campus> getOne(Long id) {
        return campusManagement.getOne(id);
    }

    @Override
    public Try<Campus> create(CampusCreateRequestDTO dto) {
        return campusManagement.create(dto);
    }

    @Override
    public Try<Campus> update(Campus campus) {
        return campusManagement.update(campus);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return campusManagement.delete(id);
    }

    @Override
    public List<Campus> fetchAll() {
        return campusManagement.fetchAll();
    }
}
