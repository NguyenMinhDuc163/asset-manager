package com.ptit.asset.web.rest.v1;

import com.ptit.asset.dto.data.RestResponseUtils;
import com.ptit.asset.dto.request.InventoryMaterialCreateRequestDTO;
import com.ptit.asset.dto.request.TransferMaterialCreateRequestDTO;
import com.ptit.asset.service.endservice.InventoryMaterialService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
//@PreAuthorize("hasRole('ACCOUNTANT')")
public class InventoryMaterialResourcesAPI {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InventoryMaterialService inventoryMaterialService;


    @GetMapping("/inventoryMaterial/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to get inventory material by id >>>>> : {}", id);
        val result = inventoryMaterialService.getOne(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Get inventory material success", 200,
                        "Get inventory material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

//    @PreAuthorize("hasRole('INSPECTOR')")
    @PostMapping("/inventoryMaterial:create") // todo: THIS API SHOULD BE CALL BY SCAN QR Code ACTION
    public ResponseEntity<?> create(@RequestBody @Valid InventoryMaterialCreateRequestDTO dto){
        logger.info(">>>>> Rest request to create inventory material >>>>> : {}", dto);
        val result = inventoryMaterialService.create(dto);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Create inventory material success", 200,
                        "Create inventory material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @DeleteMapping("/inventoryMaterial:delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        logger.info(">>>>> Rest request to delete inventory material >>>>> : {}", id);
        val result = inventoryMaterialService.delete(id);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Delete inventory material success", 200,
                        "Delete inventory material failure", 500),
                result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/inventoryMaterial:fetchAll")
    public ResponseEntity<?> fetchAll(){
        logger.info(">>>>> Rest request to fetch all inventory material >>>>>");
        val result = inventoryMaterialService.fetchAll();
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch all inventory material success", 200), HttpStatus.OK
        );
    }

    @GetMapping("/inventoryMaterial:fetchByUser/{userId}")
    public ResponseEntity<?> fetchByUser(@PathVariable("userId") Long userId){
        logger.info(">>>>> Rest request to fetch inventory material of user >>>>>");
        val result = inventoryMaterialService.fetchByUser(userId);
        return new ResponseEntity<>(
                RestResponseUtils.create(result,"Fetch inventory material of user success", 200), HttpStatus.OK
        );
    }

}
