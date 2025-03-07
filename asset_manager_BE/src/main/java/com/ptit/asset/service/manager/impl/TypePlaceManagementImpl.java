package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.TypePlace;
import com.ptit.asset.dto.request.TypePlaceCreateRequestDTO;
import com.ptit.asset.repository.TypePlaceRepository;
import com.ptit.asset.service.manager.TypePlaceManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypePlaceManagementImpl implements TypePlaceManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private TypePlaceRepository typePlaceRepository;


    @Override
    public Try<TypePlace> getOne(Long id) {
        return Try.of(() -> typePlaceRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get type place by id: "+id)));
    }

    @Override
    public Try<TypePlace> create(TypePlaceCreateRequestDTO dto) {
        return Try.of(() -> {
            val typePlace = centralMapper.toTypePlace(dto);
            return typePlaceRepository.save(typePlace);
        }).orElse(() -> Try.failure(new Exception("Failure when save type place")));
    }

    @Override
    public Try<TypePlace> update(TypePlace typePlace) {
        val typePlaceResult = typePlaceRepository.findById(typePlace.getId());
        if (!typePlaceResult.isPresent()){
            return Try.failure(new Exception("Failure when find type place to update with id: "+typePlace.getId()));
        }
        val typePlaceUpdate = centralMapper.toTypePlaceUpdate(typePlaceResult.get(),typePlace);
        return Try.of(() -> typePlaceRepository.save(typePlaceUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update type place")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            typePlaceRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete type place with id: "+id)));
    }

    @Override
    public List<TypePlace> fetchAll() {
        return typePlaceRepository.findAll();
    }
}
