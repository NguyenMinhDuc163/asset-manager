package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Inventory;
import com.ptit.asset.domain.InventoryMaterial;
import com.ptit.asset.dto.request.InventoryMaterialCreateRequestDTO;
import com.ptit.asset.dto.response.ScanQrCodeInfoResponseDTO;
import com.ptit.asset.repository.*;
import com.ptit.asset.service.manager.InventoryMaterialManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InventoryMaterialManagementImpl implements InventoryMaterialManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private InventoryMaterialRepository inventoryMaterialRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaceRepository placeRepository;

    @Override
    public Try<InventoryMaterial> getOne(Long id) {
        return Try.of(() -> inventoryMaterialRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when find inventory material by id: "+id)));
    }

//    @Override // todo: THIS API SHOULD BE CALL BY SCAN QR Code ACTION
//    public Try<InventoryMaterial> create(InventoryMaterialCreateRequestDTO dto) {
//
//        /*
//        ** Have two case for this api
//        * - Case 1: Client Scan QR Code and system not have any inventory (In Check) now -> Just display info
//        * - Case 2: Client Scan QR Code and system is have "only one" inventory (In Check) now -> Display info + make save to database
//         */
//
//        // TODO: Step 1 - Check Data Inventory
//        Inventory inventory_InCheck = inventoryRepository.findTopByInCheckOrderByIdDesc(true);
//        if (inventory_InCheck == null){ // Fall into case 1
//
//            val material = materialRepository.findById(dto.getEmbedded().getMaterialId());
//            if (!material.isPresent()){
//                return Try.failure(new Exception("Failure when find material by id: "+dto.getEmbedded().getMaterialId()));
//            }
//
//
//        }
////        val inventory = inventoryRepository.findById(dto.getEmbedded().getInventoryId());
////        if (!inventory.isPresent()){
////            return Try.failure(new Exception("Failure when find inventory by id: "+dto.getEmbedded().getInventoryId()));
////        }
//        // todo: Here should be find only one inventory in check conduct
//
//        val material = materialRepository.findById(dto.getEmbedded().getMaterialId());
//        if (!material.isPresent()){
//            return Try.failure(new Exception("Failure when find material by id: "+dto.getEmbedded().getMaterialId()));
//        }
//        val user = userRepository.findById(dto.getEmbedded().getUserId());
//        if (!user.isPresent()){
//            return Try.failure(new Exception("Failure when find user by id: "+dto.getEmbedded().getUserId()));
//        }
////        val place = placeRepository.findById(dto.getEmbedded().getPlaceId());
////        if (!place.isPresent()){
////            return Try.failure(new Exception("Failure when find user by id: "+dto.getEmbedded().getPlaceId()));
////        }
//
////        val inventoryMaterial = centralMapper.toInventoryMaterial(inventory.get(),material.get(),user.get(),place.get());
//        val inventoryMaterial = centralMapper.toInventoryMaterial(inventory.get(),material.get(),user.get());
//
//        return Try.of(() -> inventoryMaterialRepository.save(inventoryMaterial))
//            .orElse(() -> Try.failure(new Exception("Failure when save inventory material")));
//    }


    /*
     ***< Have two case for this api >
     * - Case 1: Client Scan QR Code and system not have any inventory now -> Just display info
     * - Case 2: Client Scan QR Code and system is have latest inventory (Not In Check) now => Display info | (In Check) now -> Display info + make save to database
     */
    @Override // todo: THIS API SHOULD BE CALL BY SCAN QR Code ACTION
    @Transactional
    public Try<ScanQrCodeInfoResponseDTO> create(InventoryMaterialCreateRequestDTO dto) {

        // TODO: Step 1 - Check Data Inventory (here should be find only one inventory in check conduct)
        Inventory inventory = inventoryRepository.findTop();


        if (inventory == null){ // Fall into case 1

            System.out.println("==========================> Fall into case 1");
            val material = materialRepository.findById(dto.getEmbedded().getMaterialId());
            if (!material.isPresent()){
                return Try.failure(new Exception("Failure when find material by id: "+dto.getEmbedded().getMaterialId()));
            }
            val materialResult = material.get();
            ScanQrCodeInfoResponseDTO responseDTO =
                    ScanQrCodeInfoResponseDTO.builder()
                    .credentialCode(materialResult.getCredentialCode())
                    .nameDetail(materialResult.getProduct().getName() + "-" + materialResult.getProduct().getDescription())
                    .status(materialResult.getStatus().name())
                    .currentPlace((materialResult.getCurrentPlace() != null) ?
                            materialResult.getCurrentPlace().getCode() + "-" + materialResult.getCurrentPlace().getNameSpecification()
                            : "No Place")
                    .message("System now not have any inventory so don't save history your scan")
                    .build();

            return Try.of(() -> responseDTO);
        }

        else { // Fall into case 2
            System.out.println("==========================> Fall into case 2");

            if (inventory.getInCheck()){
                System.out.println("***** DETAIL: inventory latest now in check");
                val material = materialRepository.findById(dto.getEmbedded().getMaterialId());
                if (!material.isPresent()){
                    return Try.failure(new Exception("Failure when find material by id: "+dto.getEmbedded().getMaterialId()));
                }

                val user = userRepository.findById(dto.getEmbedded().getUserId());
                if (!user.isPresent()){
                    return Try.failure(new Exception("Failure when find user by id: "+dto.getEmbedded().getUserId()));
                }

                InventoryMaterial inventoryMaterialUnique =
                        inventoryMaterialRepository.findByInventoryIdAndMaterialId(inventory.getId(), material.get().getId());
                if (inventoryMaterialUnique != null){
                    System.out.println("This inventory material already exist =>> Just Display");
                    val materialResult = material.get();
                    ScanQrCodeInfoResponseDTO responseDTO =
                        ScanQrCodeInfoResponseDTO.builder()
                            .credentialCode(materialResult.getCredentialCode())
                            .nameDetail(materialResult.getProduct().getName() + "-" + materialResult.getProduct().getDescription())
                            .status(materialResult.getStatus().name())
                            .currentPlace((materialResult.getCurrentPlace() != null) ?
                                    materialResult.getCurrentPlace().getCode() + "-" + materialResult.getCurrentPlace().getNameSpecification()
                                    : "No Place")
                            .message("You are on inventory available to check - Display Success")
                            .build();
                    return Try.of(() -> responseDTO);
                }

                Boolean checkCreated = false;
                InventoryMaterial inventoryMaterialCred =
                        inventoryMaterialRepository.findByInventoryIdAndMaterialIdAndUserId
                                (inventory.getId(),material.get().getId(),user.get().getId());
                if (inventoryMaterialCred == null){
                    System.out.println("This inventory material not exist before =>> Create and Display");
                    val inventoryMaterial = centralMapper.toInventoryMaterial(inventory,material.get(),user.get());
                    inventoryMaterialRepository.save(inventoryMaterial);
                    checkCreated = true;
                } else {
                    System.out.println("This inventory material already exist =>> Just Display");
                }
                    val materialResult = material.get();
                    ScanQrCodeInfoResponseDTO responseDTO =
                    ScanQrCodeInfoResponseDTO.builder()
                        .credentialCode(materialResult.getCredentialCode())
                        .nameDetail(materialResult.getProduct().getName() + "-" + materialResult.getProduct().getDescription())
                        .status(materialResult.getStatus().name())
                        .currentPlace((materialResult.getCurrentPlace() != null) ?
                                materialResult.getCurrentPlace().getCode() + "-" + materialResult.getCurrentPlace().getNameSpecification()
                                : "No Place")
                        .message("You are on inventory available to check - " + (checkCreated ? "Create Inventory and display success" : "Display success"))
                        .build();
                    return Try.of(() -> responseDTO);
            }

            else {
                System.out.println("***** DETAIL: inventory latest not in check =>> JUST DISPLAY");
                val material = materialRepository.findById(dto.getEmbedded().getMaterialId());
                if (!material.isPresent()){
                    return Try.failure(new Exception("Failure when find material by id: "+dto.getEmbedded().getMaterialId()));
                }

                val materialResult = material.get();
                ScanQrCodeInfoResponseDTO responseDTO =
                ScanQrCodeInfoResponseDTO.builder()
                    .credentialCode(materialResult.getCredentialCode())
                    .nameDetail(materialResult.getProduct().getName() + "-" + materialResult.getProduct().getDescription())
                    .status(materialResult.getStatus().name())
                    .currentPlace(
                            materialResult.getCurrentPlace() != null ?
                                    (materialResult.getCurrentPlace().getCode() + "-" + materialResult.getCurrentPlace().getNameSpecification()):
                                    "Now material not place any where"
                    )
                    .message("You are on inventory not available to check - Display success")
                    .build();
                return Try.of(() -> responseDTO);
            }
        }

    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            inventoryMaterialRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete inventory material by id: "+id)));
    }

    @Override
    public List<InventoryMaterial> fetchAll() {
        return inventoryMaterialRepository.findAll();
    }

    @Override
    public List<InventoryMaterial> fetchByUser(Long userId) {
        Inventory inventory = inventoryRepository.findTop();
        if (inventory == null){
            return new ArrayList<>();
        }
        return inventoryMaterialRepository.findAllByInventoryIdAndUserId(inventory.getId(), userId);
    }
}
