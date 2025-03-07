package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Inventory;
import com.ptit.asset.dto.request.InventoryChangeStatusRequestDTO;
import com.ptit.asset.dto.request.InventoryCreateRequestDTO;
import com.ptit.asset.repository.InventoryRepository;
import com.ptit.asset.service.manager.InventoryManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryManagementImpl implements InventoryManagement {


    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private InventoryRepository inventoryRepository;


    @Override
    public Try<Inventory> getOne(Long id) {
        return Try.of(() -> inventoryRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get inventory by id: "+id)));
    }

    @Override
    public Try<Inventory> create(InventoryCreateRequestDTO dto) {
        if (dto.getStartTime().compareTo(dto.getEndTime()) > 0){
            return Try.failure(new Exception("Start time must less than End time"));
        }

        String confirm = validationData();
        if (!confirm.equalsIgnoreCase("ACCEPTABLE")){
            return Try.failure(new Exception(confirm));
        }

        val inventory = centralMapper.toInventory(dto);
        inventory.setInCheck(true);// available for checker scan QR code

        return Try.of(() -> inventoryRepository.save(inventory))
            .orElse(() -> Try.failure(new Exception("Failure when save inventory")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            inventoryRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete inventory by id: "+id)));
    }

    @Override
    public List<Inventory> fetchAll() {
        return inventoryRepository.findAll();
    }

    @Override
    public Try<Boolean> changeStatus(InventoryChangeStatusRequestDTO dto) {
        val inventory = inventoryRepository.findById(dto.getId());
        if (!inventory.isPresent()){
            return Try.failure(new Exception("Failure when find inventory with id:"+dto.getId()));
        }
        Inventory inventoryUpdate = inventory.get();

        // logic space
        if (dto.getStatus().equals(inventoryUpdate.getInCheck())){
            return Try.failure(new Exception("Nothing change with current status"));
        }

        if (dto.getStatus() && !inventoryUpdate.getInCheck()){
             List<Inventory> inventoryList = inventoryRepository.findAll();
             for(Inventory element: inventoryList){
                 if (element.getInCheck()){
                     return Try.failure(new Exception("Just only one inventory allow in check"));
                 }
             }
        }
        // logic space

        inventoryUpdate.setInCheck(dto.getStatus());
        return Try.of(() -> {
            inventoryRepository.save(inventoryUpdate);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when change status of inventory")));

    }


    // -----------------------------------------------------------------------------------------------------------------
    protected String validationData(){
        List<Inventory> inventoryList = inventoryRepository.findAll();
        for (Inventory inventory : inventoryList){
            if (inventory.getInCheck()){
                return "Already existing inventory with status (IN_CHECK)";
            }
        }
        return "ACCEPTABLE";
    }

}
