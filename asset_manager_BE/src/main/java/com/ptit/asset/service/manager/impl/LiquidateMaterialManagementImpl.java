package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.LiquidateMaterial;
import com.ptit.asset.domain.enumeration.MaterialStatus;
import com.ptit.asset.dto.request.LiquidateMaterialCreateRequestDTO;
import com.ptit.asset.repository.LiquidateMaterialRepository;
import com.ptit.asset.repository.LiquidateRepository;
import com.ptit.asset.repository.MaterialRepository;
import com.ptit.asset.service.manager.LiquidateMaterialManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiquidateMaterialManagementImpl implements LiquidateMaterialManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private LiquidateMaterialRepository liquidateMaterialRepository;
    @Autowired
    private LiquidateRepository liquidateRepository;
    @Autowired
    private MaterialRepository materialRepository;


    @Override
    public Try<LiquidateMaterial> getOne(Long id) {
        return Try.of(() -> liquidateMaterialRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when find liquidate material by id: "+id)));
    }

    @Override
    public Try<LiquidateMaterial> create(LiquidateMaterialCreateRequestDTO dto) {
        val liquidate = liquidateRepository.findById(dto.getEmbedded().getLiquidateId());
        if (!liquidate.isPresent()){
        return Try.failure(new Exception("Failure when find liquidate by id: "+dto.getEmbedded().getLiquidateId()));
        }

        // todo: Logic check if liquidate with status un_done => accept add material into liquidate and vice versa
        if (liquidate.get().getDone()) {
            return Try.failure(new Exception("This Liquidate is done (Add material into liquidate not valid now)"));
        }

        val material = materialRepository.findById(dto.getEmbedded().getMaterialId());
        if (!material.isPresent()){
            return Try.failure(new Exception("Failure when find material by id: "+dto.getEmbedded().getMaterialId()));
        }

        val liquidateMaterial = centralMapper.toLiquidateMaterial(liquidate.get(), material.get());

        val materialUpdate = material.get();
        return Try.of(() -> liquidateMaterialRepository.save(liquidateMaterial))
        .flatMap(liquidateMaterialSaved -> {
            materialUpdate.setStatus(MaterialStatus.REQUEST_LIQUIDATE);
            materialRepository.save(materialUpdate);
            return Try.of(() -> liquidateMaterialSaved);
        })
        .orElse(() -> Try.failure(new Exception("Failure when save liquidate material")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            liquidateMaterialRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete liquidate material by id: "+id)));
    }

    @Override
    public List<LiquidateMaterial> fetchAll() {
        return liquidateMaterialRepository.findAll();
    }
}
