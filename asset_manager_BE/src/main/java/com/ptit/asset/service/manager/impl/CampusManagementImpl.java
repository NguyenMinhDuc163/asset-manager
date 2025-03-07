package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Campus;
import com.ptit.asset.dto.request.CampusCreateRequestDTO;
import com.ptit.asset.repository.CampusRepository;
import com.ptit.asset.service.manager.CampusManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampusManagementImpl implements CampusManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private CampusRepository campusRepository;


    @Override
    public Try<Campus> getOne(Long id) {
        return Try.of(() -> campusRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get campus by id: "+id)));
    }

    @Override
    public Try<Campus> create(CampusCreateRequestDTO dto) {
        return Try.of(() -> {
            val unit = centralMapper.toCampus(dto);
            return campusRepository.save(unit);
        }).orElse(() -> Try.failure(new Exception("Failure when save campus")));
    }

    @Override
    public Try<Campus> update(Campus campus) {
        val campusResult = campusRepository.findById(campus.getId());
        if (!campusResult.isPresent()){
            return Try.failure(new Exception("Failure when find campus to update with id: "+campus.getId()));
        }
        val campusUpdate = centralMapper.toCampusUpdate(campusResult.get(),campus);
        return Try.of(() -> campusRepository.save(campusUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update campus")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            campusRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete campus with id: "+id)));
    }

    @Override
    public List<Campus> fetchAll() {
        return campusRepository.findAll();
    }
}
