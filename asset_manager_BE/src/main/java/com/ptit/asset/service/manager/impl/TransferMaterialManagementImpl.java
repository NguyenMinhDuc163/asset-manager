package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Material;
import com.ptit.asset.domain.Place;
import com.ptit.asset.domain.TransferMaterial;
import com.ptit.asset.domain.enumeration.MaterialStatus;
import com.ptit.asset.dto.request.TransferMaterialCreateRequestDTO;
import com.ptit.asset.repository.MaterialRepository;
import com.ptit.asset.repository.PlaceRepository;
import com.ptit.asset.repository.TransferMaterialRepository;
import com.ptit.asset.repository.UserRepository;
import com.ptit.asset.service.manager.TransferMaterialManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferMaterialManagementImpl implements TransferMaterialManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private TransferMaterialRepository transferMaterialRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Try<TransferMaterial> getOne(Long id) {
        return Try.of(() -> transferMaterialRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when find transfer material by id: "+id)));
    }

    @Override
    public Try<TransferMaterial> create(TransferMaterialCreateRequestDTO dto) {

        val material = materialRepository.findById(dto.getEmbedded().getMaterialId());
        if (!material.isPresent()){
            return Try.failure(new Exception("Failure when find material by id: "+dto.getEmbedded().getMaterialId()));
        }
        val user = userRepository.findById(dto.getEmbedded().getUserId());
        if (!user.isPresent()){
            return Try.failure(new Exception("Failure when find user by id: "+dto.getEmbedded().getUserId()));
        }
        val placeTarget = placeRepository.findById(dto.getEmbedded().getPlaceTargetId());
        if (!placeTarget.isPresent()){
            return Try.failure(new Exception("Failure when find place target by id: "+dto.getEmbedded().getPlaceTargetId()));
        }

//        if (dto.getEmbedded().getPlaceFromId() == null){
//
//        }
//        val placeFrom = placeRepository.findById(dto.getEmbedded().getPlaceFromId());
//        if (!placeFrom.isPresent()){
//            return Try.failure(new Exception("Failure when find place From by id: "+dto.getEmbedded().getPlaceFromId()));
//        }


        // validation Data here
        String validConfirm = validationData(dto, material.get(),
                (dto.getEmbedded().getPlaceFromId() != null) ?
                        placeRepository.findById(dto.getEmbedded().getPlaceFromId()).get() : null
                , placeTarget.get());
        if (!validConfirm.equalsIgnoreCase("ACCEPTABLE")){
            return Try.failure(new Exception(validConfirm));
        }
        // validation Data here

        System.out.println("=================== PASS validation ...");
        val transferMaterial = centralMapper.toTransferMaterial(dto,
                (dto.getEmbedded().getPlaceFromId() != null) ?
                        placeRepository.findById(dto.getEmbedded().getPlaceFromId()).get() : null
                ,placeTarget.get(),material.get(),user.get());

        System.out.println("=================== Build transfer material success ...");
//        val result = Try.of(() -> transferMaterialRepository.save(transferMaterial));
//        TransferMaterial transferMaterialCreated = transferMaterialRepository.save(transferMaterial);
//        if (transferMaterialCreated != null){
//            Material materialUpdate = transferMaterialCreated.getMaterial();
//            materialUpdate.setCurrentPlace(transferMaterialCreated.getPlaceTarget());
//            materialRepository.save(materialUpdate);
//            System.out.println("=================== Update current place of material ...");
//            return Try.of(() -> transferMaterialCreated);
//        }
//        if (result.isSuccess()){
//            TransferMaterial transferMaterialCreated = result.get();
//            Material materialUpdate = transferMaterialCreated.getMaterial();
//            materialUpdate.setCurrentPlace(transferMaterialCreated.getPlaceTarget());
//            materialRepository.save(materialUpdate);
//            System.out.println("=================== Update current place of material ...");
//            return Try.of(() -> transferMaterialCreated);
//        }

        return Try.of(() -> transferMaterialRepository.save(transferMaterial))
        .flatMap(transferMaterialCreated -> {
            Material materialUpdate = transferMaterialCreated.getMaterial();
            materialUpdate.setCurrentPlace(transferMaterialCreated.getPlaceTarget());
            materialRepository.save(materialUpdate);
            System.out.println("=================== Update current place of material ...");
                return Try.of(() -> transferMaterialCreated);
        }).orElse(() -> Try.failure(new Exception("Failure when save transfer material")));

    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            transferMaterialRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delte transfer material by id: "+id)));
    }

    @Override
    public List<TransferMaterial> fetchAll() {
        return transferMaterialRepository.findAll();
    }


    // -----------------------------------------------------------------------------------------------------------------
    protected String validationData(TransferMaterialCreateRequestDTO dto,Material material, Place placeFrom, Place placeTarget){

        if (material.getStatus().equals(MaterialStatus.NO_LONGER)){
            return "Material no longer so can't transfer";
        }
        if (placeFrom != null){
            if (placeTarget.equals(placeFrom)){
                return "Place from and Place target must different";
            }
        }

        if (material.getCurrentPlace() != null){
            if (!material.getCurrentPlace().getId().equals(dto.getEmbedded().getPlaceFromId())){
                return "Current place of material not matched with place from";
            }
        }

        if (material.getCurrentPlace() == null && (dto.getEmbedded().getPlaceFromId() != null)){
            return "Current place of material not available - request not valid";
        }

        TransferMaterial transferMaterial =
                transferMaterialRepository.findTopByMaterialIdOrderByIdDesc(dto.getEmbedded().getMaterialId());
        if (transferMaterial != null){
            System.out.println("========================== Transfer Material Last Recent ==========================");
            System.out.println(transferMaterial.toString());
            if (! dto.getEmbedded().getPlaceFromId().equals(transferMaterial.getPlaceTarget().getId())){
                return "Place From Not Valid - Must is place most recent of material";
            }
        }
        return "ACCEPTABLE";
    }
}
