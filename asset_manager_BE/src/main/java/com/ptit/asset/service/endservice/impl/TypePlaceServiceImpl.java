package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.TypePlace;
import com.ptit.asset.dto.request.TypePlaceCreateRequestDTO;
import com.ptit.asset.service.endservice.TypePlaceService;
import com.ptit.asset.service.manager.TypePlaceManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypePlaceServiceImpl implements TypePlaceService {

    @Autowired
    private TypePlaceManagement typePlaceManagement;

    @Override
    public Try<TypePlace> getOne(Long id) {
        return typePlaceManagement.getOne(id);
    }

    @Override
    public Try<TypePlace> create(TypePlaceCreateRequestDTO dto) {
        return typePlaceManagement.create(dto);
    }

    @Override
    public Try<TypePlace> update(TypePlace typePlace) {
        return typePlaceManagement.update(typePlace);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return typePlaceManagement.delete(id);
    }

    @Override
    public List<TypePlace> fetchAll() {
        return typePlaceManagement.fetchAll();
    }
}
