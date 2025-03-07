package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Liquidate;
import com.ptit.asset.domain.LiquidateMaterial;
import com.ptit.asset.domain.Material;
import com.ptit.asset.domain.enumeration.MaterialStatus;
import com.ptit.asset.dto.request.LiquidateChangeStatusRequestDTO;
import com.ptit.asset.dto.request.LiquidateCreateRequestDTO;
import com.ptit.asset.dto.request.LiquidateUpdateRequestDTO;
import com.ptit.asset.repository.LiquidateMaterialRepository;
import com.ptit.asset.repository.LiquidateRepository;
import com.ptit.asset.repository.MaterialRepository;
import com.ptit.asset.repository.UserRepository;
import com.ptit.asset.service.manager.LiquidateManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiquidateManagementImpl implements LiquidateManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private LiquidateRepository liquidateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private LiquidateMaterialRepository liquidateMaterialRepository;


    @Override
    public Try<Liquidate> getOne(Long id) {
        return Try.of(() -> liquidateRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get liquidate by id: "+id)));
    }

    @Override
    public Try<Liquidate> create(LiquidateCreateRequestDTO dto) {
        val user = userRepository.findById(dto.getEmbedded().getUserId());
        if (!user.isPresent()){
            return Try.failure(new Exception("Failure when find user by id: "+dto.getEmbedded().getUserId()));
        }

        String confirm = validationData();
        if (!confirm.equalsIgnoreCase("ACCEPTABLE")){
            return Try.failure(new Exception(confirm));
        }

        val liquidate = centralMapper.toLiquidate(dto, user.get());
        liquidate.setDone(false);

        return Try.of(() -> liquidateRepository.save(liquidate))
            .orElse(() -> Try.failure(new Exception("Failure when save liquidate")));
    }

    @Override
    public Try<Liquidate> update(LiquidateUpdateRequestDTO dto) {
        val liquidateResult = liquidateRepository.findById(dto.getId());
        if (!liquidateResult.isPresent()){
            return Try.failure(new Exception("Failure when find liquidate to update with id: "+dto.getId()));
        }

        val liquidateUpdate = centralMapper.toLiquidateUpdate(liquidateResult.get(),dto);

        // relationship space
        if (dto.getEmbedded() != null){
            val userId =dto.getEmbedded().getUserId();
            if (userId != null && !userId.equals(liquidateUpdate.getUser().getId())){
                val user = userRepository.findById(userId);
                user.ifPresent(liquidateUpdate::setUser);
            }
        }
        // relationship space

        return Try.of(() -> liquidateRepository.save(liquidateUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update liquidate")));

    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            liquidateRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete liquidate with id: "+id)));
    }

    @Override
    public List<Liquidate> fetchAll() {
        return liquidateRepository.findAll();
    }

    @Override
    public Try<Boolean> changeStatus(LiquidateChangeStatusRequestDTO dto) {
        val liquidate = liquidateRepository.findById(dto.getId());
        if (!liquidate.isPresent()){
            return Try.failure(new Exception("Failure when find liquidate with id:"+dto.getId()));
        }

        Liquidate liquidateUpdate = liquidate.get();
        // logic space
        if(dto.getStatus().equals(liquidateUpdate.getDone())){
            return Try.failure(new Exception("Nothing change with current status"));
        }
        if (dto.getStatus() && !liquidateUpdate.getDone()){// done liquidate procedure

            //todo: set status of all material belong this liquidate become NO-LONGER when procedure of liquidate is done
            List<LiquidateMaterial> liquidateMaterialList = liquidateMaterialRepository.findByLiquidateId(liquidate.get().getId());
            for (LiquidateMaterial element : liquidateMaterialList){
                Material materialUpdate = element.getMaterial();
                materialUpdate.setStatus(MaterialStatus.NO_LONGER);
                materialRepository.save(materialUpdate);
            }

            liquidateUpdate.setDone(dto.getStatus());
            return Try.of(() -> {
                liquidateRepository.save(liquidateUpdate);
                return true;
            }).orElse(() -> Try.failure(new Exception("Failure when change status of liquidate")));
        }
        // logic space
        return Try.failure(new Exception("Something wrong with business rule"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected String validationData(){
        List<Liquidate> liquidateList = liquidateRepository.findAll();
        for (Liquidate liquidate : liquidateList){
            if (!liquidate.getDone()){
                return "Already existing liquidate not done";
            }
        }
        return "ACCEPTABLE";
    }

}
